package com.command.base.window.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.command.Command;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.base.service.CommandService;
import com.command.base.service.ServiceLocator;
import com.command.base.window.Worker;

public class DefaultWorker implements Worker {
    private static Logger logger = LoggerFactory.getLogger(Worker.class);

    @Override
    public void execute(Command command) throws Exception {
        CommandService commandService = ServiceLocator.getService(CommandService.class);
        Handler handler = commandService.allocateHandler(command);
        HandleContext context = new HandleContext();
        context.model = command;
        if (handler == null || !handler.handle(context)) {
            logger.warn(String.format("unhandle command:%s", command.getCommandLine()));
        }
    }

}
