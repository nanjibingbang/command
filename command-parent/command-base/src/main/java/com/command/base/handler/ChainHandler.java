package com.command.base.handler;

import java.util.UUID;

/**
 * 
 * @author liou
 *
 */
public abstract class ChainHandler implements Handler {

    private String uuid;

    public ChainHandler() {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    /**
     * uuid 用于标识处理链
     * 
     * @return
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 链式处理器根据uuid适配
     * 
     * @return always true
     */
    @Override
    public final boolean adapt(Object object) {
        return true;
    }

}
