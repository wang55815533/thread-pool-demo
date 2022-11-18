package com.semi;

import java.util.concurrent.*;

public class ThreadPool {
    private static volatile ExecutorService mainThreadPool = null;
    private static volatile ExecutorService subThreadPool = null;
    private static volatile ThreadPoolExecutor threadPoolExecutor = null;

    private ThreadPool() {
    }

    private static String lock() {
        return "threadPool";
    }

    public static ExecutorService getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            synchronized(lock()) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(10, 100, 5, TimeUnit.SECONDS,
                            new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }

        return threadPoolExecutor;
    }

    public static void printPoolInfo() {
        System.out.println("PoolSize: "+ threadPoolExecutor.getPoolSize() + " ,TaskCount: " + threadPoolExecutor.getTaskCount() + " ,QueueSize: " +threadPoolExecutor.getQueue().size());
    }

    public static void shutdownPool() {
        System.out.println("--Start Shudown Thread Pool! PoolSize: "+ threadPoolExecutor.getPoolSize() + " ,TaskCount: " + threadPoolExecutor.getTaskCount() + " ,QueueSize: " +threadPoolExecutor.getQueue().size());
        threadPoolExecutor.shutdown();
    }
}
