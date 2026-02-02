package com.nassdk.tradernet.core.logger

import android.util.Log

object AppLogger {
    private const val TAG = "Tradernet"

    fun d(tag: String = TAG, message: String) {
//        if (BuildConfig.DEBUG) {
        Log.d(tag, message)
//        }
    }

    fun e(tag: String = TAG, message: String, throwable: Throwable? = null) {
//        if (BuildConfig.DEBUG) {
        Log.e(tag, message, throwable)
//        }
    }

    fun w(tag: String = TAG, message: String) {
//        if (BuildConfig.DEBUG) {
        Log.w(tag, message)
//        }
    }
}

