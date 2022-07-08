package com.java.busylooping.backget.models;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public class DataHolderDownloadItem {
    private String name;
    private String date;
    private String size;
    private String location;
    private String downloadRes;


    private int backgroundRef;

    public DataHolderDownloadItem(String name, String date, String size, String location, String downloadRes, int backgroundRef) {
        this.name = name;
        this.date = date;
        this.size = size;
        this.location = location;
        this.downloadRes = downloadRes;
        this.backgroundRef = backgroundRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDownloadRes() {
        return downloadRes;
    }

    public void setDownloadRes(String downloadRes) {
        this.downloadRes = downloadRes;
    }

    public int getBackgroundRef() {
        return backgroundRef;
    }

    public void setBackgroundRef(int backgroundRef) {
        this.backgroundRef = backgroundRef;
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format(Locale.getDefault(), "%.1f %cB", value / 1024.0, ci.current());
    }

    public static String humanReadablePath(String rawPath) {
        String newPath = new String(rawPath);
        newPath = newPath.replace("/storage/emulated/0", "Storage");
        newPath = newPath.replace("/", "  <  ");
        StringBuilder stringBuilder = new StringBuilder(newPath);
        if (newPath.endsWith("  <  ")) {
            newPath = stringBuilder.replace(newPath.lastIndexOf("  <  "), newPath.length(), "").toString();
        }
        return newPath;
    }
}
