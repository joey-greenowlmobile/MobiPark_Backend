package com.greenowl.callisto.service.config;

import com.greenowl.callisto.domain.AppConfig;
import com.greenowl.callisto.repository.AppConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServiceImpl.class);
    private Map<String, String> cache = null;

    @Inject
    private AppConfigRepository appConfigRepository;

    @Override
    public void update() {
        LOG.info("Updating local copy of configuration map!");
        cache = new HashMap<>();
        updateCacheFromDB();
    }

    @Override
    public <T> T get(String key, Class<T> cls) {
        healthCheck();
        return get(key, cls, null);
    }

    @Override
    public <T> T get(String key, Class<T> cls, T defaultValue) {
        healthCheck();
        String value = cache.get(key);
        T cachedValue = cls.cast(value);
        return (cachedValue != null) ? cachedValue : defaultValue;
    }

    @Override
    public void set(String key, Object item) {
        healthCheck();
        cache.put(key, (String) item);
    }

    @Override
    public <T> T set(String key, Class<T> cls, T item) {
        healthCheck();
        set(key, item);
        return item;
    }

    @Override
    public Map<String, String> getMap() {
        healthCheck();
        return cache;
    }

    private void updateCacheFromDB() {
        List<AppConfig> configs = appConfigRepository.findAll();
        LOG.debug("Found {} configurations from database", configs.size());
        configs.forEach(this::addToCache);
    }

    private void addToCache(AppConfig config) {
        set(config.getKey(), config.getValue());
    }

    private void healthCheck() {
        if (cache == null) {
            update();
        }
    }

}
