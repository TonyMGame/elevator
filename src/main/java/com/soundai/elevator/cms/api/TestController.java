package com.soundai.elevator.cms.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(value="测试类")
@RestController
@RequestMapping("/test")
public class TestController {

    @ApiOperation(value = "get")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public Object login(@RequestParam String machineId){
        Map map = new HashMap();
        map.put("machineId",machineId);
        return  map;
    }


}
