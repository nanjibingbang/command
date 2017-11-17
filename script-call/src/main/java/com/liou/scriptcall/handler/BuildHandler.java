package com.liou.scriptcall.handler;

import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.liou.scriptcall.Constants;
import com.liou.scriptcall.command.BuildCommand;
import com.liou.scriptcall.manager.ScriptCallManager;

public class BuildHandler implements Handler {

    @Override
    public boolean adapt(Object object) {
        if (object instanceof String) {
            return object.toString().startsWith(Constants.CALL_BUILD);
        }
        return false;
    }

    @Override
    public boolean handle(HandleContext context) {
        BuildCommand buildCommand = new BuildCommand(context.model.toString());
        ScriptCallManager.getInstance().execCommand(buildCommand, null, "");
        return true;
    }

}
