package me.donnior.sparkle.netty;


import me.donnior.sparkle.WebRequest;
import me.donnior.sparkle.WebResponse;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class NettyWebRequest implements WebRequest {

    private HttpRequest request;
    private WebResponse webResponse;

    public NettyWebRequest(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.webResponse = new NettyWebResponse(response);
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
    public String getParameter(String arg0) {
        throw new RuntimeException("to be implementated");
    }

    @Override
    public String[] getParameterValues(String arg0) {
        throw new RuntimeException("to be implementated");
    }

    @Override
    public String getPath() {
        return this.request.getUri();
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
