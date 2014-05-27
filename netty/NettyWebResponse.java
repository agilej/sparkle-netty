package me.donnior.sparkle.netty;

import me.donnior.sparkle.WebResponse;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class NettyWebResponse implements WebResponse {

    private HttpResponse response;
    private StringBuilder sb = new StringBuilder();

    public NettyWebResponse(HttpResponse response) {
        this.response = response;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOriginalResponse() {
        return (T) this.response;
    }

    @Override
    public void setStatus(int sc) {
        this.response.setStatus(HttpResponseStatus.valueOf(sc));
    }

    @Override
    public void write(String string) {
        this.sb.append(string);
    }

    @Override
    public void setHeader(String name, String value) {
        this.response.setHeader(name, value);
    }

    @Override
    public void setContentType(String type) {
        this.response.setHeader(HttpHeaders.Names.CONTENT_TYPE, type);
    }

    
    public void prepareFlush() {
        @SuppressWarnings("deprecation")
        ChannelBuffer buf = ChannelBuffers.copiedBuffer(this.sb.toString(), "UTF-8");
        sb.setLength(0);

        response.setContent(buf);
        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buf.readableBytes()));
    }

}
