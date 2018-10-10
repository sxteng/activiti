package com.sxteng.activit.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

import java.io.Serializable;
import java.util.List;

public class WithDrawCmd  implements Command<String>, Serializable {
//    /**
//     * 弃审实现
//     * @param commandContext
//     * @return
//     */
//       public String execute(CommandContext commandContext) {
//        /*
//         * 如果是下个环节任务已经产生但是没有完成,这个时候弃审
//         * 删除所有的
//         */
//        String processInstanceId = "105001";
//        ExecutionEntity processExe = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
//        List<ExecutionEntity> childExe =  processExe.getExecutions();
//        for (int i = 0; i < childExe.size(); i++) {
//            ExecutionEntity executionEntity = childExe.get(i);
//            executionEntity.setActivity(null);
//            executionEntity.deleteCascade("deleted");
//            System.out.println("=============");
//            System.out.println("=============");
//            System.out.println("=============");
//            System.out.println("=============");
//        }
//
//
//
//
//        return null;
//    }

    /**
     * 清空流程实例的结束状态
     * @param processInstanceId
     */
    protected void cleanProcessInstanceEnd(String processInstanceId) {
        HistoricProcessInstanceEntity historicProcessInstance = Context.getCommandContext().getHistoricProcessInstanceEntityManager()
                .findHistoricProcessInstance(processInstanceId);
        historicProcessInstance.setEndTime(null);
        historicProcessInstance.setEndActivityId(null);
        historicProcessInstance.setDeleteReason(null);
    }

    public String execute(CommandContext commandContext) {
        return null;
    }


//    public String execute(CommandContext commandContext) {
//        String historyTaskId = "85002";
//
//        HistoricTaskInstanceEntity histask = commandContext.getHistoricTaskInstanceEntityManager()
//                .findHistoricTaskInstanceById(historyTaskId);
//        ExecutionEntity concurrentExecution =  commandContext.getExecutionEntityManager().findExecutionById(histask.getExecutionId());
//        concurrentExecution.setActive(true);
//        concurrentExecution.setConcurrent(true);
//        concurrentExecution.setScope(false);
//        concurrentExecution.executeActivity(concurrentExecution.getActivity());
//
//        return null;
//
//    }
}
