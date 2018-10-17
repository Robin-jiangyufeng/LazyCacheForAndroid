package com.robin.lazy.cache.util.log;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @desc: 日志打印的被委托者
 * @projectName：LazyNetForAndroid
 * @className： CacheLog
 * @author： jiangyufeng
 * @createTime： 2018/10/12 下午5:36
 */
public final class CacheLog {
    /**
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 4;

    /***
     * 是否是debug
     */
    private static boolean isDebug=true;
    /***
     * 委托者
     */
    private static ILog delegate = new AndroidLog();

    /**
     * 重置委托者
     * @param delegate
     */
    public static void resetDelegate(ILog delegate) {
        CacheLog.delegate = delegate;
    }

    /**
     * 设置是否debug模式，debug才会输出日志，默认为true
     * @param isDebug
     */
    public static void setIsDebug(boolean isDebug) {
        CacheLog.isDebug = isDebug;
    }

    public static void d(String tag, String message) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.d(tag,message);
    }

    public static void d(String tag, String message, Throwable throwable) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.d(tag,message,throwable);
    }

    public static void e(String tag, String message) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.e(tag,message);
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.e(tag,message,throwable);
    }

    public static void w(String tag, String message) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.w(tag,message);
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.w(tag,message,throwable);
    }

    public static void i(String tag, String message) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.i(tag,message);
    }

    public static void i(String tag, String message, Throwable throwable) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.i(tag,message,throwable);
    }

    public static void v(String tag, String message) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.v(tag,message);
    }

    public static void v(String tag, String message, Throwable throwable) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.v(tag,message,throwable);
    }

    public static void wtf(String tag, String message) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.wtf(tag,message);
    }

    public static void wtf(String tag, String message, Throwable throwable) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        delegate.wtf(tag, message, throwable);
    }

    /**
     * 打印json数据日志
     * @param tag
     * @param json
     */
    public static void json(String tag, String json) {
        if (!isDebug)return;
        if (delegate == null) throw new NullPointerException("delegate may not be null");
        if (TextUtils.isEmpty(json)) {
            d(tag,"Empty/Null json content");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(tag,message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(tag,message);
            }
        } catch (JSONException e) {
            e(tag,e.getCause().getMessage() + "\n" + json);
        }
    }

}
