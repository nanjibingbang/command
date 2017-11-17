package com.command.server.handler;

import com.command.base.Charset;
import com.command.base.handler.HandleContext;
import com.command.base.handler.Handler;
import com.command.base.packet.Packet;
import com.command.base.window.Application;

public class StreamHandler implements Handler {

    @Override
    public boolean adapt(Object object) {
        if (object instanceof Packet) {
            return ((Packet) object).isStream();
        }
        return false;
    }

    @Override
    public boolean handle(HandleContext context) {
        Packet packet = (Packet) context.model;
        if (packet.isText()) {
            Application.console
                    .println(new String(packet.getPayload(), Charset.fromCode(packet.getCharsetCode()).getCharset()));
        } else {
            if (Packet.RESULT_ACK == packet.getResult()) {
                Application.console
                        .println("stream start topic:" + new String(packet.getPayload(), Charset.UTF8.getCharset()));
            }
        }
        return true;
    }

}
