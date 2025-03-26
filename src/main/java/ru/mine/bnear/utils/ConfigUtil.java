// ConfigUtil.java
package ru.mine.bnear.utils;

import ru.mine.bnear.BNear;

public class ConfigUtil {
    public static String getString(String path) {
        return getString(path, "");
    }

    public static String getString(String path, String def) {
        return HexUtil.color(BNear.instance.getConfig().getString(path, def));
    }

    public static int getInt(String path) {
        return BNear.instance.getConfig().getInt(path);
    }

    public static boolean getBoolean(String path) {
        return BNear.instance.getConfig().getBoolean(path);
    }
}