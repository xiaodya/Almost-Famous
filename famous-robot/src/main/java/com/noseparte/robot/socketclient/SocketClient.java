package com.noseparte.robot.socketclient;

import com.noseparte.common.battle.server.Decoder;
import com.noseparte.common.battle.server.Encoder;
import com.noseparte.common.battle.server.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class SocketClient {
	// 日志
	private Logger log = LogManager.getLogger(SocketClient.class);

	// worker负责读写数据
	protected EventLoopGroup workerGroup = null;

	// 负责写回
	protected Channel channel = null;

	private String ip = null;

	private int port = 0;
	
	protected Map<Object,BiConsumer<Object, Protocol>> listeners = new HashMap<Object,BiConsumer<Object,Protocol>>();

	public String getIp() {
		return ip;
	}
	
	/**
	 * 注册一个接受服务器返回消息的监听
	 * @param object 要接受监听消息的对象
	 * @param consumer 处理消息的BiConsumer
	 */
	public void registListener(Object object,BiConsumer<Object, Protocol> consumer) {
		this.listeners.put(object,consumer);
	}
	/**
	 * 移除消息监听
	 */
	public void removeLisener(Object object) {
		this.listeners.remove(object);
	}

	public int getPort() {
		return port;
	}

	public SocketClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public boolean start() throws Exception {
		return this.start(false);

	}

	public boolean start(boolean isTest) throws Exception {
		Bootstrap bootstrap = new Bootstrap();

		this.workerGroup = new NioEventLoopGroup();

		// 设置线程池
		bootstrap.group(this.workerGroup);

		// 设置socket工厂
		bootstrap.channel(NioSocketChannel.class);////

		// 设置管道
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				// 通信管道
				ChannelPipeline pipeline = socketChannel.pipeline();

				pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));

				// 消息编码器
				pipeline.addLast(new Encoder());
				// 消息解码器
				pipeline.addLast(new Decoder());
				// 消息分发器
				if (isTest) {
					SocketTestClientHandler clientHandler = new SocketTestClientHandler();
					clientHandler.listeners = listeners;
					pipeline.addLast(new ChannelHandler[] {clientHandler});
				} else {
					pipeline.addLast(new SocketClientHandler());
				}
			}
		});

		// 发起异步连接操作
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(this.ip, this.port)).sync();

		if (future.isSuccess()) {
			this.channel = future.channel();
			log.info("Netty socket client to [ip:{}, port:{}]", this.ip, port);
			return true;
		} else {
			return false;
		}
	}

	public void stop() {
		this.workerGroup.shutdownGracefully().syncUninterruptibly();
		log.info("Netty socket closed");
	}

	public void reconnect() throws Exception {
		// 先断开
		this.stop();
		// 再连接
		this.start();
	}

	public void sendMsg(Protocol msg) {
		if (this.channel == null || !this.channel.isActive()) {
			try {
				this.reconnect();
			} catch (Exception e) {
				log.error("Failed to do reconnect!");
			}
		}
		if (this.channel != null && this.channel.isActive()) {
			this.channel.writeAndFlush(msg);
		}
	}
}
