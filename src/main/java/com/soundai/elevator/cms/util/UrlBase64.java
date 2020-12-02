package com.soundai.elevator.cms.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class UrlBase64 {


    public static String safeUrlBase64Encode(byte[] data){
        String encodeBase64 = new BASE64Encoder().encode(data);
        String safeBase64Str = encodeBase64.replace('+', '-');
        safeBase64Str = safeBase64Str.replace('/', '_');
        safeBase64Str = safeBase64Str.replaceAll("=", "");
        return safeBase64Str;
    }
    public static byte[] safeUrlBase64Decode(final String safeBase64Str){
        try {
        String base64Str = safeBase64Str.replace('-', '+');
        base64Str = base64Str.replace('_', '/');
        int mod4 = base64Str.length()%4;
        if(mod4 > 0){
            base64Str = base64Str + "====".substring(mod4);
        }
        return new BASE64Decoder().decodeBuffer(base64Str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBASE64(String s) {
        if (s == null) {
            return null;
        }
        return (new BASE64Encoder()).encode(s.getBytes());
    }




    public static void main(String[] args) {
       String appID = "6cd32413fc6e79b7";
       String key = "207b9a7dd3643ba91e377b27366a9460";

        String sss =MD5Code.encodeByMD5(appID+key).toLowerCase();
        System.out.println(sss);
        System.out.println(getBASE64(sss));
    }


}
