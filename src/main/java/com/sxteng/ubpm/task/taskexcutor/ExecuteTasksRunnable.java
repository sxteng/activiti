package com.sxteng.ubpm.task.taskexcutor;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExecuteTasksRunnable implements  Runnable {

    private static Logger log = LoggerFactory.getLogger(ExecuteTasksRunnable.class);

    private List<Task> tasks;
    private DudateTaskExecutor taskExecutor;

    public ExecuteTasksRunnable(List<Task> tasks, DudateTaskExecutor taskExecutor) {
        this.tasks = tasks;
        this.taskExecutor = taskExecutor;
    }

    public void run() {
        if (tasks != null) {
            handleMultipleTask();
        }

    }

    private void handleMultipleTask() {
      while (!tasks.isEmpty()) {
          Task task =  tasks.remove(0);
          try {
              taskExecutor.getCommandExecutor().execute(new ExecuteDudateTaskCmd(task));
          } catch (Exception e) {
             log.error("执行逾期任务 task [{}] 异常 ,异常信息 {}", task, e.getMessage(), e);
          }
      }
    }
}
