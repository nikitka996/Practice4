package com;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.*;

public class ServerHandler extends SimpleChannelInboundHandler<String>{

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("New client has connected");
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public static String response(String request) {
        String firstNum = "";
        String secondNum = "";
        char sign = '+';
        String operations = "+-*/";

        try {
            for (int i = 0; i < request.length(); i++) {
                if (operations.indexOf(request.charAt(i)) == -1) {
                    if (request.charAt(i) != ' ')
                        firstNum += request.charAt(i);
                }
                else {
                    sign = request.charAt(i);
                    for (int j  = i+1; j < request.length(); j++) {
                        if (request.charAt(j) != ' ')
                            secondNum += request.charAt(j);
                    }
                    break;
                }
            }

            int a = Integer.parseInt(firstNum);
            int b = Integer.parseInt(secondNum);
            int res = 0;

            switch(sign){
                case '+':
                    res = a + b;
                    break;
                case '-':
                    res = a - b;
                    break;
                case '*':
                    res = a * b;
                    break;
                case '/':
                    res = a / b;
                    break;
            }

            return Integer.toString(res);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return "Wrong expression. Use two numbers and one sign!";
        }
    }

    @Override
    protected void messageReceived(ChannelHandlerContext chc, String s){
        s = s.trim();
        chc.writeAndFlush(response(s) + "\r\n");
    }
}
