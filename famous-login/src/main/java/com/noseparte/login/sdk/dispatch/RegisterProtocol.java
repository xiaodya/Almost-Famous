package com.noseparte.login.sdk.dispatch;

import com.noseparte.common.bean.Action;
import com.noseparte.common.utils.SpringContextUtils;
import com.noseparte.login.sdk.internal.controller.ActorLoginAction;
import com.noseparte.login.sdk.internal.controller.ActorRegisterAction;
import com.noseparte.login.sdk.internal.controller.RefreshTokenAction;
import com.noseparte.login.sdk.internal.controller.VerifyUserLoginAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Noseparte
 * @date 2019/8/12 16:24
 * @Description
 */
public class RegisterProtocol {

    ////////// 玩家注册登录
    public final static int ACTOR_LOGIN_ACTION_REQ = 301;
    public final static int ACTOR_LOGIN_ACTION_RESP = 302;

    public final static int ACTOR_REGISTER_ACTION_REQ = 303;
    public final static int ACTOR_REGISTER_ACTION_RESP = 304;

    public static final int REFRESH_TOKEN_ACTION_REQ = 331;
    public static final int REFRESH_TOKEN_ACTION_RESP = 332;

    public static final int VERIFY_USER_LOGIN_REQ = 20001;
    public static final int VERIFY_USER_LOGIN_RESP = 20002;


    public static final Map<Integer, Action> REGISTER_PROTOCOL_MAP = new HashMap<Integer, Action>() {
        {
            //角色
            put(ACTOR_LOGIN_ACTION_REQ, SpringContextUtils.getBean("actorLoginAction", ActorLoginAction.class));
            put(ACTOR_REGISTER_ACTION_REQ, SpringContextUtils.getBean("actorRegisterAction", ActorRegisterAction.class));
            put(REFRESH_TOKEN_ACTION_REQ, SpringContextUtils.getBean("refreshTokenAction", RefreshTokenAction.class));
            put(VERIFY_USER_LOGIN_REQ, SpringContextUtils.getBean("verifyUserLoginAction", VerifyUserLoginAction.class));

        }
    };

}
