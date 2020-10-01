package com.zhpan.idea;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhpan.idea.utils.Utils;


public class PrefencesUtil {
    public static final String FILE_NAME = "data";

    public static void writeString(String key, String valx) {
        SharedPreferences sharedPreferences =
                Utils.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, valx);
        editor.commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences =
                Utils.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}