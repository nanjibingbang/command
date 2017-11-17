package com.command.server.handler;

import java.net.InetSocketAddress;
import java.util.Iterator;
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

public class ListHandler implements Handler {

    @Override
    public boolean handle(HandleContext context) {
        StoreService storeService = ServiceLocator.getService(StoreService.class);
        IoAcceptor acceptor = storeService.getValue("acceptor", IoAcceptor.class);
        Map<Long, IoSession> sessions = acceptor.getManagedSessions();
        if (sessions.size() == 0) {
            Application.console.println("empty");
        } else {
            Iterator<Long> it = sessions.keySet().iterator();
            while (it.hasNext()) {
                Long key = it.next();
                IoSession value = sessions.get(key);
                Application.console.println(String.format("%s %s", key, getIpPort(value)));
            }
        }
        return true;
    }

    private String getIpPort(IoSession session) {
        InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
        return String.format("%s %s", address.getHostName(), address.getPort());
    }

    @Override
    public boolean adapt(Object object) {
        if (object instanceof Command) {
            return ((Command) object).is(ServerWorker.COMMAND_LIST);
        }
        return false;
    }

}
