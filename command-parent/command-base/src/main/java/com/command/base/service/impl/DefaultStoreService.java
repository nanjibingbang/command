package com.command.base.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.command.base.service.StoreService;

public class DefaultStoreService implements StoreService {

    private Map<String, Object> storeMap = new ConcurrentHashMap<String, Object>();

    @Override
    public <T> void save(String key, T value) {
        storeMap.put(key, value);
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return clazz.cast(storeMap.get(key));
    }

    @Override
    public Set<String> keys() {
        return storeMap.keySet();
    }

}
