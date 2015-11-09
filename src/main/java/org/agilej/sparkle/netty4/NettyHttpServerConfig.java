package org.agilej.sparkle.netty4;

public interface NettyHttpServerConfig {

    boolean isStaticServeEnabled();

    StaticResourceRouteMatcher staticResourceRouteMatcher();

    /**
     * Whether use Netty's NioEventLoop as Sparkle's controller execution thread.
     * Default is true.
     * @return
     */
    boolean useNioEventLoopAsFrameworkExecutor();

}
