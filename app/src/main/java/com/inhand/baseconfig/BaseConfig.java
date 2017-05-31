package com.inhand.baseconfig;

public class BaseConfig {

    private String serverAddr;

    public BaseConfig() {
    }

    public BaseConfig(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
}
