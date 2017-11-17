package com.command.server;

import java.io.IOException;
import java.util.Enumeration;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.command.base.handler.ChainHandler;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.base.packet.Packet;
import com.command.base.service.CommandService;
import com.command.base.service.ServiceLocator;
import com.command.base.service.StoreService;
import com.command.base.service.TransformService;
import com.command.base.window.Application;

public class IoHandlerImpl extends IoHandlerAdapter {

    private CommandService commandService;
    private StoreService storeService;

    public IoHandlerImpl(CommandService commandService, StoreService storeService) {
        this.commandService = commandService;
        this.storeService = storeService;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Packet packet = (Packet) message;
        boolean handled = false;
        HandleContext context = new HandleContext();
        context.session = session;
        if (packet.getUuid() != null) {
            context.model = packet;
            context.uuid = packet.getUuid();
            ChainHandler handler = commandService.allocateHandler(context.uuid);
            if (handler != null && handler.handle(context)) {
                handled = true;
            }
        } else {
            Enumeration<TransformService> enumeration = ServiceLocator.getServices(TransformService.class);
            while (enumeration.hasMoreElements()) {
                TransformService element = enumeration.nextElement();
                if (element.adapt(packet)) {
                    context.model = element.tranform(packet);
                    Handler handler = commandService.allocateHandler(context.model);
                    if (handler != null && handler.handle(context)) {
                        handled = true;
                    }
                }
            }
        }

        if (!handled) {// 尝试不转换
            context.model = packet;
            Handler handler = commandService.allocateHandler(packet);
            if (handler != null && handler.handle(context)) {
                handled = true;
            }
        }

        if (!handled) {
            Application.console.println(String.format("packet not handle:%s uuid:%s", packet.toString(),
                    packet.getUuid() == null ? "" : new String(packet.getUuid())));
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            if (!session.isActive()) {
                IoSession current = storeService.getValue("currentSession", IoSession.class);
                if (current == session) {
                    storeService.save("currentSession", null);
                }
            }
        } else {
            cause.printStackTrace(Application.console);
        }
    }
}
