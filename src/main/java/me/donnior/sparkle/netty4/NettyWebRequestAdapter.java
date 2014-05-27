package me.donnior.sparkle.netty4;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.donnior.sparkle.WebRequest;
import me.donnior.sparkle.WebResponse;

public class NettyWebRequestAdapter implements WebRequest {

    private HttpRequest request;
    private NettyWebResponseAdapter webResponse;
    private QueryStringDecoder decoder;
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public NettyWebRequestAdapter(HttpRequest request) {
        this.request = request;
        this.webResponse = new NettyWebResponseAdapter();
        this.decoder = new QueryStringDecoder(this.request.getUri());
    }
    
    public NettyWebRequestAdapter(HttpRequest request, NettyWebResponseAdapter webResponse) {
        this.request = request;
        this.webResponse = webResponse;
    }

    @Override
    public String getBody() {
        if(this.request instanceof HttpContent){
            return ((HttpContent)this.request).content().toString(Charset.forName("UTF-8"));
        } else {
            throw new RuntimeException("Unsupport get body for class not a HttpContent");
        }
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

}
