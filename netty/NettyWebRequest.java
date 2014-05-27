package me.donnior.sparkle.netty;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.donnior.sparkle.WebRequest;
import me.donnior.sparkle.WebResponse;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

public class NettyWebRequest implements WebRequest {
    
    private HttpRequest request;
    private WebResponse webResponse;
    private QueryStringDecoder decoder;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    
    public NettyWebRequest(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.webResponse = new NettyWebResponse(response);
        this.decoder = new QueryStringDecoder(this.request.getUri());
    }
    
    public NettyWebRequest(HttpRequest request, WebResponse webResponse) {
        this.request = request;
        this.webResponse = webResponse;
    }
    
    @Override
    public String getBody(){
        return this.request.getContent().toString("UTF-8");
    }
    
    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public String getHeader(String name) {
        return this.request.getHeader(name);
    }

    @Override
    public String getMethod() {
        return this.request.getMethod().getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOriginalRequest() {
        return (T)this.request;
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return values.length > 0 ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(String name) {
        //TODO add get params from nettyp post body
//        if(this.getMethod().toLowerCase().equals("post")){
//            throw new RuntimeException("currently not support get param from post request");
//        }
        try {
            this.messageReceived();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        List<String> values = this.decoder.getParameters().get(name);
        if(values == null) {
            return new String[]{};
        }
        int size =  values.size();
        String[] valueArr = new String[size];
        return values.toArray(valueArr);
    }

    @Override
    public String getPath() {
        return this.decoder.getPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOriginalResponse() {
        return (T)this.webResponse.getOriginalResponse();
    }
    
    @Override
    public WebResponse getWebResponse() {
        return this.webResponse;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getAttribute(String name) {
        return (T)this.attributes.get(name);
    }
    
    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }
    
    
    public void messageReceived() throws Exception {
        
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), this.request);
        
        List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
        for(InterfaceHttpData data : datas){
            if (data.getHttpDataType() == HttpDataType.Attribute) {
                Attribute attribute = (Attribute) data;
                String value = attribute.getValue();
                String name = attribute.getName();
                System.out.println(name + " :" + value);
             }
        }
        
      }

}
