package com;

import java.io.*;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
  
import io.netty.handler.codec.*;
import io.netty.handler.codec.string.*;

public final class Client {
    static final String HOST = "127.0.0.1";
    static final int PORT = 8000;

    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }
  
    public static void main(String[] args) throws Exception {
        disableWarning();

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(1234, Delimiters.lineDelimiter()));
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast("handler", new ClientHandler());

                        }                
                    });

            ChannelFuture f = b.connect(HOST, PORT).sync();
            Channel channel = f.channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = in.readLine();
                channel.writeAndFlush(line + "\r\n");
            }
            
        } finally {
            group.shutdownGracefully();
        }
    }
}
