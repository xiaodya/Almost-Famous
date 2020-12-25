package com.noseparte.robot.socketclient;

import com.noseparte.common.battle.server.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocketClientHandler extends SimpleChannelInboundHandler<Protocol> {
	// 日志
	private Logger log = LogManager.getLogger(SocketClientHandler.class);

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
		log.debug(msg.toString());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.WRITER_IDLE)) {// 如果出发了read_idle，主动发送一次心跳
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getMessage(), cause);
		ctx.close();
	}

}
