package com.example.news_core

import android.util.Log

public interface Loggers {
    public fun d(
        tag: String,
        message: String
    )

    public fun e(
        tag: String,
        message: String
    )
}

public fun AndroidLogcatLogger(): Loggers =
    object : Loggers {
        override fun d(
            tag: String,
            message: String
        ) {
            Log.d(tag, message)
        }

        override fun e(
            tag: String,
            message: String
        ) {
            Log.e(tag, message)
        }
    }
