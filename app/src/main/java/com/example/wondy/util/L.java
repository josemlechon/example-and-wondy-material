package com.example.wondy.util;

import android.util.Log;

import com.example.wondy.BuildConfig;


/**
 *
 * @author Jose m lechon on 02/02/16.
 * @version 1.0.0
 * @since 1
 */
public class L {

    private static final String LOG_PREFIX = "test_";

    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();

    private static final int MAX_LOG_TAG_LENGTH = 23;


    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static void LOGD(final String tag, String message) {
        final String shortTag = makeLogTag(tag);
        if (BuildConfig.DEBUG || Log.isLoggable(shortTag, Log.DEBUG)) {
            Log.d(shortTag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        final String shortTag = makeLogTag(tag);
        if (BuildConfig.DEBUG || Log.isLoggable(shortTag, Log.DEBUG)) {
            Log.d(shortTag, message, cause);
        }
    }

    public static void LOGV(final String tag, String message) {
        final String shortTag = makeLogTag(tag);
        if (BuildConfig.DEBUG && Log.isLoggable(shortTag, Log.VERBOSE)) {
            Log.v(shortTag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        final String shortTag = makeLogTag(tag);
        if (BuildConfig.DEBUG && Log.isLoggable(shortTag, Log.VERBOSE)) {
            Log.v(shortTag, message, cause);
        }
    }

    public static void LOGI(final String tag, String message) {
        Log.i(makeLogTag(tag), message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(makeLogTag(tag), message, cause);
    }

    public static void LOGW(final String tag, String message) {
        Log.w(makeLogTag(tag), message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(makeLogTag(tag), message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(makeLogTag(tag), message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(makeLogTag(tag), message, cause);

    }

    private L() {
    }
}
