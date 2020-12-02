package com.soundai.elevator.cms.filter;

import com.soundai.elevator.cms.entity.ThreadLocalBean;
import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.ThreadRepertory;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Log4j2
public class Interceptor implements HandlerInterceptor {


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
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN,token");
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (request.getRequestURI().matches("/token/logout")) {
            return true;
        }
        if (request.getRequestURI().matches("/token/token")) {
            return true;
        }
        String accessToken = request.getHeader("token");
        log.info("accessToken {}", accessToken);
        ThreadRepertory.setParm(new ThreadLocalBean(accessToken));
        if (accessToken == null) {
            response.getWriter().println(tokenRes());
            return false;
        }
        User user = (User) CachePool.getInstance().getCacheItem(accessToken);
        log.info("拦截器查询的user:{}", user);
        if (user == null) {
            response.getWriter().println(userRes());
            return false;
        }
        if(user.getLevel()==0){
            response.getWriter().println(userLevel() );
            return false;
        }
        return true;
    }

    public String userRes() {
        String msg = "{\n" +
                "  \"code\": 9996,\n" +
                "  \"msg\": \"未查询到该用户，请重新登陆\",\n" +
                "  \"data\": null\n" +
                "}";
        return msg;
    }

    public String userLevel() {
        String msg = "{\n" +
                "  \"code\": 9995,\n" +
                "  \"msg\": \"当前用户为游客，请联系管理员\",\n" +
                "  \"data\": null\n" +
                "}";
        return msg;
    }

    public String tokenRes() {
        String msg = "{\n" +
                "  \"code\": 9997,\n" +
                "  \"msg\": \"token为空，请登陆\",\n" +
                "  \"data\": null\n" +
                "}";
        return msg;
    }
}
