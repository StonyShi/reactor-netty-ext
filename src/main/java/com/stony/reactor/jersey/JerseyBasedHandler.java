package com.stony.reactor.jersey;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponseWriter;
import com.sun.jersey.spi.container.WebApplication;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.http.server.HttpServerRequest;
import reactor.ipc.netty.http.server.HttpServerResponse;
import reactor.ipc.netty.http.server.HttpServerRoutes;
import reactor.ipc.netty.http.server.SimpleHttpServerRoutes;

import javax.annotation.PreDestroy;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>reactor-netty-ext
 * <p>com.stony.reactor.jersey
 *
 * @author stony
 * @version 下午6:38
 * @since 2018/1/19
 */
public class JerseyBasedHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>>, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(JerseyBasedHandler.class);

    private WebApplication application;
    private NettyToJerseyBridge nettyToJerseyBridge;
    private final ResourceConfig resourceConfig;
    private volatile boolean isShutdown = false;

    /**
     * @param pkgNamesStr
     */
    public JerseyBasedHandler(String pkgNamesStr) {
        this(new ClassPathResourceConfig(pkgNamesStr));
    }

    public JerseyBasedHandler(ClassPathResourceConfig config) {
        resourceConfig = config;
        NettyContainer container = ContainerFactory.createContainer(NettyContainer.class, resourceConfig);
        NettyContainer.setHolderContainer(container);
        application = container.getApplication();
        nettyToJerseyBridge = container.getNettyToJerseyBridge();
        logger.info("Started Jersey based request router.");
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {

        HttpServerRoutes routes = HttpServerRoutes.newRoutes();

         /*
         * Creating the Container request eagerly, subscribes to the request content eagerly. Failure to do so, will
          * result in expiring/loss of content.
         */

        //we have to close input stream, to emulate normal lifecycle

        final InputStream requestData = new HttpContentInputStream(response.alloc(), request.receive());

        final ContainerRequest containerRequest = nettyToJerseyBridge.bridgeRequest(request, requestData);
        final ContainerResponseWriter containerResponse = nettyToJerseyBridge.bridgeResponse(response);

        return Mono.<Void>create(sink -> {
            try {
                application.handleRequest(containerRequest, containerResponse);
                sink.success();
            } catch (IOException e) {
                logger.error("Failed to handle request.", e);
                sink.error(e);
            } finally {
                //close input stream and release all data we buffered, ignore errors
                try {
                    requestData.close();
                } catch (IOException e) {
                }
            }
        }).subscribeOn(Schedulers.elastic());
    }

    @PreDestroy
    public void stop() {
        if (isShutdown) return;
        logger.info("Stopped Jersey based request router.");
        application.destroy();
        synchronized (this) {
            isShutdown = true;
        }
    }


    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void close() throws Exception {
        stop();
    }

    public static final class Builder {
        String classPath = "com.jersey";
        List<Class<?>> providerClass = Arrays.asList(MessageBodyReader.class, MessageBodyWriter.class);
        Set<Class<?>> providers = new HashSet<>(16);
        Consumer<HttpServerRoutes> routesBuilder;

        public Builder withClassPath(String classPath) {
            this.classPath = classPath;
            return this;
        }

        public Builder withRouter(Consumer<HttpServerRoutes> routesBuilder) {
            this.routesBuilder = routesBuilder;
            return this;
        }

        public Builder withClassPath(Collection<String> stringCollection) {
            this.classPath = stringCollection.stream().collect(Collectors.joining(":"));
            return this;
        }

        public Builder addValueProvider(Class<?> provider) {
            if (supportProvider(provider)) {
                providers.add(provider);
            }
            return this;
        }

        public boolean supportProvider(Class<?> provider) {
            for (Class<?> clazz : providerClass) {
                return clazz.isAssignableFrom(provider);
            }
            return false;
        }

        public BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> build() {
            ClassPathResourceConfig config = new ClassPathResourceConfig(this.classPath);
            if (!providers.isEmpty()) {
                config.addValueProviderClass(providers);
            }
            if (this.routesBuilder != null) {
                HttpServerRoutes routes = SimpleHttpServerRoutes.newRoutes(new JerseyBasedHandler(config));
                routesBuilder.accept(routes);
                return routes;
            }
            return new JerseyBasedHandler(config);
        }
    }
}
