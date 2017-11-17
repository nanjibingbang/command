package com.command.server.handler;

import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;

import com.command.base.command.Command;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.base.service.ServiceLocator;
import com.command.base.service.StoreService;
import com.command.base.window.Application;
import com.command.server.worker.ServerWorker;

public class UseConnectHandler implements Handler {

    @Override
    public boolean adapt(Object object) {
        if (object instanceof Command) {
            return ((Command) object).is(ServerWorker.COMMAND_USE);
        }
        return false;
    }

    @Override
    public boolean handle(HandleContext context) {
        Command command = (Command) context.model;
        StoreService storeService = ServiceLocator.getService(StoreService.class);
        IoAcceptor acceptor = storeService.getValue("acceptor", IoAcceptor.class);
        Map<Long, IoSession> sessions = acceptor.getManagedSessions();
        try {
            long key = Long.parseLong(command.getParam("-k"));
            IoSession session = sessions.get(key);
            if (session != null && session.isActive()) {
                storeService.save("currentSession", session);
                Application.console.println(String.format("%s used", getIpPort(session)));
            } else {
                throw new RuntimeException();
            }
        } catch (NumberFormatException e) {
            Application.console.println("error! use -k specify connect");
        } catch (Exception e) {
            Application.console.println("list to freshen");
        }
        return true;
    }

    private String getIpPort(IoSession session) {
        InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
        return String.format("%s %s", address.getHostName(), address.getPort());
    }

}
