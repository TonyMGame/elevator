package com.soundai.elevator.cms.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.soundai.elevator.cms.common.BusinessException;
import com.soundai.elevator.cms.common.ResultEnum;
import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.mapper.UserMapper;
import com.soundai.elevator.cms.service.UserService;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.ThreadRepertory;
import com.soundai.elevator.cms.vo.ChangeValueVo;
import com.soundai.elevator.cms.vo.PageList;
import com.soundai.elevator.cms.vo.UserPageVo;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户列表
     *
     * @param userVo
     * @return
     * @throws BusinessException
     */
    @Override
    public PageList getUserList(UserPageVo userVo) throws BusinessException {
        try {
            User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
            Page page = PageHelper.startPage(userVo.getPageNum(), userVo.getPageSize());
            if (user.getLevel() != 2) {
                userVo.setUserId(user.getId());
            }
            List<User> materialList = this.userMapper.getUserList(userVo);
            return new PageList(userVo.getPageNum(),
                    userVo.getPageSize(),
                    page.getTotal(), page.getPages(),
                    materialList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.QUERY_DATA_ERROR);
        }

    }

    /**
     * 删除用户
     *
     * @param id
     * @throws BusinessException
     */
    @Override
    public void delUser(int id) throws BusinessException {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        if (user.getLevel() != 2) {
            throw new BusinessException(ResultEnum.DEL_POWER_ERROR);
        }
        if (id == user.getId()) {
            throw new BusinessException(ResultEnum.DEL_POWER_ERROR);
        }
        CachePool.getInstance().removeById(id);
        this.userMapper.delUser(id);
    }

    /**
     * 更改用户level级别  控制sms操作权限
     *
     * @param changeValueVo
     * @throws BusinessException
     */
    @Override
    public void upLevel(ChangeValueVo changeValueVo) throws BusinessException {
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        if (user.getLevel() != 2) {
            throw new BusinessException(ResultEnum.POWER_ERROR);
        }
        if (changeValueVo.getId() ==user.getId()) {
            throw new BusinessException(ResultEnum.POWER_ERROR);
        }
        CachePool.getInstance().changeLevelById(changeValueVo.getId(), Integer.parseInt(String.valueOf(changeValueVo.getItem())));
        this.userMapper.upLevel(changeValueVo);
    }

    @Override
    public void updateCompanyName(ChangeValueVo changeValueVo) throws BusinessException {
        this.userMapper.updateCompanyName(changeValueVo);
    }

    @Override
    public void updateName(ChangeValueVo changeValueVo) throws BusinessException {
        this.userMapper.updateName(changeValueVo);
    }
}
