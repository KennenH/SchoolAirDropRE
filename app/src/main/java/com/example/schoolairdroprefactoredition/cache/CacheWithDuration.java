package com.example.schoolairdroprefactoredition.cache;

public class CacheWithDuration {
    private long duration;
    private String jsonCache;

    public CacheWithDuration(long duration, String jsonCache) {
        this.duration = duration;
        this.jsonCache = jsonCache;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getJsonCache() {
        return jsonCache;
    }

    public void setJsonCache(String jsonCache) {
        this.jsonCache = jsonCache;
    }
}
