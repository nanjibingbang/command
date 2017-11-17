package com.liou.scriptcall.task;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.service.ServiceLocator;
import com.liou.scriptcall.command.ScriptCallCommand;
import com.liou.scriptcall.info.InfoSyncService;
import com.liou.scriptcall.info.InfoSyncable;
import com.liou.scriptcall.info.MessagePipeline;

public class ScriptTask implements Runnable, InfoSyncable {

    private static Logger logger = LoggerFactory.getLogger(ScriptTask.class);

    private Process process;

    private ScriptCallCommand command;
    private MessagePipeline messagePipeline;

    private String user;

    public ScriptTask(ScriptCallCommand command, String user) {
        this.command = command;
        this.user = user;
    }

    @Override
    public void run() {
        int exitValue = -1;
        try {
            logger.debug("begin:" + command.getCommandLine());
            process = Runtime.getRuntime().exec(command.toSystemCall());
            InfoSyncService infoSyncService = ServiceLocator.getService(InfoSyncService.class);
            infoSyncService.syncInfo(this);
            exitValue = process.exitValue();
        } catch (Exception e) {
            logger.error("{}:执行系统调用出错！", Thread.currentThread().getName(), e);
        }
        command.record(user, exitValue);
        logger.debug(command.getCommandLine() + " end");
    }

    @Override
    public int currentWait() {
        try {
            return process.waitFor();
        } catch (InterruptedException e) {
            logger.error("{}:线程等待出错！", Thread.currentThread().getName(), e);
        }
        return 1;
    }

    @Override
    public InputStream getInputStream() {
        return process.getInputStream();
    }

    @Override
    public InputStream getErrorStream() {
        return process.getErrorStream();
    }

    @Override
    public MessagePipeline getMessagePipeline() {
        return messagePipeline;
    }

    @Override
    public void setMessagePipeline(MessagePipeline messagePipeline) {
        this.messagePipeline = messagePipeline;
    }

    @Override
    public String syncName() {
        return command.getCommandLine();
    }

}
