package com.soundai.elevator.cms.mapper;


import com.soundai.elevator.cms.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
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


    User getUserByAccountIAandNumber(Map params);

}
