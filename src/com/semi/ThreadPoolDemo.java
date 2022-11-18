package com.semi;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ThreadPoolDemo {
    private static volatile int completedWorkerCount = 0;

    public static void main(String[] args) {
        int taskCount = 50;
        ArrayList<CompletableFuture> cfTaskList = new ArrayList();

        for (int i = 0; i < taskCount; i++) {
            int taskNo = i + 1;
            // 1.创建一个CompletableFuture对象
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //加锁 Task完成时计数
                synchronized ("lock") {
                    completedWorkerCount++;
                }
                return new StringBuilder().append("taskNo: ").append(taskNo).append(" worker: ").append(completedWorkerCount).append(" thread: ").append(Thread.currentThread()).append(" time: ").append(System.currentTimeMillis()).toString();
            }, ThreadPool.getThreadPoolExecutor());

            ThreadPool.printPoolInfo();

            // 2.添加回调函数
            future.whenComplete((t, u) -> {
                // 2.1如果没有异常，打印异步任务结果
                if (null == u) {
                    System.out.println(t);
                } else {
                    // 2.2打印异常信息
                    System.out.println(u.getLocalizedMessage());
                }
                //System.out.flush();
            });
            cfTaskList.add(future);
        }

        CompletableFuture.allOf(cfTaskList.toArray(new CompletableFuture[cfTaskList.size()])).join();

        //System.out.flush();
        System.out.println("--All worker finished! Thread: " + Thread.currentThread());
        //System.exit(0);

        ThreadPool.shutdownPool();
    }

}
