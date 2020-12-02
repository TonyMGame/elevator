package com.soundai.elevator.cms.util;

public class Signature {

    public  static String signatureStr(String appId,String key){
        return UrlBase64.getBASE64(MD5Code.encodeByMD5(appId+key).toLowerCase());
    }

}
