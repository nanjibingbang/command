package com.command.server.handler;

import com.command.base.command.Command;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.server.worker.ServerWorker;

public class ExitHandler implements Handler {

    @Override
    public boolean adapt(Object object) {
        if (object instanceof Command) {
            return ((Command) object).is(ServerWorker.COMMAND_EXIT);
        }
        return false;
    }

    @Override
    public boolean handle(HandleContext context) {
        Runtime.getRuntime().exit(0);
        return true;
    }

}
