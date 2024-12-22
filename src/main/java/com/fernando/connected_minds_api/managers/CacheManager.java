package com.fernando.connected_minds_api.managers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CacheManager {
    private static Map<String, Object> CACHE = new HashMap<>();
    
    
    @SuppressWarnings("unchecked")
    public <T> T getCacheValue(String key) {
        return (T) CACHE.get(key);
    }

    public <T extends Object> void addCacheValue(String key, T data) {
        CACHE.put(key, data);
    }

    public boolean hasCacheKey(String key) {
        return CACHE.containsKey(key);
    }

    public void removeCacheValueContains(String characters) {
        for (String key: CACHE.keySet()) {
            if (key.contains(characters)) {
                CACHE.remove(key);
                break;
            }
        }
    }
}