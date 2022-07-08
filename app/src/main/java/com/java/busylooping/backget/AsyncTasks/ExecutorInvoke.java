package com.java.busylooping.backget.AsyncTasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorInvoke {
    private ExecutorService executorService;
    private ExecutorInvokeTask task;

    public ExecutorInvoke(ExecutorInvokeTask task) {
        this.task = task;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static class ExecutorInvokeTask implements Runnable {
        @Override
        public void run() {
        }
    }

    public void execute() {

    }

}
