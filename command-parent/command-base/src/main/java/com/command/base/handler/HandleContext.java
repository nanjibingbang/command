package com.command.base.handler;

import org.apache.mina.core.session.IoSession;

public class HandleContext {

    public String uuid;

    public Object model;

    /**
     * 如果命令从session中分配到 将记录该session
     */
    public IoSession session;

}
