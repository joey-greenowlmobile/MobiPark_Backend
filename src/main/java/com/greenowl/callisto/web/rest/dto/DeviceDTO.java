package com.greenowl.callisto.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDTO {

    private Long id;

    @NotEmpty
    private String deviceId = null;

    private String clientVersion = null;

    @NotEmpty
    private String platform = null; // IOS, Android

    private String osVersion = null;

    @NotEmpty
    private String pushInfo = null; // Information For sending push messages

    private String aloharId;

    public DeviceDTO() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPushInfo() {
        return pushInfo;
    }

    public void setPushInfo(String pushInfo) {
        this.pushInfo = pushInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAloharId() {
        return aloharId;
    }

    public void setAloharId(String aloharId) {
        this.aloharId = aloharId;
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
                "aloharId='" + aloharId + '\'' +
                ", pushInfo='" + pushInfo + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", platform='" + platform + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", id=" + id +
                '}';
    }
}
