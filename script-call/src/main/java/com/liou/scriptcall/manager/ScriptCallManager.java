package com.liou.scriptcall.manager;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.service.ServiceLocator;
import com.command.base.window.Application;
import com.command.base.window.Destroyable;
import com.liou.scriptcall.command.ScriptCallCommand;
import com.liou.scriptcall.info.InfoSyncService;
import com.liou.scriptcall.info.MessagePipeline;
import com.liou.scriptcall.task.ScriptTask;

public class ScriptCallManager implements Destroyable {

    private static ScriptCallManager instance;

    private ScriptCallManager() {
    }

    public synchronized static ScriptCallManager getInstance() {
        if (null == instance) {
            instance = new ScriptCallManager();
            instance.executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
            ServiceLocator.instanceOf(InfoSyncService.class);
            Application.registerDestroy(instance);
        }
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(ScriptCallManager.class);

    private ThreadPoolExecutor executor;

    public void execCommand(ScriptCallCommand command, MessagePipeline messagePipeline, String user) {
        logger.debug("exec build:" + command.getCommandLine());
        ScriptTask task = new ScriptTask(command, user);
        task.setMessagePipeline(messagePipeline);
        executor.execute(task);
    }

    public boolean hasRemain() {
        return executor.getActiveCount() > 0;
    }

    public void destroy() {
        List<Runnable> awaitings = executor.shutdownNow();
        if (awaitings.size() > 0) {
            logger.warn("appliction exited with %d tasks are awaiting to run", awaitings.size());
        }
    }

}
