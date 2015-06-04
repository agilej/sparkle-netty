package me.donnior.sparkle.netty4;

public interface NettyHttpServerConfig {

    boolean isStaticServeEnabled();

    StaticResourceRouteMatcher staticResourceRouteMatcher();

}
