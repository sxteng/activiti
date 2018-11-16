package com.sxteng.ubpm.task.taskexcutor;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AcquireTaskCmd implements Command<List<Task>> {

    private final DudateTaskExecutor taskExecutor;

    public AcquireTaskCmd(DudateTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public  List<Task> execute(CommandContext commandContext) {
        int maxTasksPerAcquisition = taskExecutor.getMaxTasksPerAcquisition();
        int lockTimeInMillis = taskExecutor.getLockTimeInMillis();
        ProcessEngineConfiguration processEngineConfig = Context.getProcessEngineConfiguration();
        Date now = processEngineConfig.getClock().getCurrentTime();
        TaskQueryImpl taskQuery = new TaskQueryImpl();
        taskQuery.setFirstResult(0);
        taskQuery.setMaxResults(maxTasksPerAcquisition);
        taskQuery.dueBefore(now);
        List<Task> tasks = commandContext.getTaskEntityManager().findTasksByQueryCriteria(taskQuery);
        lockTasks(commandContext,tasks, lockTimeInMillis);
        return tasks;
    }

    /**
     * 查询一次逾期时间延长五分钟
     * 避免多次查询同一结果,处理多次
     * @param commandContext
     * @param tasks
     * @param lockTimeInMillis
     */
    protected void lockTasks(CommandContext commandContext, List<Task> tasks, int lockTimeInMillis) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(commandContext.getProcessEngineConfiguration().getClock().getCurrentTime());
        gregorianCalendar.add(Calendar.MILLISECOND, lockTimeInMillis);
        for (Task task : tasks) {
             task.setDueDate(gregorianCalendar.getTime());
        }
    }
}
