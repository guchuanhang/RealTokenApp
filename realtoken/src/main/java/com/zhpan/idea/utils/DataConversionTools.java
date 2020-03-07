package com.zhpan.idea.utils;

import java.nio.charset.Charset;
import java.util.Calendar;

/**
 * Created by jokerlee on 16-6-23.
 * 这是一个用于将不同数据类型转换为byte数组或者解码byte组为其它数据的工具类
 * Slc 设备基本采用高位在前的数据传输
 */
public class DataConversionTools {

    /**
     * 获取低8位的byte数据
     * @param v
     * @return
     */
    public static byte lowUint16(short v) {
        return (byte) (v & 0xFF);
    }


    /**
     * 将byte数组转换为十六进制的字符串
     * @param data
     * @return
     */
    public static String bytesToHexString(byte[] data){
        String result = "";
        for( byte b : data ){
            result += String.format("%02x",b);
        }
        return  result;
    }

}