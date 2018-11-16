package com.sxteng.ubpm.task.taskexcutor.handler;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;

public interface DuedateTaskHandler {
    String getType();
    void execute(Task task, ExecutionEntity execution, CommandContext commandContext);
}
