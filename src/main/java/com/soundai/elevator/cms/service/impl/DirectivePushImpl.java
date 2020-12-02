package com.soundai.elevator.cms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ResultEnum;
import com.soundai.elevator.cms.service.DirectivePush;
import com.soundai.elevator.cms.util.OkHttp3;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 常磊
 * @date 4.21
 * @describe 任务推送
 */
@Log4j2
@Service
public class DirectivePushImpl implements DirectivePush {


    @Value("${massage.push-url}")
    private String pushUrl;
    @Value("${massage.type}")
    private String type;
    @Value("${massage.namespace}")
    private String namespace;
    @Value("${massage.name}")
    private String name;
    @Value("${massage.message-type}")
    private String messageType;


    @Resource
    private OkHttp3 okHttp3;

    /**
     * 向Azero推送消息
     *
     * @param deviceId
     * @param sources
     * @throws BusinessException
     */
    @Override
    public void pushMassage(String deviceId, List<Map<String, Object>> sources) throws BusinessException {
        try {
            String json = handleStr(deviceId, sources);
            RequestBody requestBody = FormBody.create(MediaType.parse("application.properties/json; charset=utf-8")
                    , json);
            Request request = new Request.Builder()
                    .url(pushUrl)
                    .post(requestBody)
                    .build();
            Response response = okHttp3.getOkHttpClient().newCall(request).execute();
            String res = response.body().string();
            log.info("DirectivePushImpl pushMassage deviceId{} response{}",deviceId, res);
        } catch (Exception e) {
            log.info("pushMassage  err");
            e.printStackTrace();
            throw new BusinessException(ResultEnum.UPLOAD_ERROR);
        }

    }

    /***
     * 组装消息
     * @param deviceId
     * @param sources
     * @return
     */
    public String handleStr(String deviceId, List<Map<String, Object>> sources) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("deviceId", deviceId);
        JSONObject directive = new JSONObject();
        directive.put("namespace", namespace);
        directive.put("name", name);
        try {
            JSONObject payload = new JSONObject();
            payload.put("sources", sources);
            payload.put("type", messageType);
            directive.put("payload", payload);
            jsonObject.put("directive", directive);
            String jsonStr = jsonObject.toJSONString();
            log.info("DirectivePushImpl deviceId {} 下发任务数据 {}",deviceId,jsonStr);
            return jsonStr;
        }catch (Exception e){
            e.printStackTrace();
            log.info("DirectivePushImpl deviceId {} 任务数据异常",deviceId);
        }
        return null;
    }


}

