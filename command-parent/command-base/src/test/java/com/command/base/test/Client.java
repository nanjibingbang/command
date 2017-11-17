package com.command.base.test;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.command.base.packet.PacketCodecFactory;

public class Client {

    private IoConnector connector;
    private IoSession session;

    public void start() {
        connector = new NioSocketConnector();
        connector.setHandler(new IoHandlerAdapter() {
            @Override
            public void sessionOpened(IoSession se) throws Exception {
                session = se;
            }

        });
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PacketCodecFactory()));
        connector.connect(new InetSocketAddress("127.0.0.1", 30));
    }

    public void send(String message) {
        session.write(message);
    }

}
