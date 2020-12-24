package com;

import io.netty.channel.*;
import io.netty.channel.ChannelHandlerContext;
  

public class ClientHandler extends SimpleChannelInboundHandler<String>{
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s){
        System.out.println("Result = " + s);
    }
}
