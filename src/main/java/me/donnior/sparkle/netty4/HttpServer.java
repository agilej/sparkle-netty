package me.donnior.sparkle.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {
    
    private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private NettyHttpServerConfig config;
    
    private final int port;

    public HttpServer(int port) {
        this(port, new DefaultNettyHttpServerConfig());
    }

    public HttpServer(int port, NettyHttpServerConfig config){
        this.port = port;
        this.config = config;
    }

    public void setConfig(NettyHttpServerConfig config){
        this.config = config;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new SparkleChannelInitializer(this.config))
             .childOption(ChannelOption.TCP_NODELAY, true);

            logger.info("Server started and listening at "+ port +" ...\n");
            b.bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    
    
    public static void main(String[] args) throws Exception{
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new HttpServer(port).run();

    }
}


