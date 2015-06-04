package me.donnior.sparkle.netty4;


import io.netty.handler.codec.http.HttpRequest;

public class DefaultNettyHttpServerConfig implements NettyHttpServerConfig{

    private static  StaticResourceRouteMatcher DEFAULT_STATIC_RESOURCE_MATCHER = new StaticResourceRouteMatcher(){

        @Override
        public boolean isStaticResource(HttpRequest request) {
            return request.getUri().startsWith("/static/");
        }
    };

    @Override
    public boolean isStaticServeEnabled() {
        return true;
    }

    @Override
    public StaticResourceRouteMatcher staticResourceRouteMatcher() {
        return DEFAULT_STATIC_RESOURCE_MATCHER;
    }


}
