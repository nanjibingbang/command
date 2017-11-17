package com.command.base.packet;

import com.command.base.Charset;
import com.command.base.utils.ByteUtils;

public class PacketBuffer {

    private byte[] bytes = new byte[1];
    private int size = 0;
    private int ps = 0;
    private int pl = 0;

    private byte head = 0;
    private boolean hasUuid = false;

    // private int cleanFlag = 0xffff;

    public PacketBuffer() {
    }

    /**
     * flow to next packet
     * 
     * @return <code>true</code> if current bytes include a packet else
     *         <code>false</code>
     */
    private boolean next() {
        ps += pl;
        pl = 0;

        if (size > ps) {
            head = bytes[ps];
            hasUuid = (head & 0x40) == 0x40;

            int lenlen = head & 0x3;
            if (size > ps + lenlen) {
                byte[] lenbs = new byte[lenlen];
                System.arraycopy(bytes, ps + 1, lenbs, 0, lenlen);
                int payLoadLen = ByteUtils.toNum(lenbs);
                if (size > ps + lenlen + payLoadLen) {
                    ps += lenlen + 1;
                    pl = payLoadLen;
                    return true;
                }
            }
        }
        return false;
    }

    public void append(byte[] bs, int offest, int len) {
        extendsize(len);
        System.arraycopy(bs, offest, bytes, size, len);
        size += len;
    }

    private void extendsize(int len) {
        if (bytes.length - size < len) {
            int newLen = size + len * 2;
            byte[] newBs = new byte[newLen];
            System.arraycopy(bytes, 0, newBs, 0, size);
            bytes = newBs;
        }
    }

    public Packet readPacket() {
        if (!next()) {
            return null;
        }
        int payloadLen = pl;
        String uuid = null;
        if (hasUuid) {
            byte[] ubs = new byte[36];
            System.arraycopy(bytes, ps, ubs, 0, 36);
            uuid = new String(ubs, Charset.UTF8.getCharset());
            ps += 36;
        }
        byte[] payload = new byte[payloadLen];
        System.arraycopy(bytes, ps + (hasUuid ? 36 : 0), payload, 0, payloadLen);
        return new Packet(head, payload, uuid);
    }

    public void clean() {
        bytes = new byte[1];
        size = 0;
        ps = 0;
        pl = 0;
        head = 0;
        hasUuid = false;
    }

}
