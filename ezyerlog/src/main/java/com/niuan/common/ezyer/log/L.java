package com.niuan.common.ezyer.log;

import android.content.Context;
import android.util.Log;

/**
 * Created by Carlos on 2015/9/25.
 */
public class L {

    private static int LOG_LEVEL = 0;
    private static int PID = android.os.Process.myPid();
    private static int PNAME;

    public static void init(Context context, String logPath) {

    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        Log.w(tag, msg, t);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        Log.e(tag, msg, t);
    }

    // Pid:$pid, Tid:$tid, $msg ($class, $file_name:$line_number) 
    private String wrapMessage(int logLevel, String msg) {

        Thread currentThread = Thread.currentThread();

        long tid = currentThread.getId();

        StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
        StackTraceElement callerStackTrace = stackTraceElements[2];
        StringBuilder builder = new StringBuilder();
        builder.append("P:").append(PID)
                .append(", T:").append(tid)
                .append(", ").append(msg)
                .append("(").append(callerStackTrace.getClassName()).append(".").append(callerStackTrace.getMethodName())
                .append(", at ").append(callerStackTrace.getFileName()).append(" ").append(callerStackTrace.getLineNumber()).append(")");

        return builder.toString();
    }
}
