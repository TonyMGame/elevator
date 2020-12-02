package com.soundai.elevator.cms.api;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.vo.PullTaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags = "任务上报")
@RestController
@RequestMapping("/pull_task")
@CrossOrigin
public class PullTaskController {

    @ApiOperation(value = "上报任务")
    @RequestMapping(value = "/data_reporting", method = RequestMethod.POST)
    public void pullMassage(@RequestBody PullTaskVo pullTaskVo) throws BusinessException {

    }



}
