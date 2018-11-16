package com.sxteng.activit;

import com.sxteng.activit.cmd.WithDrawCmd;
import org.activiti.engine.*;
import org.activiti.engine.impl.RuntimeServiceImpl;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo {


    @Test
    //parallel
    public  void deploy() {

        ProcessEngineConfiguration processEngineConfiguration =  ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml");

        // Create Activiti process engine
        ProcessEngine processEngine =processEngineConfiguration.buildProcessEngine();

        // Get Activiti services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // Deploy the process definition
        repositoryService.createDeployment()
                .addClasspathResource("dbmp/timer.bpmn")
                .deploy();




        // Start a process instance
 /*       runtimeService.startProcessInstanceByKey("process222");
        try {
            Thread.sleep(1000 * 6 * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }



    @Test
    public  void await() {

        ProcessEngineConfiguration processEngineConfiguration =  ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml");

        // Create Activiti process engine
        ProcessEngine processEngine =processEngineConfiguration.buildProcessEngine();

        // Get Activiti services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

/*
        // Deploy the process definition
        repositoryService.createDeployment()
                .addClasspathResource("dbmp/process.bpmn")
                .deploy();
*/




        // Start a process instance
      // runtimeService.startProcessInstanceByKey("process222");
        try {
            Thread.sleep(1000 * 6 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public  void startProcessInstance() {

        ProcessEngineConfiguration processEngineConfiguration =  ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml");

        // Create Activiti process engine
        ProcessEngine processEngine =processEngineConfiguration.buildProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> assigneeList = new ArrayList<String>();
        assigneeList.add("sxteng08");
        map.put("assigneeList", assigneeList);
        runtimeService.startProcessInstanceByKey("timer", map);

        try {
            Thread.sleep(1000 * 60 *10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Test
    public  void compelete() {

        ProcessEngineConfiguration processEngineConfiguration =  ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml");

        // Create Activiti process engine
        ProcessEngine processEngine =processEngineConfiguration.buildProcessEngine();

        // Get Activiti services
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        processEngine.getTaskService().complete("80030");
        processEngine.getTaskService().complete("80037");
        processEngine.getTaskService().complete("90002");




        // Deploy the process definition
/*        repositoryService.createDeployment()
                .addClasspathResource("dbmp/createTimersProcess.bpmn20.xml")
                .deploy();*/

        // Start a process instance
/*        Map<String, Object> var = new HashMap<String ,Object>();
        var.put("duration", "short");
        runtimeService.startProcessInstanceByKey("process",var);
        try {
            Thread.sleep(1000 * 6 * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }



    @Test
    public  void withDraw() {

        ProcessEngineConfiguration configuration =  ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml");

        // Create Activiti process engine
        ProcessEngine processEngine =configuration.buildProcessEngine();

        RuntimeServiceImpl runtimeService = (RuntimeServiceImpl)processEngine.getRuntimeService();

        runtimeService.getCommandExecutor().execute( new WithDrawCmd());
       // runtimeService.getCommandExecutor().execute();

      /*  runtimeService.*/
        //processEngine.getRuntimeService().deleteProcessInstance(, );



  /*      ExecutionEntity execution = null;
        execution.deleteCascade("");*/



    }


    @Test
    public  void deleteProcessInstance() {

        ProcessEngineConfiguration processEngineConfiguration =  ProcessEngineConfiguration.createProcessEngineConfigurationFromResource( "activiti.cfg.xml");

        // Create Activiti process engine
        ProcessEngine processEngine =processEngineConfiguration.buildProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskEntity taskEntity =  Context.getCommandContext().getTaskEntityManager().findTaskById("");
        taskEntity.getVariables();

        runtimeService.deleteProcessInstance("60001", "deleted");


    }

}
