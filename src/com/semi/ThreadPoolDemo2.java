package com.semi;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class ThreadPoolDemo2 {

    public static void main(String[] args) throws Exception {

        int taskCount = 50;
        //记录单个任务的执行次数
        CountDownLatch countDownLatch = new CountDownLatch(taskCount);

        for(int i= 0; i< taskCount; i++){
            // 1.创建一个CompletableFuture对象
            CompletableFuture<String> future = CompletableFuture.supplyAsync(()-> {
                //System.out.println("test: " + Thread.currentThread());
                return "test: " + Thread.currentThread();
            }, ThreadPool.getThreadPoolExecutor());

            // 2.添加回调函数
            future.whenComplete((t, u) -> {
                countDownLatch.countDown();
                // 2.1如果没有异常，打印异步任务结果
                if (null == u) {
                    System.out.println(t);
                } else {
                    // 2.2打印异常信息
                    System.out.println(u.getLocalizedMessage());
                }
            });
        }

        try {
            // 让当前线程处于阻塞状态，直到锁存器计数为零
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new NullPointerException(e.getMessage());
        }


    }

}
