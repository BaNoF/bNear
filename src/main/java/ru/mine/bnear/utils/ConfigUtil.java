package ru.mine.bnear.utils;

import ru.mine.bnear.BNear;

public class ConfigUtil {
    public static String getString(String path) {
        return HexUtil.color(BNear.instance.getConfig().getString(path));
    }

    public static int getInt(String path) {
        return BNear.instance.getConfig().getInt(path);
    }

    public static  Boolean getBoolean(String path){
        return BNear.instance.getConfig().getBoolean(path);
    }
}
