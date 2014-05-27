
package me.donnior.sparkle.netty;

import java.util.HashMap;
import java.util.Map;

import me.donnior.sparkle.HTTPMethod;
import me.donnior.sparkle.engine.SparkleEngine;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    private static SparkleEngine sparkle = new SparkleEngine(new NettySpecific());
    
    static Map<HttpMethod, HTTPMethod> METHOD_MAP = new HashMap<HttpMethod, HTTPMethod>(); 
    
    static {
        METHOD_MAP.put(HttpMethod.GET, HTTPMethod.GET);
        METHOD_MAP.put(HttpMethod.POST, HTTPMethod.POST);
        METHOD_MAP.put(HttpMethod.PUT, HTTPMethod.PUT);
        METHOD_MAP.put(HttpMethod.DELETE, HTTPMethod.DELETE);
        METHOD_MAP.put(HttpMethod.HEAD, HTTPMethod.HEAD);
        METHOD_MAP.put(HttpMethod.OPTIONS, HTTPMethod.OPTIONS);
        METHOD_MAP.put(HttpMethod.TRACE, HTTPMethod.TRACE);
        
        //TODO support more http methods
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request  = (HttpRequest) e.getMessage();
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        
        boolean keepAlive = request.isKeepAlive(); 
        
        NettyWebRequest webRequest = new NettyWebRequest(request, response);
        
        sparkle.doService(webRequest, methodFor(request.getMethod()));
        
        NettyWebResponse nwr = (NettyWebResponse)webRequest.getWebResponse();
        nwr.prepareFlush();
        
        ChannelFuture future = e.getChannel().write(response);
        if(!keepAlive){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
    
    private HTTPMethod methodFor(HttpMethod method) {
        return METHOD_MAP.get(method);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
