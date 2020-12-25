package com.noseparte.robot.socketclient;

import LockstepProto.C2SMatch;
import LockstepProto.NetMessage;
import com.noseparte.common.battle.server.Protocol;
import com.noseparte.match.match.CMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 每个client对象相当于一个客户端，包含了登录过程和登录之后发消息
 */
class Client {
	private Logger log = LogManager.getLogger(this.getClass());
	//客户端的socket实例
	private SocketClient socketClient = null;
	//用户的uid
	private long uid;
	private long st;
	private long et;
	Client(){
	}
	/**
	 * 启动客户端，包括http登录和建立socket连接，后续的逻辑请自己酌情加入
	 */
	public void start() {
		try {
			long loginSt = System.currentTimeMillis();
			st = loginSt;
			if(!this.login()) {
				return;
			}
			long loginEt = System.currentTimeMillis();
			log.debug("login的http请求花费时长-------->"+(loginEt - loginSt));
			socketConnect();
			log.debug("socketConnect花费时长-------->"+(System.currentTimeMillis() - loginEt));
//			st = System.currentTimeMillis();
		} catch (Exception e) {
			log.error("connect to server error!",e,e.getMessage(),e.getStackTrace());
			e.printStackTrace();
		}
	}
	
	private boolean login() throws Exception {
		Map<String, Object> getParam = new HashMap<>();
		return true;
	}
	
	private void socketConnect() throws Exception {
		socketClient = new SocketClient("localhost", 9872);
		socketClient.registListener(this, consumer);
		boolean success = socketClient.start(true);
		if (!success) {
			return;
		}
		byte[] resmsg = C2SMatch.newBuilder().setUserId(69111044030599168L).setRoleId(69111181259837440L)
                .setToken("33313209179750712:Y0D6NjkxMTEwNDQwMzA1OTkxNjg=0ID").build().toByteArray();
        Protocol p = new CMatch();
        p.setType(NetMessage.C2S_Match_VALUE);
        p.setMsg(resmsg);

		socketClient.sendMsg(p);
	}
	//TODO 登录之后的测试逻辑放在这里面
	private void startLogical() {
		//这里是登录并建立连接完成，项目组可以在这里根据实际情况加入自身的测试逻辑
		//下面是示例代码：每10毫秒发送一个心跳消息
//		ScheduleUtils.getInstance().runEveryPeriod(new RRunnable("attackCity") {
//			@Override
//			public void execute() throws Exception {
//				CG_HeartBeat testMsg = new CG_HeartBeat();
//				socketClient.sendMsg(testMsg);
//			}
//		}, 1000);
	}
	
	private BiConsumer<Object, Protocol> consumer = new BiConsumer<Object, Protocol>() {
		@Override
		public void accept(Object t, Protocol msg) {
			Client self = (Client)t;
			msg.run();
			//TODO 这里是收到服务器返回的消息的地方
			log.debug("Receive msg BiConsumer:"+msg.getClass());

		}
	};
}
