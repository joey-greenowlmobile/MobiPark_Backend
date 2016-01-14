package com.greenowl.callisto.service.config;

import java.util.Map;

public interface ConfigService {

    void update();

    /**
     * pull a config object, if no object is found will return null.
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> cls);

    /**
     * Pull a config object with a default value.
     *
     * @param key
     * @param cls
     * @param defaultValue
     * @param <T>
     * @return
     */
    <T> T get(String key, Class<T> cls, T defaultValue);

    void set(String key, Object item);

    /**
     * Set a config with type = :cls.
     *
     * @param key  The key for this config
     * @param cls  the type of the config
     * @param item The item to set.
     * @param <T>  The generic type fo the item to set.
     * @return The item set.
     */
    <T> T set(String key, Class<T> cls, T item);

    Map<String, String> getMap();
}
