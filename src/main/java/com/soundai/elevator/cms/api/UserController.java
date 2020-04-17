package com.soundai.elevator.cms.api;


import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value="用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "get")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public void login(@RequestParam String machineId){
        System.out.println(machineId);
    }

    @ApiOperation(value = "获取token")
    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public Object token(@RequestParam String code) throws BusinessException {
        return userService.getAccessToken(code);
    }

}
