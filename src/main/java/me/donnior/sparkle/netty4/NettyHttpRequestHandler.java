package me.donnior.sparkle.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.donnior.sparkle.HTTPMethod;
import me.donnior.sparkle.engine.SparkleEngine;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.*;

public class NettyHttpRequestHandler extends SimpleChannelInboundHandler {

    private static SparkleEngine sparkle = new SparkleEngine(
            new NettySpecific());

    static Map<HttpMethod, HTTPMethod> METHOD_MAP = new HashMap<HttpMethod, HTTPMethod>();

    static {
        METHOD_MAP.put(HttpMethod.GET, HTTPMethod.GET);
        METHOD_MAP.put(HttpMethod.POST, HTTPMethod.POST);
        METHOD_MAP.put(HttpMethod.PUT, HTTPMethod.PUT);
        METHOD_MAP.put(HttpMethod.DELETE, HTTPMethod.DELETE);
        METHOD_MAP.put(HttpMethod.HEAD, HTTPMethod.HEAD);
        METHOD_MAP.put(HttpMethod.OPTIONS, HTTPMethod.OPTIONS);
        METHOD_MAP.put(HttpMethod.TRACE, HTTPMethod.TRACE);

        // TODO support more http methods
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object e)
            throws Exception {
        System.out.println("in channel read " +  e.getClass());
        if (e instanceof HttpRequest) {
            
            DefaultHttpRequest request = (DefaultHttpRequest) e;
                        // boolean keepAlive = request.isKeepAlive();

            
            NettyWebRequestAdapter webRequest = new NettyWebRequestAdapter(request);

            System.out.println("processing request with sparkle!!");
            sparkle.doService(webRequest, methodFor(request.getMethod()));

            NettyWebResponseAdapter nwr = (NettyWebResponseAdapter) webRequest.getWebResponse();
            nwr.prepareFlush();
            
            
            DefaultFullHttpResponse original = nwr.getOriginalResponse();
            

            ctx.writeAndFlush(original);
        }
//        
//        if (e instanceof HttpContent) {
//            HttpContent httpContent = (HttpContent) e;
//            
//            ByteBuf content = httpContent.content();
//            System.out.println("get a httpcontent : " + content.toString(Charset.forName("UTF-8")));
//        }
//        

        // 
        // if(!keepAlive){
        // future.addListener(ChannelFutureListener.CLOSE);
        // }
    }

    private HTTPMethod methodFor(HttpMethod method) {
        return METHOD_MAP.get(method);
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
