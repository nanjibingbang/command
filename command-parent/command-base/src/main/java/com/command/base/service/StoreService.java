package com.command.base.service;

import java.util.Set;

public interface StoreService {

    <T> void save(String key, T value);

    <T> T getValue(String key, Class<T> clazz);

    Set<String> keys();
}
