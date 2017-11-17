package com.liou.scriptcall.handler;

import com.command.base.Charset;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.base.packet.Packet;
import com.liou.scriptcall.Constants;
import com.liou.scriptcall.command.TestCommand;
import com.liou.scriptcall.info.MessagePipeline;
import com.liou.scriptcall.manager.ScriptCallManager;

public class ScriptCallTestHandler implements Handler {

    @Override
    public boolean adapt(Object object) {
        if (object instanceof String) {
            return object.toString().startsWith(Constants.CALL_TEST);
        }
        return false;
    }

    @Override
    public boolean handle(HandleContext context) {
        Packet packet = new Packet("TEST".getBytes(Charset.UTF8.getCharset()));
        packet.setStream(true);
        packet.setResult(Packet.RESULT_ACK);
        context.session.write(packet);
        ScriptCallManager.getInstance().execCommand(new TestCommand(context.model.toString()),
                MessagePipeline.wrap(context.session), "user");
        return true;
    }

}
