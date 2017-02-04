package com.aequalis.concurrent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class WaitAndAddTaskLaterPolicy implements RejectedExecutionHandler {


    @Override
    public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
//            LOGGER.error("unable to add task {} to the queue", r, e);
        }
    }

}