package com.command.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.keepalive.KeepAliveFilter;
import org.apache.mina.filter.keepalive.KeepAliveRequestTimeoutHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.config.Configuration;
import com.command.base.packet.PacketCodecFactory;
import com.command.base.service.CommandService;
import com.command.base.service.ServiceLocator;
import com.command.base.service.StoreService;
import com.command.base.service.impl.DefaultCommandService;
import com.command.base.service.impl.DefaultStoreService;
import com.command.base.window.Application;
import com.command.base.window.Worker;
import com.command.server.beating.KeepAliveMessageFactoryImpl;
import com.command.server.handler.ExitHandler;
import com.command.server.handler.ListHandler;
import com.command.server.handler.StreamHandler;
import com.command.server.handler.UseConnectHandler;
import com.command.server.worker.ServerWorker;

public class Server extends Application {

    private static Logger logger = LoggerFactory.getLogger(Server.class);

    private IoAcceptor acceptor;

    public Server(Worker worker) {
        super(worker);
    }

    @Override
    public boolean init() {
        ServiceLocator.instanceOf(DefaultStoreService.class);
        CommandService commandService = ServiceLocator.instanceOf(DefaultCommandService.class);
        commandService.registerHandler(new ListHandler());
        commandService.registerHandler(new UseConnectHandler());
        commandService.registerHandler(new ExitHandler());
        commandService.registerHandler(new StreamHandler());
        return true;
    }

    @Override
    public String[] getConfigFiles() {
        return new String[] { "config.properties" };
    }

    public void doAccept() throws Exception {
        StoreService storeService = ServiceLocator.getService(StoreService.class);
        Configuration configuration = storeService.getValue(Application.STORE_GLOBAL, Configuration.class);
        acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new IoHandlerImpl(ServiceLocator.getService(CommandService.class), storeService));
        int serverPort = Integer.parseInt(configuration.getPropertie("server_port"));
        acceptor.bind(new InetSocketAddress(serverPort));
        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new KeepAliveMessageFactoryImpl(), IdleStatus.BOTH_IDLE);
        keepAliveFilter.setForwardEvent(true);
        keepAliveFilter.setRequestInterval(Integer.parseInt(getConfig("beating_period")));
        keepAliveFilter.setRequestTimeoutHandler(KeepAliveRequestTimeoutHandler.LOG);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PacketCodecFactory()));
        acceptor.getFilterChain().addLast("heart", keepAliveFilter);
        storeService.save("acceptor", acceptor);
    }

    public static void main(String[] args) {
        Server server = new Server(new ServerWorker());
        try {
            server.doAccept();
            server.mainLoop();
        } catch (Exception e) {
            logger.error("start server failure", e);
        }
    }

}
