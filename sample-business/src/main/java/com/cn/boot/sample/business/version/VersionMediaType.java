package com.cn.boot.sample.business.version;

import org.springframework.http.MediaType;

/**
 * @author Chen Nan
 */
public class VersionMediaType {
    private static final String MEDIA_TYPE_TEMP = "application/vnd.%s.%s+json";
    private final String appName = "cn";
    private final String version;
    private final MediaType mediaType;

    public VersionMediaType(String version) {
        this.version = version;
        this.mediaType = MediaType.valueOf(String.format("application/vnd.%s.%s+json", "cn", version));
    }

    public String toString() {
        return this.mediaType.toString();
    }

    public String getAppName() {
        this.getClass();
        return "blade";
    }

    public String getVersion() {
        return this.version;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }
}
