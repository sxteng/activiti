package com.sxteng.ubpm.task.taskexcutor;

import com.sxteng.ubpm.task.taskexcutor.handler.DuedateTaskHandler;
import com.sxteng.ubpm.task.taskexcutor.handler.TaskHandlerFactory;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteDudateTaskCmd implements Command<Void> {

    private static Logger log = LoggerFactory.getLogger(ExecuteDudateTaskCmd.class);

    protected Task task;

    public ExecuteDudateTaskCmd(Task task) {
        this.task = task;
    }

    public Void execute(CommandContext commandContext) {
        if (task == null || task.getExecutionId() == null) {
            throw new ActivitiException("task or executionId is not null");
        }
        ExecutionEntity  execution = commandContext.getExecutionEntityManager().findExecutionById(task.getExecutionId());
        ActivityImpl activity = execution.getProcessDefinition().findActivity(task.getTaskDefinitionKey());
        String dueDatePolicy  = (String)activity.getProperty("dudate");
        DuedateTaskHandler taskHandler = TaskHandlerFactory.getTaskHanler("autocomplete");
        taskHandler.execute(task, execution,commandContext );
        return null;
    }
}
