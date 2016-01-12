package com.greenowl.callisto.util;

import com.greenowl.callisto.config.ApiVersion;

public class ApiUtil {

    private static final ThreadLocal<ApiVersion> threadLocal = new ThreadLocal<>();

    public static void setVersion(ApiVersion version) {
        threadLocal.set(version);
    }

    public static ApiVersion getVersion() {
        if(threadLocal.get() == null){
            threadLocal.set(ApiVersion.latest());
        }
        return threadLocal.get();
    }

}
