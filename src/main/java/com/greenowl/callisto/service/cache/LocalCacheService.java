package com.greenowl.callisto.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LocalCacheService implements CacheService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalCacheService.class);

    private Map<String, Object> cache = new HashMap<>();

    @Override
    public void clear() {
        cache = new HashMap<>();
    }

    @Override
    public <T> T get(String key, Class<T> cls) {
        return cls.cast(cache.get(key));
    }

    @Override
    public void set(String key, Object item) {
        cache.put(key, item);
    }
}
