package com.stony.reactor.jersey;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.ContainerProvider;
import com.sun.jersey.spi.container.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>reactor-netty-ext
 * <p>com.stony.reactor.jersey
 *
 * @author stony
 * @version 上午10:51
 * @since 2018/1/19
 * @see netflix.karyon.jersey.blocking.NettyContainerProvider
 */
public class NettyContainerProvider implements ContainerProvider<NettyContainer> {
    private static final Logger logger = LoggerFactory.getLogger(NettyContainerProvider.class);
    @Override
    public NettyContainer createContainer(Class<NettyContainer> type, ResourceConfig resourceConfig, WebApplication application) throws ContainerException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(application);
        if (!type.equals(NettyContainer.class)) {
            logger.error(
                    "Netty container provider can only create container of type {}. Invoked to create container of type {}",
                    NettyContainer.class.getName(), type.getName());
        }
        return new NettyContainer(application);
    }
}
