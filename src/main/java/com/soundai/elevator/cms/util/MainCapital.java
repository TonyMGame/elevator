package com.soundai.elevator.cms.util;

import com.soundai.elevator.cms.entity.User;
import com.soundai.elevator.cms.task.CachePool;
import com.soundai.elevator.cms.task.ThreadRepertory;

public class MainCapital {

    public static User getAZeroMsg() {
        String key = ThreadRepertory.getParm().getToken();
        User user = (User) CachePool.getInstance().getCacheItem(ThreadRepertory.getParm().getToken());
        return user;
    }
}
