package me.donnior.sparkle.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.donnior.sparkle.Multipart;
import me.donnior.sparkle.WebRequest;
import me.donnior.sparkle.WebResponse;

public class NettyWebRequestAdapter implements WebRequest {

    private FullHttpRequest request;
    private NettyWebResponseAdapter webResponse;
    private QueryStringDecoder decoder;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private AsyncRequestHandler asyncRequestHandler;
    private ChannelHandlerContext ctx;
    private volatile boolean async = false;

    public NettyWebRequestAdapter(ChannelHandlerContext ctx, FullHttpRequest request) {
        this(ctx, request, new NettyWebResponseAdapter(request.getProtocolVersion()));
    }

    public NettyWebRequestAdapter(ChannelHandlerContext ctx, FullHttpRequest request, AsyncRequestHandler asyncRequestHandler) {
        this(ctx, request, new NettyWebResponseAdapter(request.getProtocolVersion()), asyncRequestHandler);
    }

    public NettyWebRequestAdapter(ChannelHandlerContext ctx, FullHttpRequest request, NettyWebResponseAdapter webResponse) {
        this(ctx, request, webResponse, null);
    }

    public NettyWebRequestAdapter(ChannelHandlerContext ctx, FullHttpRequest request, NettyWebResponseAdapter webResponse, AsyncRequestHandler asyncRequestHandler) {
        this.ctx = ctx;
        this.request = request;
        this.webResponse = webResponse;
        this.decoder = new QueryStringDecoder(this.request.getUri());
        this.asyncRequestHandler = asyncRequestHandler;
    }

    @Override
    public String getBody() {
        return this.request.content().toString(Charset.forName("UTF-8"));
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getHeader(String name) {
        return this.request.headers().get(name);
    }

    @Override
    public String getMethod() {
        return this.request.getMethod().name();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOriginalRequest() {
        return (T) this.request;
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return values.length > 0 ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(String name) {
        // TODO add get params from nettyp post body
        // if(this.getMethod().toLowerCase().equals("post")){
        // throw new
        // RuntimeException("currently not support get param from post request");
        // }
        // try {
        // this.messageReceived();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        List<String> values = this.decoder.parameters().get(name);
        if (values == null) {
            return new String[] {};
        }
        int size = values.size();
        String[] valueArr = new String[size];
        return values.toArray(valueArr);
    }

    @Override
    public String getPath() {
        return this.decoder.path();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOriginalResponse() {
        return (T) this.webResponse.getOriginalResponse();
    }

    @Override
    public WebResponse getWebResponse() {
        return this.webResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name) {
        return (T) this.attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public List<Multipart> getMultiparts() {
        throw new RuntimeException("not implementated yet!");
    }

    public void startAsync(){
        this.async = true;
    }

    public boolean isAsync(){
        return this.async;
    }

    public void completeAsync(){
        this.asyncRequestHandler.completeAsync(this.ctx, this);
    }

    // public void messageReceived() throws Exception {
    //
    // HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new
    // DefaultHttpDataFactory(false), this.request);
    //
    // List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
    // for(InterfaceHttpData data : datas){
    // if (data.getHttpDataType() == HttpDataType.Attribute) {
    // Attribute attribute = (Attribute) data;
    // String value = attribute.getValue();
    // String name = attribute.getName();
    // System.out.println(name + " :" + value);
    // }
    // }
    //
    // }

    @Override
    public String toString(){
        return this.getMethod() + " " + this.getPath();
    }

}
