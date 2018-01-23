package com.stoney.reactor.jerysey;

import com.stony.reactor.jersey.JacksonProvider;
import com.stony.reactor.jersey.JerseyBasedHandler;
import org.junit.Test;
import reactor.ipc.netty.http.server.HttpServer;

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
    public void test_start(){
        HttpServer.create(8080)
                .startAndAwait(JerseyBasedHandler.builder()
                        .withClassPath("com.stoney.reactor.jerysey.router")
                        .addValueProvider(JacksonProvider.class)
                        .build());

    }
}
