package com.noseparte.login.sdk.internal.controller;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.bean.Action;
import com.noseparte.common.exception.ErrorCode;
import com.noseparte.common.global.Resoult;
import com.noseparte.login.sdk.dispatch.RegisterProtocol;
import com.noseparte.login.sdk.internal.entity.Account;
import com.noseparte.login.sdk.internal.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Author: xiaodya
 * @Date: 2020/12/25 18:23
 */
@Component
public class VerifyUserLoginAction extends Action {
    @Autowired
    private AccountService accountService;

    @Override
    public Resoult execute(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        Account account = jsonObject.toJavaObject(Account.class);
        String clientIp = request.getRemoteAddr();
        account = accountService.login(account, clientIp);

        if (Objects.nonNull(account)) {
            return Resoult.ok(RegisterProtocol.VERIFY_USER_LOGIN_RESP).responseBody(account);
        }
        return Resoult.error(RegisterProtocol.VERIFY_USER_LOGIN_RESP, ErrorCode.ACCOUNT_NOT_EXIST, "");
    }
}
