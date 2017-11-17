package com.command.server.worker;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.command.Command;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.base.service.CommandService;
import com.command.base.service.ServiceLocator;
import com.command.base.service.StoreService;
import com.command.base.window.Application;
import com.command.base.window.Worker;

public class ServerWorker implements Worker {
    private static Logger logger = LoggerFactory.getLogger(Worker.class);

    public static final String COMMAND_EXIT = "exit";

    public static final String COMMAND_USE = "use";

    public static final String COMMAND_LIST = "list";

    private String[] serverCommands = new String[] { COMMAND_EXIT, COMMAND_USE, COMMAND_LIST, "y", "n", "Y", "N" };

    @Override
    public void execute(Command command) throws Exception {
        if (isServerCommand(command)) {
            CommandService commandService = ServiceLocator.getService(CommandService.class);
            Handler handler = commandService.allocateHandler(command);
            HandleContext context = new HandleContext();
            context.model = command;
            if (handler == null || !handler.handle(context)) {
                logger.warn(String.format("unhandle command:%s", command.getCommandLine()));
            }
        } else {
            StoreService storeService = ServiceLocator.getService(StoreService.class);
            IoSession session = storeService.getValue("currentSession", IoSession.class);
            if (session == null || !session.isActive()) {
                Application.console.println("no session selected or session closed");
            } else {
                session.write(command.getCommandLine());
            }
        }
    }

    public boolean isServerCommand(Command command) {
        for (String sign : serverCommands) {
            if (command.is(sign)) {
                return true;
            }
        }
        return false;
    }

}
