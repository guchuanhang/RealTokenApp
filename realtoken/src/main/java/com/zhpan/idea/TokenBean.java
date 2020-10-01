package com.zhpan.idea;

import java.io.Serializable;

/**
 * @ClassName ConfigBean
 * @Description 生成token，需要使用到的数据结构
 * @Author guchu
 * @Date 2020/10/1 15:57
 * @Version 1.0
 */
public class TokenBean implements Serializable {
    public String deviceName;
    public String secretKey;

}
