package com.command.base.packet;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class PacketDecoder implements ProtocolDecoder {

    private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        PacketBuffer packetBuffer = getPacketBuffer(session);
        packetBuffer.append(in.array(), in.arrayOffset(), in.remaining());
        Packet packet = null;
        while (null != (packet = packetBuffer.readPacket())) {
            out.write(packet);
        }
        in.position(in.remaining());
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        getPacketBuffer(session).clean();
    }

    private PacketBuffer getPacketBuffer(IoSession session) {
        PacketBuffer packetBuffer = (PacketBuffer) session.getAttribute(CONTEXT);
        if (packetBuffer == null) {
            packetBuffer = new PacketBuffer();
            session.setAttribute(CONTEXT, packetBuffer);
        }
        return packetBuffer;
    }

}
