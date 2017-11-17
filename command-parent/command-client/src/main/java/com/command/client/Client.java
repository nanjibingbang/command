package com.command.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.handler.HandlerLoader;
import com.command.base.packet.PacketCodecFactory;
import com.command.base.service.CommandService;
import com.command.base.service.ServiceLocator;
import com.command.base.service.impl.DefaultCommandService;
import com.command.base.service.impl.DefaultStoreService;
import com.command.base.service.impl.TextTransformService;
import com.command.base.window.Application;
import com.command.base.window.Worker;
import com.command.base.window.impl.DefaultWorker;
import com.command.client.beating.KeepAliveMessageFactoryImpl;

public class Client extends Application {
    public static final String BUILD_COMMAND = "build";

    public static final String COMMAND_EXIT = "exit";

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(Worker Worker) {
        super(Worker);
    }

    @Override
    public boolean init() {
        ServiceLocator.instanceOf(DefaultStoreService.class);
        ServiceLocator.instanceOf(TextTransformService.class);
        CommandService commandService = ServiceLocator.instanceOf(DefaultCommandService.class);
        HandlerLoader loader = new HandlerLoader(commandService);
        try {
            loader.loadConfigHandlers();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String[] getConfigFiles() {
        return new String[] { "config.properties" };
    }

    public void connectServer() throws Throwable {
        String serverIp = getConfig("server_ip");
        int serverPort = Integer.parseInt(getConfig("server_port"));
        IoConnector connector = new NioSocketConnector();
        connector.setHandler(new IoHandlerImpl());
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageFactoryImpl(), IdleStatus.BOTH_IDLE);
        keepAliveFilter.setForwardEvent(true);
        keepAliveFilter.setRequestInterval(Integer.parseInt(getConfig("beating_period")));
        keepAliveFilter.setRequestTimeoutHandler(KeepAliveRequestTimeoutHandler.CLOSE);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PacketCodecFactory()));
        connector.getFilterChain().addLast("heart", keepAliveFilter);
        ConnectFuture connect = connector.connect(new InetSocketAddress(serverIp, serverPort));
        try {
            connect = connect.await();
            if (connect.getException() != null) {
                throw connect.getException();
            }
        } catch (InterruptedException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        Client client = new Client(new DefaultWorker());
        try {
            client.connectServer();
            Application.console.println("connect server success");
            client.mainLoop();
        } catch (Throwable e) {
            logger.error("connect to server error", e);
        }
    }

}
