package me.donnior.sparkle.netty4;


import io.netty.handler.codec.http.HttpRequest;

public interface StaticResourceRouteMatcher {

    boolean isStaticResource(HttpRequest request);

}
