package com.noseparte.robot.socketclient;

import LockstepProto.NetMessage;
import com.noseparte.common.battle.server.CHeartBeat;
import com.noseparte.common.battle.server.RegistryProtocol;
import com.noseparte.common.battle.server.SHeartBeat;
import com.noseparte.match.match.*;

public class PressureTest {
    //要启动的客户端的数量
    private static int clientNum = 1;

    private static void init() throws Exception {
        //TODO init env
        RegistryProtocol.protocolMap.put(NetMessage.C2S_HeartBeat_VALUE, CHeartBeat.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_HeartBeat_VALUE, SHeartBeat.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_Match_VALUE, CMatch.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_Match_VALUE, SMatch.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_Reconnect_VALUE, CReconnect.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_Reconnect_VALUE, SReconnect.class);
        RegistryProtocol.protocolMap.put(NetMessage.C2S_MatchCancel_VALUE, CMatchCancel.class);
        RegistryProtocol.protocolMap.put(NetMessage.S2C_MatchCancel_VALUE, SMatchCancel.class);
    }

    public static void main(String[] args) throws Exception {
        init();
        for (int i = 1; i <= clientNum; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Client client = new Client();
                    client.start();
                }
            });
            //TODO 线程的名字就是登录时的用户名，要测试不同客户端的时候记得把这里改掉，不然会互相踢下线
            t.setName("xiaodya00" + i);
            t.start();
        }
    }
}
