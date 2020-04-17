package com.soundai.elevator.cms.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Interceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(Interceptor.class);

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        response.setContentType("text/html;charset=UTF-8");
        log.info("------------------>:已完成跨域处理");
        log.info("------------------>:"+request.getRequestURI());
        log.info("------------------>:"+getIpAddress(request));
        String accessToken  = request.getHeader("token");
        log.info("accessToken {}",accessToken);
        /*if(accessToken==null){
            throw new BusinessException(ResultEnum.NO_TOKEN);
        }
        CachePool.getInstance().getSize();
        User user = (User) CachePool.getInstance().getCacheItem(accessToken);
        log.info("拦截器查询的user:{}",user);
        if (user == null) {
            throw new BusinessException(ResultEnum.NO_LOGIN);
        }*/
        return true;
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        return ip;
    }

}
