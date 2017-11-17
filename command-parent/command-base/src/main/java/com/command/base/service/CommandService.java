package com.command.base.service;

import com.command.base.handler.ChainHandler;
import com.command.base.handler.Handler;

/**
 * 任务分配服务
 * 
 * @author liou
 *
 */
public interface CommandService {

    ChainHandler allocateHandler(String uuid);

    Handler allocateHandler(Object obj);

    /**
     * 设置处理链在当前节点上的handler
     * 
     * @param handler
     * @param uuid
     */
    void handlUuidPacket(ChainHandler handler, String uuid);

    /**
     * 从处理链上移除handler 当前节点已完成处理链上任务
     * 
     * @param uuid
     */
    void removeUuidHandler(String uuid);

    void registerHandler(Handler handler);

}
