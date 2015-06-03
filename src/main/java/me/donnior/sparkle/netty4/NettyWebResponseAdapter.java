package me.donnior.sparkle.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import me.donnior.sparkle.WebResponse;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class NettyWebResponseAdapter implements WebResponse {

    private DefaultFullHttpResponse response;
    private ByteBuf bb;
    private Writer writer;

    public NettyWebResponseAdapter() {
        this(HttpVersion.HTTP_1_1);
    }

    public NettyWebResponseAdapter(HttpVersion version) {
        this(new DefaultFullHttpResponse(version, HttpResponseStatus.OK, Unpooled.buffer()));
    }

    public NettyWebResponseAdapter(DefaultFullHttpResponse response) {
        this.response = response;
        this.bb = response.content();
        
        ByteBufOutputStream bbos = new ByteBufOutputStream(this.bb);
        this.writer = new BufferedWriter(new OutputStreamWriter(bbos));
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
        this.bb.writeBytes(string.getBytes());
    }

    @Override
    public void setHeader(String name, String value) {
        this.response.headers().set(name, value);
    }

    @Override
    public void setContentType(String type) {
        this.response.headers().set(HttpHeaders.Names.CONTENT_TYPE, type);
    }

    public void prepareFlush() {
        this.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(this.bb.readableBytes()));
    }

    @Override
    public Writer getWriter(){
        // ByteBufOutputStream bbos = new ByteBufOutputStream(this.bb);
        // return new BufferedWriter(new OutputStreamWriter(bbos));
        return this.writer;
    }

    public void closeWriter(){
        try{
            this.writer.close();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
