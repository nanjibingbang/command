package com.command.server.beating;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

import com.command.base.packet.Packet;

public class KeepAliveMessageFactoryImpl implements KeepAliveMessageFactory {

    @Override
    public boolean isRequest(IoSession session, Object message) {
        if (message instanceof Packet) {
            return ((Packet) message).getHead() == 0;
        }
        return false;
    }

    @Override
    public boolean isResponse(IoSession session, Object message) {
        return false;
    }

    @Override
    public Object getRequest(IoSession session) {
        return null;
    }

    @Override
    public Object getResponse(IoSession session, Object request) {
        return new Packet(null);
    }
    
}
