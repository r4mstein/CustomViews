package com.example.r4mst.customviews.util.logger;


import android.util.Log;

public final class LoggerDefault implements Logger {

    @Override
    public int d(String _tag, String _msg) {
        return Log.d(_tag, _msg);
    }

    @Override
    public int d(String _tag, String _msg, Throwable _tr) {
        return Log.d(_tag, _msg, _tr);
    }
}
