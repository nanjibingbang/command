package com.command.base.utils;

public class ByteUtils {

    public static final int BYTE_BLANK = 0x7fffffff;

    public static int toNum(byte... bs) {
        int result = 0;
        for (int i = bs.length; i > 0; i--) {
            result += Byte.toUnsignedInt(bs[i - 1]) << (Byte.SIZE * (bs.length - i));
        }
        return result;
    }

    /**
     * 
     * @param num
     *            大于0
     * @return
     */
    public static byte[] toBytes(int num) {
        byte[] bs = new byte[Integer.BYTES];
        int index = Integer.BYTES - 1;
        while (num > 0) {
            bs[index--] = (byte) (num & 0xff);
            num = num >> Byte.SIZE;
        }
        byte[] result = new byte[Integer.BYTES - index - 1];
        System.arraycopy(bs, index + 1, result, 0, result.length);
        return result;
    }

    public static int toOpposite(int i) {
        if (i >= 0) {
            return i | 0x80000000;
        } else {
            return i & 0x7fffffff;
        }
    }

}
