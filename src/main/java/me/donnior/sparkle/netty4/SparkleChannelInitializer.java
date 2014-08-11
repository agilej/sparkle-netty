package me.donnior.sparkle.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class SparkleChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        
        pipeline.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
//        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        
        /* 
         * HttpObjectAggregator will transform multiple messages into a single 
         * FullHttpRequest or FullHttpResponse
         */
        pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        
        //pipeline.addLast("deflater", new HttpContentCompressor());
        
        pipeline.addLast("static", new HttpStaticFileServerHandler(true));
        pipeline.addLast("handler", new SpakelExecutionHandler());

        
    }
}