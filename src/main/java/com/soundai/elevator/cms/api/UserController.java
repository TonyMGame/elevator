package com.soundai.elevator.cms.api;

import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.service.UserService;
import com.soundai.elevator.cms.vo.ChangeValueVo;
import com.soundai.elevator.cms.vo.UserPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Object list(@RequestBody UserPageVo userVo) throws BusinessException {
        return userService.getUserList(userVo);
    }

    @ApiOperation(value = "删除用户")
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    public void list(@RequestParam int id) throws BusinessException {
        this.userService.delUser(id);
    }

    @ApiOperation(value = "赋予权限")
    @RequestMapping(value = "/up_level", method = RequestMethod.POST)
    public void upLevel(@RequestBody ChangeValueVo changeValueVo) throws BusinessException {
        this.userService.upLevel(changeValueVo);
    }

    @ApiOperation(value = "添加公司名")
    @RequestMapping(value = "/update_company_name", method = RequestMethod.POST)
    public void updateCompanyName(@RequestBody ChangeValueVo changeValueVo) throws BusinessException {
        this.userService.updateCompanyName(changeValueVo);
    }

    @ApiOperation(value = "添加名字")
    @RequestMapping(value = "/update_name", method = RequestMethod.POST)
    public void updateName(@RequestBody ChangeValueVo changeValueVo) throws BusinessException {
        this.userService.updateName(changeValueVo);
    }



}
