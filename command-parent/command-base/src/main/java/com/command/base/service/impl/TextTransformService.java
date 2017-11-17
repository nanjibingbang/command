package com.command.base.service.impl;

import com.command.base.Charset;
import com.command.base.packet.Packet;
import com.command.base.service.TransformService;

public class TextTransformService implements TransformService<String> {

    @Override
    public String tranform(Packet packet) {
        return packet.toString();
    }

    @Override
    public Packet serialize(String model) {
        Packet packet = new Packet(model.getBytes(Charset.UTF8.getCharset()));
        packet.setText(true);
        packet.setCharsetCode(Charset.UTF8.getCode());
        return packet;
    }

    @Override
    public boolean adapt(Object object) {
        return ((Packet) object).isText();
    }
}
