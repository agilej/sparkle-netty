package me.donnior.sparkle.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class SparkleChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final NettyHttpServerConfig config;
    private final EventExecutorGroup group;

    private SparkleExecutionChannelHandler sparkleExecutionChannelHandler;
    public  SparkleChannelInitializer(NettyHttpServerConfig config){
        this(config, null);
    }

    public  SparkleChannelInitializer(NettyHttpServerConfig config, EventExecutorGroup group){
        this.config = config;
        this.group = group;
        this.sparkleExecutionChannelHandler = new SparkleExecutionChannelHandler();
    }

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

        if (config.isStaticServeEnabled()){
            pipeline.addLast("staticResourceHandler", new StaticResourceServeHandler(true, config.staticResourceRouteMatcher()));
        }

        /*
         * 1. since SparkleExecutionHandler is thread safe, can be marked as @Sharable,
         * we don't need new it for every channel's pipeline
         *
         * 2. if this.group is null, the actionHandler will bind to eventLoop thread
         *
         */
        pipeline.addLast(this.group, "actionHandler", this.sparkleExecutionChannelHandler);
//        if (this.group != null){
//            pipeline.addLast(this.group, "actionHandler", this.sparkleExecutionHandler);
//        } else {
//            pipeline.addLast("actionHandler", this.sparkleExecutionHandler);
//        }

    }
}