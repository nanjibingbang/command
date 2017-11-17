package com.command.base.service.impl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.command.base.handler.ChainHandler;
import com.command.base.handler.Handler;
import com.command.base.service.CommandService;

public class DefaultCommandService implements CommandService {

    private Map<String, ChainHandler> uuidMap = new ConcurrentHashMap<String, ChainHandler>();

    private Vector<Handler> handlers = new Vector<Handler>();

    @Override
    public ChainHandler allocateHandler(String uuid) {
        Iterator<Entry<String, ChainHandler>> it = uuidMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, ChainHandler> next = it.next();
            if (next.getKey().equals(uuid)) {
                it.remove();
                return next.getValue();
            }
        }
        return null;
    }

    @Override
    public Handler allocateHandler(Object obj) {
        Enumeration<Handler> elements = handlers.elements();
        while (elements.hasMoreElements()) {
            Handler element = elements.nextElement();
            if (element.adapt(obj)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public void handlUuidPacket(ChainHandler handler, String uuid) {
        uuidMap.put(uuid, handler);
    }

    @Override
    public void removeUuidHandler(String uuid) {
        uuidMap.remove(uuid);
    }

    @Override
    public void registerHandler(Handler handler) {
        handlers.add(handler);
    }

}
