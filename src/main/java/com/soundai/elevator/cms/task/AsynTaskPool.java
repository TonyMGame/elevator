package com.soundai.elevator.cms.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * @author 常磊
 * @Date 4.17
 */
public class AsynTaskPool {

    static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private static class LazyHolder {
        final static AsynTaskPool INSTANCE = new AsynTaskPool();
    }

    public static final AsynTaskPool getInstance() {
        return AsynTaskPool.LazyHolder.INSTANCE;
    }

    public void task(Runnable runnable) {
        cachedThreadPool.execute(runnable);
    }

    public void closeTask() {
        cachedThreadPool.shutdownNow(); //关闭线程池 不可用
    }
}
