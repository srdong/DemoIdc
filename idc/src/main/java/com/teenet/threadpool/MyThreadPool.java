package com.teenet.threadpool;

import java.util.concurrent.*;


public class MyThreadPool {

    public static final LinkedBlockingDeque queue = new LinkedBlockingDeque(20);
    public static final MyThreadFactory factory = new MyThreadFactory("custom-处理消息");
    /**
     * 拒绝策略 清除以前的线程
     */
    public static ThreadPoolExecutor EXECUTOR_SERVICE =
            new ThreadPoolExecutor(3, 10, 1L, TimeUnit.MINUTES, queue, factory, new MyRejectedExecutionHandler());

}
