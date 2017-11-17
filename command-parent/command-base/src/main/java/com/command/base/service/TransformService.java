package com.command.base.service;

import com.command.base.packet.Packet;

public interface TransformService<T> extends Adaptable {

    T tranform(Packet packet);

    Packet serialize(T model);
}
