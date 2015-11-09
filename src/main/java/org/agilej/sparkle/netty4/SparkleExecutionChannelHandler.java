package org.agilej.sparkle.netty4;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.agilej.sparkle.WebRequest;
import org.agilej.sparkle.engine.SparkleEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class SparkleExecutionChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements AsyncRequestHandler{

    private final static Logger logger = LoggerFactory.getLogger(SparkleExecutionChannelHandler.class);
    
    private static SparkleEngine sparkle = new SparkleEngine(new NettySpecific());

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest request)
            throws Exception {
//        logger.debug("begin to process http request using sparkle framework");

        final NettyWebRequestAdapter webRequest = new NettyWebRequestAdapter(ctx, request, this);
        sparkle.doService(webRequest, webRequest.getHttpMethod());

        if (webRequest.isAsync()){

        } else {
            completeAsync(ctx, webRequest);
        }

    }

    @Override
    public void completeAsync(ChannelHandlerContext ctx, WebRequest webRequest){
        NettyWebResponseAdapter nwr = (NettyWebResponseAdapter) webRequest.getWebResponse();
        nwr.prepareFlush();

        DefaultFullHttpResponse original = nwr.getOriginalResponse();

        FullHttpRequest request = webRequest.getOriginalRequest();

        boolean keepAlive = isKeepAlive(request);
        if (!keepAlive) {
            ctx.write(original).addListener(ChannelFutureListener.CLOSE);
        } else {
            original.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.write(original);
        }
        ctx.flush();
        nwr.closeWriter();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error(cause.getMessage());
    }
    
    private void test(HttpRequest httpRequest) throws IOException{
        Map<String, List<String>> requestParameters;

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
                httpRequest.getUri());
        requestParameters = queryStringDecoder.parameters();

        if (httpRequest.getMethod() == HttpMethod.POST) {
            // Add POST parameters
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                    new DefaultHttpDataFactory(false), httpRequest);
            try {
                while (decoder.hasNext()) {
                    InterfaceHttpData httpData = decoder.next();
                    if (httpData.getHttpDataType() == HttpDataType.Attribute) {
                        Attribute attribute = (Attribute) httpData;
                        if (!requestParameters.containsKey(attribute.getName())) {
                            requestParameters.put(attribute.getName(),
                                    new LinkedList<String>());
                        }
                        requestParameters.get(attribute.getName()).add(
                                attribute.getValue());
                        attribute.release();
                    }
                }
            } catch (HttpPostRequestDecoder.EndOfDataDecoderException ex) {
                // Exception when the body is fully decoded, even if there
                // is still data
            }

            decoder.destroy();
        }

       
        for (Entry<String, List<String>> entry : requestParameters.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (value.size() == 1)
                System.out.println(key + " : " + value.get(0));
            else
                System.out.println(key + " : " + value);
        }
    }
    

}
