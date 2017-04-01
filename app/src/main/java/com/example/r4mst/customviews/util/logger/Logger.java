package com.example.r4mst.customviews.util.logger;


public interface Logger {

    int d(String _tag, String _msg);

    int d(String _tag, String _msg, Throwable _tr);
}
