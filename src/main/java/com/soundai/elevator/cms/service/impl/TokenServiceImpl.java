package com.soundai.elevator.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ProException;
import com.soundai.elevator.cms.common.ResultEnum;
import com.soundai.elevator.cms.mapper.UserMapper;
import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.service.TokenService;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.util.SSLSocketClient;
import com.soundai.elevator.cms.util.Signature;
import com.soundai.elevator.cms.vo.AccessToken;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 常磊
 * @date 4.16
 * @describe 根据code换取token；
 */
@Log4j2
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private UserMapper userDao;

    private final OkHttpClient okHttpClient;

    @Value("${token.base-url}")
    private String baseUrl;
    @Value("${token.client-id}")
    private String client_id;
    @Value("${token.logout-url}")
    private String logout_url;
    @Value("${token.redirect-url}")
    private String redirect_url;
    @Value("${token.user-info-url}")
    private String userInfoUrl;
    @Value("${azero.appId}")
    private String appId;
    @Value("${azero.secretKey}")
    private String secretKey;
    @Value("${azero.association}")
    private String association;

    public TokenServiceImpl() {
        this.okHttpClient = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())// 配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();
    }

    /**
     * 常磊 4.17
     * 获取token并异步步处理新用户
     *
     * @param code
     * @return
     */
    @Override
    public AccessToken getAccessToken(String code) throws BusinessException {
        String url = baseUrl + "?client_id=" + client_id + "&code=" + code + "&logout_url=" + logout_url;
        String tokenMsg = this.getRequest(url);
        JSONObject tokenMsgJson = JSON.parseObject(tokenMsg);
        String accessToken = tokenMsgJson.getString("accessToken");
        if (accessToken == null) {
            throw new ProException(Integer.parseInt(tokenMsgJson.getString("status")), tokenMsgJson.getString("message"));
        }
        User user = getUserMessage(accessToken);
        cacheToken(user, tokenMsgJson);
        AccessToken accessTokenBody = new AccessToken();
        accessTokenBody.setName(user.getName());
        accessTokenBody.setUserId(user.getId());
        accessTokenBody.setAccessToken(accessToken);
        accessTokenBody.setPhone(user.getNumber());
        return accessTokenBody;
    }

    /**
     * 退出
     *
     * @param response
     * @param token
     */
    @Override
    public void logout(HttpServletResponse response, String token) {
        CachePool.getInstance().removeCacheItem(token);
        //sso注销
        try {
            response.sendRedirect(redirect_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 放入缓存池
     *
     * @param user
     * @param tokenMsgJson
     */
    public void cacheToken(User user, JSONObject tokenMsgJson) {
        try {
            String accessToken = tokenMsgJson.getString("accessToken");
            Long expireIn = Long.valueOf(tokenMsgJson.getString("expireIn"));
            CachePool.getInstance().putCacheItem(accessToken, user, expireIn * 1000);
            log.info("cacheToken {}", CachePool.getInstance().getCacheItem(accessToken));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProException(998, "缓存失败请重试");
        }
    }

    /**
     * 常磊 4.17
     * 请求注册中心
     *
     * @param url
     * @return
     */
    public String getRequest(String url) {
        try {
            RequestBody body = new FormBody.Builder().build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String res = response.body().string();
            log.info(res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 常磊 4.17
     * 请求注册中心
     *
     * @param accountId
     * @return
     */
    public String getRequestPost(String accountId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", accountId);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                    , jsonObject.toJSONString());
            Request request = new Request.Builder()
                    .header("Authorization", "SaiApp " + appId + ":" + Signature.signatureStr(appId, secretKey))
                    .url(association)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String res = response.body().string();
            JSONObject resJson = JSON.parseObject(res);
            log.info("getRequestPost resJson{}", resJson);
            String userId = null;
            if (resJson.getString("code").equals("200")) {
                String dataJson = resJson.getString("data");
                userId = JSON.parseObject(dataJson).getString("userId");
            }
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 常磊 4.17
     * 获取用户信息并验证新用户
     *
     * @param accessToken
     */
    public User getUserMessage(String accessToken) {
        User user;
        try {
            String userMsgUrl = userInfoUrl + "?client_id=" + client_id + "&access_token=" + accessToken;
            String userInfo = getRequest(userMsgUrl);
            user = this.userDao.getUserByAccountIAandNumber(JSON.parseObject(userInfo, Map.class));
            log.info("getUserMessage user{}", user);
            if (user == null) {
                user = new User();
                JSONObject userInfoJson = JSON.parseObject(userInfo);
                String accountId = userInfoJson.getString("accountId");
                String number = userInfoJson.getString("number");
                String userId = getRequestPost(accountId);
                if (userId == null) {
                    throw new BusinessException(ResultEnum.GET_USER_ID);
                }
                user.setUserId(userId);
                user.setAccountId(accountId);
                user.setIsDel(0);
                user.setLevel(0);
                user.setNumber(number);
                user.setName("未添加");
                user.setCompanyName("未添加");
                this.userDao.insertUser(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProException(997, "getUserMessage err");
        }
        return user;
    }

}
