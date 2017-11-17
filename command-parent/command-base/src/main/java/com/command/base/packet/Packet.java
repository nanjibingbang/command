package com.command.base.packet;

import com.command.base.Charset;
import com.command.base.utils.ByteUtils;

public class Packet {

    /*
     * (1 保留)(1 uuid)(1 字节流)(1 文本消息)(11 (文本消息表示编码,其余表示处理结果))(11 长度字节)
     */
    private byte head = 0;
    private byte[] payload;// 负载
    private String uuid; // 36位字符

    private byte[] lenBytes;

    public static final int MAX_PAYLOAD_LENGHT = 0xffffff;

    public static final int RESULT_ACK = 0x1;
    public static final int RESULT_REFUSE = 0x2;

    public Packet(byte[] bs) {
        int pl = bs == null ? 0 : bs.length;
        lenBytes = ByteUtils.toBytes(pl);
        head = (byte) lenBytes.length;
        if (MAX_PAYLOAD_LENGHT < pl) {
            throw new IllegalArgumentException("too much bytes");
        }
        if (bs != null) {
            payload = new byte[pl];
            System.arraycopy(bs, 0, payload, 0, bs.length);
        }
    }

    public Packet(byte[] bs, int offest, int len) {
        if (MAX_PAYLOAD_LENGHT < len) {
            throw new IllegalArgumentException("too much bytes");
        }
        payload = new byte[len];
        System.arraycopy(bs, offest, payload, 0, len);
        lenBytes = ByteUtils.toBytes(len);
        head = (byte) lenBytes.length;
    }

    protected Packet(byte head, byte[] bs, String uuid) {
        this.head = head;
        this.payload = bs;
        this.uuid = uuid;
    }

    public boolean is(int sign) {
        return (head & sign) == sign;
    }

    public byte getHead() {
        return head;
    }

    public byte[] getPayload() {
        return payload;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        if (uuid != null) {
            if (uuid.length() != 36) {
                throw new IllegalArgumentException();
            }
            head |= 0x40;
        } else {
            head &= 0xbf;
        }
        this.uuid = uuid;
    }

    public boolean isStream() {
        return is(0x20);
    }

    public void setStream(boolean isStream) {
        if (isStream) {
            head |= 0x20;
        } else {
            head &= 0xdf;
        }
    }

    public boolean isText() {
        return is(0x10);
    }

    public void setText(boolean isText) {
        if (isText) {
            head |= 0x10;
        } else {
            head &= 0xef;
        }
    }

    public int getCharsetCode() {
        return (head & 0xc) >> 2;
    }

    public void setCharsetCode(int charsetCode) {
        if (isText()) {
            head = (byte) ((head & 0xf3) + ((charsetCode << 2) & 0xc));
        }
    }

    public int getResult() {
        return (head & 0xc) >> 2;
    }

    public void setResult(int result) {
        if (!isText()) {
            head = (byte) ((head & 0xf3) + ((result << 2) & 0xc));
        }
    }

    public final byte[] packing() {
        int len = payload == null ? 0 : payload.length;
        len += 1;
        if (uuid != null) {
            len += 36;
        }
        int lenlen = lenBytes.length;
        len += lenlen;
        byte[] result = new byte[len];
        result[0] = head;
        System.arraycopy(lenBytes, 0, result, 1, lenlen);
        if (uuid != null) {
            System.arraycopy(uuid.getBytes(Charset.UTF8.getCharset()), 0, result, 1 + lenlen, 36);
        }
        if (payload != null) {
            System.arraycopy(payload, 0, result, len - payload.length, payload.length);
        }
        return result;
    }

    /**
     * 将负载以utf-8编码转为字符串
     */
    @Override
    public String toString() {
        return new String(payload, Charset.UTF8.getCharset());
    }

}
