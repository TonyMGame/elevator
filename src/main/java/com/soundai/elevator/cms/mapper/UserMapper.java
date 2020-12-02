package com.soundai.elevator.cms.mapper;


import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.vo.ChangeValueVo;
import com.soundai.elevator.cms.vo.UserPageVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper {

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User getUserById(int id);

    /**
     * 新增用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 根据params查询用户
     * @param params
     * @return
     */
    User getUserByAccountIAandNumber(Map params);

    /**
     * 查询用户列表
     * @param userVo
     * @return
     */
    List<User> getUserList(UserPageVo userVo);

    /**
     * 删除用户
     * @param id
     */
    void delUser(int id);

    /**
     * 赋予控制权限
     * @param changeValueVo
     */
    void upLevel(ChangeValueVo changeValueVo);

    /**
     * 赋予控制权限
     * @param changeValueVo
     */
    void updateCompanyName(ChangeValueVo changeValueVo);

    /**
     * 赋予控制权限
     * @param changeValueVo
     */
    void updateName(ChangeValueVo changeValueVo);


}
