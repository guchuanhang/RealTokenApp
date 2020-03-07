package com.zhpan.idea;

import com.zhpan.idea.utils.PreferencesUtil;

public class ServerConfig {
    public static ServerConfig instance = new ServerConfig();
    public String DEVICE_NAME;
    public String SECRET_KEY;

    private ServerConfig() {
        DEVICE_NAME = (String) PreferencesUtil.get(RealToken.getContext(), "device_name", "");
        SECRET_KEY = (String) PreferencesUtil.get(RealToken.getContext(), "secret_key", "");
    }

}
