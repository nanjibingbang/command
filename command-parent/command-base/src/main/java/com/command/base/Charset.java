package com.command.base;

public enum Charset {

    UTF8(java.nio.charset.Charset.forName("UTF-8"), 0), GBK(java.nio.charset.Charset.forName("GBK"),
            1), ISO88591(java.nio.charset.Charset.forName("ISO8859-1"), 2);

    private Charset(java.nio.charset.Charset charset, int code) {
        this.charset = charset;
        this.code = code;
    }

    private java.nio.charset.Charset charset;
    private int code;

    public java.nio.charset.Charset getCharset() {
        return charset;
    }

    public void setCharset(java.nio.charset.Charset charset) {
        this.charset = charset;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static Charset fromCode(int code) {
        Charset[] values = Charset.values();
        for (Charset charset : values) {
            if (charset.code == code) {
                return charset;
            }
        }
        return null;
    }

}
