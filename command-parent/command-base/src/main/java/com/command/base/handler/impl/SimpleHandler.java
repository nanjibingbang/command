package com.command.base.handler.impl;

import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;

public class SimpleHandler implements Handler {

    @Override
    public boolean adapt(Object object) {
        if (object instanceof String) {
            return true;
        }
        return false;
    }

    @Override
    public boolean handle(HandleContext context) {
        System.out.println(context.model.toString());
        return true;
    }

}
