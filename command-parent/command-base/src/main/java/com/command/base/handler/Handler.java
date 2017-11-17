package com.command.base.handler;

import com.command.base.service.Adaptable;

/**
 * 
 * @TODO 回执信息
 * @author liou
 *
 */
public interface Handler extends Adaptable {
    
    boolean handle(HandleContext context);

}
