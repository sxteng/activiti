package com.sxteng.ubpm.task.taskexcutor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class TestDemo {

    @Test
    public void testDemo (){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("activiti.cfg.xml");
        DudateTaskExecutor executor = (DudateTaskExecutor) context.getBean("dudateTaskExecutor");
        System.out.println(executor.getCommandExecutor());
        System.out.println(executor.getProcessEngineConfiguration());
        try {
            Thread.sleep(1000 * 60 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void removeList (){
        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        while (!list.isEmpty()) {
            list.remove(0);
            System.out.println("size+++"+list.size());
        }


    }

}
