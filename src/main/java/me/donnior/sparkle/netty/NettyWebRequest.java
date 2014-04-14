package me.donnior.sparkle.netty;


import java.util.List;

import me.donnior.sparkle.WebRequest;
import me.donnior.sparkle.WebResponse;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

public class NettyWebRequest implements WebRequest {

    private HttpRequest request;
    private WebResponse webResponse;
    private QueryStringDecoder decoder;

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
    

}
