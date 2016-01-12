package com.greenowl.callisto.service.cache;

public interface CacheService {

    void clear();

    <T> T get(String key, Class<T> cls);

    void set(String key, Object item);

}
