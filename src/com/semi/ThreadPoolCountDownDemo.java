package com.semi;

import java.util.concurrent.CountDownLatch;

public class ThreadPoolCountDownDemo {

    public static void main(String[] args) throws Exception {

        int taskCount = 50;
        //记录单个任务的执行次数
        CountDownLatch countDownLatch = new CountDownLatch(taskCount);

        for(int i= 0; i< taskCount; i++){
            ThreadPool.getThreadPoolExecutor().execute(new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));

            // 任务个数 - 1, 直至为0时唤醒await()
            countDownLatch.countDown();
        }

        try {
            // 让当前线程处于阻塞状态，直到锁存器计数为零
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new NullPointerException(e.getMessage());
        }

    }

}
