package com.greenowl.callisto.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "T_DEVICE")
public class Device extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Size(min = 0, max = 255)
    @Column(length = 255, name = "device_id")
    private String deviceId = null;

    @Size(min = 0, max = 255)
    @Column(length = 255, name = "client_version")
    private String clientVersion = null; // OS version

    private String platform = null; // IOS, Android

    @Size(min = 0, max = 255)
    @Column(length = 255, name = "os_version")
    private String osVersion = null;

    @Size(min = 0, max = 255)
    @Column(length = 255, name = "push_info", nullable = false) // device token
    private String pushInfo = null; // Information For sending push messages

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner; //The owner responsible for creating this Dido

    @Column(name = "endpoint_arn")
    private String endpointARN;

    @Column(name = "registered_device")
    private Boolean registeredDevice; // Identifies that this device belongs to a registered account (i.e User is not null).

    public Device() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getRegisteredDevice() {
        return registeredDevice;
    }

    public void setRegisteredDevice(Boolean registeredDevice) {
        this.registeredDevice = registeredDevice;
    }

    public void setUser(User user) {
        this.owner = user;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getEndpointARN() {
        return endpointARN;
    }

    public void setEndpointARN(String endpointARN) {
        this.endpointARN = endpointARN;
    }


    @Override
    public String toString() {
        return "MobileDevice{" +
                ", deviceId='" + deviceId + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", platform='" + platform + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", pushInfo='" + pushInfo + '\'' +
                '}';
    }
}
