package com.stoney.reactor.jerysey;

import com.stony.reactor.jersey.JacksonProvider;
import com.stony.reactor.jersey.JerseyBasedHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.net.URI;
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
        HttpServer.create(opts -> opts.port(8082))
                .startAndAwait(JerseyBasedHandler.builder()
                        .withClassPath("com.stoney.reactor.jerysey.router")
                        .addValueProvider(JacksonProvider.class)
                        .withRouter(routes -> {
                            routes.get("/get", (req, resp) -> resp.sendString(Mono.just("asdfasdf")))
                            .directory("/s", resource)
                            .get("/v2/get", (req, resp) -> {
                                System.out.println(req.params());
                                System.out.println(req.path());
                                System.out.println(req.uri());
                                System.out.println("id = " + req.param("id"));
                                System.out.println("name = " + req.param("name"));
                                return resp
                                        .header(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8")
                                        .chunkedTransfer(false)
                                        .sendString(Mono.just(req.params().toString()));
                            });
                        }).build()
                );

    }

}
