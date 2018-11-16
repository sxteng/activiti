package com.sxteng.ubpm.task.taskexcutor.handler;

import cn.hutool.core.util.ClassUtil;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TaskHandlerFactory {

    private static Logger log = LoggerFactory.getLogger(TaskHandlerFactory.class);

    public static final String scanBackage = "com.sxteng.ubpm.task.taskexcutor.handler";
    public static Map<String,DuedateTaskHandler > handlers = new HashMap<String,DuedateTaskHandler>();

    static {
        Set<Class<?>> clazzSet = ClassUtil.scanPackageBySuper(scanBackage, DuedateTaskHandler.class);
        for (Class<?> clazz : clazzSet) {
            try {
                DuedateTaskHandler handler = (DuedateTaskHandler)ConstructorUtils.invokeConstructor(clazz,null);
                if (!handlers.containsKey(handler.getType())) {
                    handlers.put(handler.getType(), handler);
                }
            } catch (Exception e) {
               log.error("创建 DuedateTaskHandler class [{}] 出错",clazz.getName());
            }
        }
    }



    public static DuedateTaskHandler getTaskHanler(String type) {
        DuedateTaskHandler handler =   handlers.get(type);
        if (handler == null) {
            return null;
        }

        return handler;
    }
}
