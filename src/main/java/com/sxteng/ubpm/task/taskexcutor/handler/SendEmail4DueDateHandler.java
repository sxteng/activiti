package com.sxteng.ubpm.task.taskexcutor.handler;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmail4DueDateHandler implements  DuedateTaskHandler{
    private static Logger log = LoggerFactory.getLogger(SendEmail4DueDateHandler.class);

    public final static String TYPE = "email";
    public String getType() {
        return TYPE;
    }

    public void execute(Task task, ExecutionEntity execution, CommandContext commandContext) {
        System.out.println("email");
    }
}
