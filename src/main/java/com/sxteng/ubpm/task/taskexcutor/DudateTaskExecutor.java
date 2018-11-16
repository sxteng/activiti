package com.sxteng.ubpm.task.taskexcutor;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.jobexecutor.DefaultJobExecutor;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 逾期任务执行类
 */
public abstract class DudateTaskExecutor {

    private static Logger log = LoggerFactory.getLogger(DudateTaskExecutor.class);

    protected String name = "TaskExecutor["+getClass().getName()+"]";
    /**每次获取最多逾期任务数*/
    protected int maxTasksPerAcquisition = 10;

    protected long waitTimeInMillis = 5000L;

    protected int lockTimeInMillis = 5 * 60 * 1000;

    protected  boolean taskExecutorActivate = false;
    protected  boolean isActive = false;
    protected CommandExecutor commandExecutor;
    @Autowired
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    protected Command<List<Task>> acquireTaskCmd; //获取逾期task cmd

    protected AcquireTaskRunnable acquireTaskRunnable;
    /**
     * 获取逾期任务启动线程
     */
    protected Thread taskAcquisitionThread;

    /**
     * 初始化CommandExecutor
     */
    @PostConstruct
    abstract void init();

    /***
     * 启动 taskExecutor
     */
    public void start() {
        if (!taskExecutorActivate) {
            log.debug("逾期taskExecutor [{}]没有配置启动",  getClass().getName());
        }
        if(isActive) {
            return ;
        }
        log.debug("启动 taskExecutor [{}]",  getClass().getName());
        ensureInitialization();
        startExecutingTask();
    }


    /** Shuts down the whole job executor */
    public synchronized void shutdown() {
        if (!isActive) {
            return;
        }
        log.info("Shutting down the JobExecutor[{}].", getClass().getName());
        acquireTaskRunnable.stop();
        stopExecutingTasks();
        ensureCleanup();
        isActive = false;
    }

    protected abstract void stopExecutingTasks();


    /**启动*/
    protected abstract void startExecutingTask();

    /**
     * 初始化 acquireTaskCmd  acquireTaskRunnable
     */
    protected void ensureInitialization() {
        if (acquireTaskCmd == null) {
            acquireTaskCmd = new AcquireTaskCmd(this);
        }
        if (acquireTaskRunnable == null) {
            acquireTaskRunnable = new AcquireTaskRunnable(this);
        }
    }



    protected  void startTaskAcquisitionThread() {
        if (taskAcquisitionThread== null) {
            taskAcquisitionThread = new Thread(acquireTaskRunnable);
        }
        taskAcquisitionThread.start();
    }


    /** Stops the acquisition thread */
    protected void stopTaskAcquisitionThread() {
        try {
            taskAcquisitionThread.join();
        } catch (InterruptedException e) {
            log.warn("Interrupted while waiting for the Task Acquisition thread to terminate", e);
        }
        taskAcquisitionThread = null;
    }








    /** Possibility to clean up resources */
    protected void ensureCleanup() {
        acquireTaskCmd = null;
        acquireTaskRunnable = null;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public boolean isTaskExecutorActivate() {
        return taskExecutorActivate;
    }

    public void setTaskExecutorActivate(boolean taskExecutorActivate) {
        this.taskExecutorActivate = taskExecutorActivate;
    }

    public String getName() {
        return name;
    }

    public int getMaxTasksPerAcquisition() {
        return maxTasksPerAcquisition;
    }

    public void setMaxTasksPerAcquisition(int maxTasksPerAcquisition) {
        this.maxTasksPerAcquisition = maxTasksPerAcquisition;
    }

    public Command<List<Task>> getAcquireTaskCmd() {
        return acquireTaskCmd;
    }

    public void setAcquireTaskCmd(Command<List<Task>> acquireTaskCmd) {
        this.acquireTaskCmd = acquireTaskCmd;
    }

    public long getWaitTimeInMillis() {
        return waitTimeInMillis;
    }

    public void setWaitTimeInMillis(long waitTimeInMillis) {
        this.waitTimeInMillis = waitTimeInMillis;
    }

    public int getLockTimeInMillis() {
        return lockTimeInMillis;
    }

    public void setLockTimeInMillis(int lockTimeInMillis) {
        this.lockTimeInMillis = lockTimeInMillis;
    }

    public abstract void executeTasks(List<Task> tasks);
}
