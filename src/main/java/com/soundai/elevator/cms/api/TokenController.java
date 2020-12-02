package com.soundai.elevator.cms.api;


import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(tags = "登陆token")
@RestController
@RequestMapping("/token")
@CrossOrigin
public class TokenController {

    @Resource
    private TokenService userService;

    @ApiOperation(value = "获取token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public Object token(@RequestParam String code) throws BusinessException {
        return userService.getAccessToken(code);
    }

    @ApiOperation(value = "推出销毁token")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletResponse response,@RequestParam String token) throws BusinessException {
        userService.logout(response,token);
    }

}
