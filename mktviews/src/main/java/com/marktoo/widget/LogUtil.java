package com.marktoo.widget;

import android.util.Log;

/**
 * Class Name: LogUtil
 * Description: log tools
 * Created by marktoo on 2017/5/25
 * Phone: 18910103220
 * Email: 18910103220@189.cn
 */

public class LogUtil {

    private LogUtil() {

    }

    static class LogUtilInit {
        public static LogUtil instance = new LogUtil();
    }

    public static LogUtil getInstance() {
        return LogUtilInit.instance;
    }

    private String TAG = "";

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public void e(String msg) {
        Log.e(getTAG(), msg);
    }

    public void d(String msg) {
        Log.d(getTAG(), msg);
    }

    public void w(String msg) {
        Log.w(getTAG(), msg);
    }

    public void v(String msg) {
        Log.v(getTAG(), msg);
    }
}
