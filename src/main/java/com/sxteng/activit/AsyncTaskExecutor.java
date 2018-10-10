package com.sxteng.activit;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

public interface AsyncTaskExecutor {

    void start();

    void shutdown();

    boolean executeAsyncTask(TaskEntity taskEntity);
}
