package com.ainemo.pad.drawSmoothLine;

public class Log {
    private static boolean isDebug=true;
    public static void d(String object) {
        if(isDebug)
            android.util.Log.d("bessel", object.toString());
    }
}
