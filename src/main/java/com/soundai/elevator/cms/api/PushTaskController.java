package com.soundai.elevator.cms.api;


import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.service.DirectivePush;
import com.soundai.elevator.cms.service.PushTaskService;
import com.soundai.elevator.cms.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "投放管理")
@RestController
@RequestMapping("/push_task")
@CrossOrigin
public class PushTaskController {

    @Resource
    private PushTaskService pushTaskService;

    @ApiOperation(value = "投放任务列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(@RequestBody PushTaskPageVo pushTaskPageVo) throws BusinessException {
        return this.pushTaskService.getPushTaskList(pushTaskPageVo);
    }


    @ApiOperation(value = "历史任务列表")
    @RequestMapping(value = "/history_list", method = RequestMethod.POST)
    public Object historyList(@RequestBody PushTaskPageVo pushTaskPageVo) throws BusinessException {
        return this.pushTaskService.getHistoryPushTaskList(pushTaskPageVo);
    }


    @ApiOperation(value = "添加任务")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(@RequestBody PushTaskVo pushTaskVo) throws BusinessException {
        this.pushTaskService.add(pushTaskVo);
    }

    @ApiOperation(value = "编辑任务")
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public void edit(@RequestBody PushTaskVo pushTaskVo) throws BusinessException {
        this.pushTaskService.edit(pushTaskVo);
    }

    @ApiOperation(value = "默认任务列表")
    @RequestMapping(value = "/default_list", method = RequestMethod.POST)
    public Object defaultList(@RequestBody PushTaskPageVo pushTaskPageVo) throws BusinessException {
        return this.pushTaskService.getPushTaskDefaultList(pushTaskPageVo);
    }

    @ApiOperation(value = "添加默认任务")
    @RequestMapping(value = "/add_default", method = RequestMethod.PUT)
    public void addDefault(@RequestBody PushDefaultTaskVo pushDefaultTaskVo) throws BusinessException {
        this.pushTaskService.addDefault(pushDefaultTaskVo);
    }

    @ApiOperation(value = "编辑默认任务")
    @RequestMapping(value = "/edit_default", method = RequestMethod.PUT)
    public void editDefault(@RequestBody PushDefaultTaskVo pushDefaultTaskVo) throws BusinessException {
        this.pushTaskService.editDefault(pushDefaultTaskVo);
    }

    @ApiOperation(value = "取消任务")
    @RequestMapping(value = "/change_status", method = RequestMethod.POST)
    public void changeStatus(@RequestBody DeletePushTask deletePushTask) throws BusinessException {
        this.pushTaskService.deleteStatus(deletePushTask);
    }


}
