package com.teenet.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;
    static final String nameSuffix = "]";

    public MyThreadFactory(String poolName) {
        SecurityManager securityManager = System.getSecurityManager();
        group = (null != securityManager) ? securityManager.getThreadGroup() : new ThreadGroup("自定义线程池----");
        namePrefix = "自定义线程" + poolName + " 池 [这个线程-";
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(group, r, namePrefix + threadNumber.getAndIncrement() + nameSuffix, 0);
    }
}