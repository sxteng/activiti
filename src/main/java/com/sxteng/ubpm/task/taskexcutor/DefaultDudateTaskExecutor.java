package com.sxteng.ubpm.task.taskexcutor;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

public class DefaultDudateTaskExecutor extends DudateTaskExecutor {

    private static Logger log = LoggerFactory.getLogger(DefaultDudateTaskExecutor.class);
    protected int queueSize = 3;
    protected int corePoolSize = 3;
    protected int maxPoolSize = 10;
    protected long keepAliveTime = 0L;

    protected BlockingQueue<Runnable> threadPoolQueue;
    protected ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void init() {
        processEngineConfiguration.buildProcessEngine();
        setCommandExecutor(processEngineConfiguration.getCommandExecutor());
        start();
    }

    protected void stopExecutingTasks() {
        stopTaskAcquisitionThread();

        // Ask the thread pool to finish and exit
        threadPoolExecutor.shutdown();

        // Waits for 1 minute to finish all currently executing jobs
        try {
            if(!threadPoolExecutor.awaitTermination(60L, TimeUnit.SECONDS)) {
                log.warn("Timeout during shutdown of task executor. "
                        + "The current running taks could not end within 60 seconds after shutdown operation.");
            }
        } catch (InterruptedException e) {
            log.warn("Interrupted while shutting down the job executor. ", e);
        }

        threadPoolExecutor = null;
    }

    protected void startExecutingTask() {
        if (threadPoolQueue==null) {
            threadPoolQueue = new ArrayBlockingQueue<Runnable>(queueSize);
        }
        if (threadPoolExecutor==null) {
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, threadPoolQueue);
            threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        }
        startTaskAcquisitionThread();
    }

    public void executeTasks(List<Task> tasks) {
        try {
            threadPoolExecutor.execute(new ExecuteTasksRunnable(tasks,this));
        } catch (RejectedExecutionException e){
           log.error("线程执行失败, tasks [{}]", tasks, e);
        }
    }




}
