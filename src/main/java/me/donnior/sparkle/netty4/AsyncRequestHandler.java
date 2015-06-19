package me.donnior.sparkle.netty4;

import io.netty.channel.ChannelHandlerContext;
import me.donnior.sparkle.WebRequest;


public interface AsyncRequestHandler {

    public void completeAsync(ChannelHandlerContext ctx, WebRequest webRequest);

}
