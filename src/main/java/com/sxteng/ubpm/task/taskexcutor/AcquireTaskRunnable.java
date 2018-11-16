package com.sxteng.ubpm.task.taskexcutor;

import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AcquireTaskRunnable implements Runnable{
    private static Logger log = LoggerFactory.getLogger(AcquireTaskRunnable.class);

    private final DudateTaskExecutor taskExecutor;
    protected volatile boolean isInterrupted = false;
    protected volatile boolean isJobAdded = false;
    protected final Object MONITOR = new Object();
    protected final AtomicBoolean isWaiting = new AtomicBoolean(false);

    protected long millisToWait = 0;

    public AcquireTaskRunnable(DudateTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public synchronized void run() {
        final CommandExecutor commandExecutor = taskExecutor.getCommandExecutor();

        log.debug(" executor [{}] 启动获取逾期任务线程", taskExecutor.getName());
        while (!isInterrupted) {
            int maxTasksPerAcquisition = taskExecutor.getMaxTasksPerAcquisition();
            try {
                List<Task> tasks = commandExecutor.execute(taskExecutor.getAcquireTaskCmd());

                taskExecutor.executeTasks(tasks);

                millisToWait = taskExecutor.getWaitTimeInMillis();
                if (tasks.size() >= maxTasksPerAcquisition) {
                    millisToWait = 0;
                }
            } catch (ActivitiOptimisticLockingException optimisticLockingException) {
                // See https://activiti.atlassian.net/browse/ACT-1390
                if (log.isDebugEnabled()) {
                    log.debug("查询数据库异常 {}", optimisticLockingException.getMessage());
                }
            } catch (Throwable e) {
                log.error("exception during task acquisition: {}", e.getMessage(), e);
                millisToWait = taskExecutor.getWaitTimeInMillis();
            }


            if (millisToWait > 0) {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("task acquisition thread sleeping for {} millis", millisToWait);
                    }
                    synchronized (MONITOR) {
                        if (!isInterrupted) {
                            isWaiting.set(true);
                            MONITOR.wait(millisToWait);
                        }
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("task acquisition thread woke up");
                    }
                } catch (InterruptedException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("task acquisition wait interrupted");
                    }
                } finally {
                    isWaiting.set(false);
                }
            }
        }

        log.info("{} stopped task acquisition", taskExecutor.getName());
    }

    public void stop() {
        synchronized (MONITOR) {
            isInterrupted = true;
            if(isWaiting.compareAndSet(true, false)) {
                MONITOR.notifyAll();
            }
        }
    }
}
