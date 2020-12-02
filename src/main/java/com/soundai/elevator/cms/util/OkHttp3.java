package com.soundai.elevator.cms.util;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
public class OkHttp3 {

    private final OkHttpClient okHttpClient;

    OkHttp3(){
        this.okHttpClient = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())// 配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();
    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}
