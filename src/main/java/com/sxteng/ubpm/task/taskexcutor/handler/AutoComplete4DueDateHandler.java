package com.sxteng.ubpm.task.taskexcutor.handler;

import com.sxteng.ubpm.task.taskexcutor.AcquireTaskRunnable;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

public class AutoComplete4DueDateHandler implements  DuedateTaskHandler {

    private static Logger log = LoggerFactory.getLogger(AutoComplete4DueDateHandler.class);

    public final static String TYPE = "autocomplete";
    public String getType() {
        return TYPE;
    }

    public void execute(Task task, ExecutionEntity execution, CommandContext commandContext) {
        try {
            commandContext.getProcessEngineConfiguration().getTaskService().complete(task.getId());
        } catch (Exception e) {
            log.error("逾期任务自动审批出错 task [{}]", task, e);
            throw new RuntimeException("逾期任务自动审批出错");
        }
    }
}
