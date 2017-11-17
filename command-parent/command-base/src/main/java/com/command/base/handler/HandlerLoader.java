package com.command.base.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.command.base.service.CommandService;

public class HandlerLoader {
    private static Logger logger = LoggerFactory.getLogger(HandlerLoader.class);

    private CommandService commandService;

    public HandlerLoader(CommandService commandService) {
        this.commandService = commandService;
    }

    public void loadConfigHandlers() throws Exception {
        try {
            Enumeration<URL> resources = this.getClass().getClassLoader().getResources("handler.config");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                InputStream is = resource.openStream();
                try {
                    doHandlerConfig(is);
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("load handler error", e);
            throw e;
        }
    }

    private void doHandlerConfig(InputStream is)
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while (null != (line = reader.readLine())) {
            Class<?> clazz = Class.forName(line);
            commandService.registerHandler(Handler.class.cast(clazz.newInstance()));
        }
    }

}
