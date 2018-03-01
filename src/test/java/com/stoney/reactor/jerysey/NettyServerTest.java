package com.stoney.reactor.jerysey;

import com.stony.reactor.jersey.JacksonProvider;
import com.stony.reactor.jersey.JerseyBasedHandler;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>reactor-netty-ext
 * <p>com.stoney.reactor.jerysey
 *
 * @author stony
 * @version 下午6:01
 * @since 2018/1/23
 */
public class NettyServerTest {

    @Test
    public void test_start() throws URISyntaxException {
        final Path resource = Paths.get(NettyServerTest.class.getResource("/public").toURI());
        HttpServer.create(8080)
                .startAndAwait(JerseyBasedHandler.builder()
                        .withClassPath("com.stoney.reactor.jerysey.router")
                        .addValueProvider(JacksonProvider.class)
                        .addRouter(routes -> {
                            routes.get("/get", (req, resp) -> resp.sendString(Mono.just("asdfasdf")))
                            .directory("/s", resource);
                        }).build()
                );

    }

}
