package com.soundai.elevator.cms.task;

import com.soundai.elevator.cms.entity.ThreadLocalBean;

import java.util.Map;

public class ThreadRepertory {

    public static ThreadLocal<ThreadLocalBean> holder = new ThreadLocal<ThreadLocalBean>();

    public static void removeParm(){
        holder.remove();
    }

    public static ThreadLocalBean getParm(){
        return holder.get();
    }

    public static void setParm(ThreadLocalBean treadLocalBean){
        holder.set(treadLocalBean);
    }

}
