package com.zhpan.idea;

import com.zhpan.idea.utils.FileUtils;

import java.io.File;

public class ServerConfig {
    public static ServerConfig instance = new ServerConfig();
    public String savePath = "";
    public TokenBean tokenBean;

    private ServerConfig() {
        File saveFile = new File(RealToken.getContext().getExternalFilesDir("config"), "realtoken");
        savePath = saveFile.getAbsolutePath();
        Object savedObj = FileUtils.readObject(savePath);
        if (savedObj instanceof TokenBean) {  //　在 JavaSE规范 中对 instanceof 运算符的规定就是：如果 obj 为 null，那么将返回 false。
            tokenBean = (TokenBean) savedObj;
        }
    }
}
