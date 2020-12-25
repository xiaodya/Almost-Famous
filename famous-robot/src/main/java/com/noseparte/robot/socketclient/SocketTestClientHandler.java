package com.noseparte.robot.socketclient;

import LockstepProto.C2SHeartBeat;
import LockstepProto.NetMessage;
import com.noseparte.common.battle.server.CHeartBeat;
import com.noseparte.common.battle.server.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class SocketTestClientHandler extends SimpleChannelInboundHandler<Protocol> {
	protected Map<Object,BiConsumer<Object, Protocol>> listeners;
	// 日志
	private Logger log = LogManager.getLogger(SocketTestClientHandler.class);

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
		if(msg == null) {
			return;
		}
		// 获取通信通道
		Channel channel = ctx.channel();
		if(listeners!=null) {
			for(Entry<Object, BiConsumer<Object, Protocol>> entry:listeners.entrySet()) {
				entry.getValue().accept(entry.getKey(), msg);
			}
		}

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.WRITER_IDLE)) {// 如果出发了read_idle，主动发送一次心跳
				// 获取通信通道
				Channel channel = ctx.channel();
				byte[] resmsg = C2SHeartBeat.newBuilder().setSeq(99).build().toByteArray();
                Protocol p = new CHeartBeat();
                p.setType(NetMessage.C2S_HeartBeat_VALUE);
                p.setMsg(resmsg);
                channel.writeAndFlush(p);
                if (log.isDebugEnabled()) {
					log.debug(p.toString());
                }
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getMessage(), cause);
		ctx.close();
	}

}
