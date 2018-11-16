package com.sxteng.ubpm.task.taskexcutor.handler;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 默认情况下 下次不再扫描
 */
public class Default4DuedateTakHandler implements DuedateTaskHandler{

    private static Logger log = LoggerFactory.getLogger(Default4DuedateTakHandler.class);

    public final static String TYPE = "default";
    public String getType() {
        return TYPE;
    }

    public void execute(Task task, ExecutionEntity execution, CommandContext commandContext) {
        task = commandContext.getTaskEntityManager().findTaskById(task.getId());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(commandContext.getProcessEngineConfiguration().getClock().getCurrentTime());
        gregorianCalendar.add(Calendar.YEAR, 100);
        task.setDueDate(gregorianCalendar.getTime());

    }
}
