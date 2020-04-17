package com.soundai.elevator.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ProException;
import com.soundai.elevator.cms.mapper.UserMapper;
import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.service.UserService;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.LocalCache;
import com.soundai.elevator.cms.util.SSLSocketClient;
import com.soundai.elevator.cms.vo.AccessToken;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author 常磊
 * @date 4.16
 * @describe  根据code换取token；
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userdao;

    private final OkHttpClient okHttpClient;

    @Value("${token.base-url}")
    private String baseUrl;
    @Value("${token.client-id}")
    private String client_id;
    @Value("${token.logout-url}")
    private String logout_url;
    @Value("${token.user-info-url}")
    private String userInfoUrl;

    public UserServiceImpl() {
        this.okHttpClient = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())// 配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();
    }

    /**
     * 常磊 4.17
     * 获取token并异步步处理新用户
     * @param code
     * @return
     */
    @Override
    public AccessToken getAccessToken(String code) throws BusinessException {
            String url = baseUrl+"?client_id="+client_id+"&code="+code+"&logout_url="+logout_url;
            String tokenMsg = this.getResqust(url);
            JSONObject tokenMsgJson = JSON.parseObject(tokenMsg);
            String accessToken = tokenMsgJson.getString("accessToken");
            if(accessToken==null){
                throw  new ProException(Integer.valueOf(tokenMsgJson.getString("status")),tokenMsgJson.getString("message"));
            }
            User user = getUserMessage(accessToken);
            cacheToken(user,tokenMsgJson);
            AccessToken accessTokenBody = new AccessToken();
            accessTokenBody.setAccessToken(accessToken);
            return accessTokenBody;
    }

    public void cacheToken(User user,JSONObject tokenMsgJson){
        try {
            String accessToken = tokenMsgJson.getString("accessToken");
            Long expireIn =  Long.valueOf(tokenMsgJson.getString("expireIn"));
            CachePool.getInstance().putCacheItem(accessToken,user,expireIn*1000);
            log.info("cacheToken {}",CachePool.getInstance().getCacheItem(accessToken));
        }catch (Exception e){
            e.printStackTrace();
            throw new ProException(998,"缓存失败请重试");
        }
    }

    /**
     * 常磊 4.17
     * 请求注册中心
     * @param url
     * @return
     */
    public  String  getResqust(String url){
        try {
            RequestBody body = new FormBody.Builder().build();
            Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
            Response response = okHttpClient.newCall(request).execute();
            String res = response.body().string();
            log.info(res);
            return  res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 常磊 4.17
     * 获取用户信息并验证新用户
     * @param accessToken
     */
    public User getUserMessage(String accessToken){
        User user;
        try {
            String userMsgUrl = userInfoUrl+"?client_id="+client_id+"&access_token="+accessToken;
            String userInfo =getResqust(userMsgUrl);
            user  =this.userdao.getUserByAccountIAandNumber(JSON.parseObject(userInfo, Map.class));
            log.info("getUserMessage user{}",user);
            if(user==null){
                user = new User();
                JSONObject userInfoJson = JSON.parseObject(userInfo);
                String accountId = userInfoJson.getString("accountId");
                String number = userInfoJson.getString("number");
                user.setAccountId(accountId);
                user.setIsDel(0);
                user.setLevel(0);
                user.setNumber(number);
                this.userdao.insertUser(user);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ProException(997,"getUserMessage err");
        }
        log.info("getUserMessage userRes{}",user.toString());
        return user;
    }

}
