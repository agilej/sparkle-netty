package org.agilej.sparkle.netty4;

import io.netty.channel.ChannelHandlerContext;
import org.agilej.sparkle.WebRequest;


public interface AsyncRequestHandler {

    public void completeAsync(ChannelHandlerContext ctx, WebRequest webRequest);

}
