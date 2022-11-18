package com.semi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Kevin Wang
 * @description
 * @date 2022/11/16 14:50
 */
public class MultiTaskDemo {

    private static volatile ExecutorService subThreadPool = null;

    public static void main(String[] args) {

        for (int j = 0; j < 15; j++) {
            Thread thread = new Thread(() -> {
                CompletableFuture future;
                List<CompletableFuture> cfList = new ArrayList<>();
                List list = new ArrayList();
                System.out.println("@" + System.identityHashCode(list));

                for (int i = 0; i < 600; i++) {
                    // 1.创建一个CompletableFuture对象
                    int finalI = i;
                    future = CompletableFuture.supplyAsync(() -> {
                        //System.out.println("hi " + Thread.currentThread() + System.nanoTime());
                        return finalI;
                    }, ThreadPool.getThreadPoolExecutor());

                    // 2.添加回调函数
                    future.whenComplete((result, action) -> {
                        synchronized (list) {
                            list.add(result);
                            try {
                                //Thread.sleep(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //System.out.println(result.toString() + Thread.currentThread() + System.nanoTime() + ":" + Thread.currentThread().getId()+"@"+System.identityHashCode(list));
                        }
                    });

                    cfList.add(future);
                }

                CompletableFuture.allOf(cfList.toArray(new CompletableFuture[cfList.size()])).join();

                System.out.println("Task finish! " + Thread.currentThread() + System.nanoTime() + "@" + System.identityHashCode(list));
                System.out.println("size=" + list.size() + Thread.currentThread() + "@" + System.nanoTime() + ":" + System.identityHashCode(list));
                for (Object o : list) {
                    //System.out.println((int) o + 1);
                }

                ThreadPool.shutdownPool();
            });
            thread.start();
        }
    }

    public static ExecutorService getSubThreadPool() {
        if (subThreadPool == null) {
            synchronized("lock") {
                if (subThreadPool == null) {
                    subThreadPool = Executors.newFixedThreadPool(25);
                }
            }
        }
        return subThreadPool;
    }
}
