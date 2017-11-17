package com.liou.scriptcall.info;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.mina.core.session.IoSession;

import com.command.base.packet.Packet;
import com.command.base.window.Application;

public class MessagePipeline {

    private Object payload;

    private MessagePipeline() {
    }

    public static MessagePipeline wrap(Object payload) {
        MessagePipeline messagePipeline = new MessagePipeline();
        messagePipeline.payload = payload;
        return messagePipeline;
    }

    public MessagePipeline append(String line) throws IOException {
        if (payload instanceof OutputStream) {
            ((OutputStream) payload).write(line.getBytes(Application.defaultCharset.getCharset()));
        } else if (payload instanceof IoSession) {
            Packet packet = new Packet(line.getBytes(Application.defaultCharset.getCharset()));
            packet.setCharsetCode(Application.defaultCharset.getCode());
            packet.setStream(true);
            packet.setText(true);
            ((IoSession) payload).write(packet);
        }
        return this;
    }

}
