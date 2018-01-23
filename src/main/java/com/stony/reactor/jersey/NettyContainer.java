package com.stony.reactor.jersey;

import com.sun.jersey.spi.container.WebApplication;

/**
 * <p>reactor-netty-ext
 * <p>com.stony.reactor.jersey
 *
 * @author stony
 * @version 上午10:51
 * @since 2018/1/19
 * @see netflix.karyon.jersey.blocking.NettyContainer
 */
public class NettyContainer {

    private final WebApplication application;
    private final NettyToJerseyBridge nettyToJerseyBridge;

    public NettyContainer(WebApplication application) {
        this.application = application;
        nettyToJerseyBridge = new NettyToJerseyBridge(application);
    }

    NettyToJerseyBridge getNettyToJerseyBridge() {
        return nettyToJerseyBridge;
    }

    WebApplication getApplication() {
        return application;
    }
}