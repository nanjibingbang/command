package com.command.base.packet;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.command.base.window.Application;

public class PacketEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        Packet packet = null;
        if (message instanceof Packet) {
            packet = (Packet) message;
        } else {
            packet = new Packet(message.toString().getBytes(Application.defaultCharset.getCharset()));
            packet.setText(true);
            packet.setCharsetCode(Application.defaultCharset.getCode());
        }
        byte[] packing = packet.packing();
        IoBuffer buffer = IoBuffer.allocate(packing.length);
        buffer.put(packing);
        buffer.flip();
        out.write(buffer);
    }

    @Override
    public void dispose(IoSession session) throws Exception {
    }

}
