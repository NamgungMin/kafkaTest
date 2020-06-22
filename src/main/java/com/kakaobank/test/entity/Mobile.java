package com.kakaobank.test.entity;

public class Mobile {
    private final String MOBILESCHEMA =
            "  {" +
            "    \"namespace\": \"com.kakaobank.test.entity\"," +
            "    \"type\": \"record\"," +
            "    \"name\": \"Mobile\"," +
            "    \"fields\": [" +
            "        {\"name\": \"logId\", \"type\": \"string\"}," +
            "        {\"name\": \"userId\", \"type\": \"string\"}," +
            "        {\"name\": \"logDt\",  \"type\": \"string\"}," +
            "        {\"name\": \"deviceId\",  \"type\": \"string\"}," +
            "        {\"name\": \"remoteIp\",  \"type\": \"string\"}," +
            "        {\"name\": \"osType\",  \"type\": \"string\"}," +
            "        {\"name\": \"osVersion\",  \"type\": \"string\"}" +
            "    ]" +
            "  }";

    private String logId;
    private String userId;
    private String logDt;
    private String deviceId;
    private String remoteIp;
    private String osType;
    private String osVersion;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogDt() {
        return logDt;
    }

    public void setLogDt(String logDt) {
        this.logDt = logDt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getMOBILESCHEMA() {
        return MOBILESCHEMA;
    }

}
