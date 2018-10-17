package com.robin.lazy.cache.util.log;

import android.util.Log;

/**
 * @desc: android官方日志打印的实现
 * @projectName：LazyNetForAndroid
 * @className： AndroidLog
 * @author： jiangyufeng
 * @createTime： 2018/10/17 下午2:39
 */
public class AndroidLog implements ILog {
    /**
     * VERBOSE类型日志
     */
    private final static int VERBOSE = 1;

    /**
     * debug类型日志
     */
    private final static int DEBUG = 2;

    /**
     * info类型日志
     */
    private final static int INFO = 3;

    /**
     * warn类型日志
     */
    private final static int WARN = 4;

    /**
     * error类型日志
     */
    private final static int ERROR = 5;

    /**
     * ASSERT类型日志
     */
    private final static int ASSERT = 6;
    /**
     * 一次日志最大打印长度
     */
    private final static int MAX_LENGTH = 3600;

    @Override
    public void d(String tag, String message) {
        log(DEBUG,tag,message,null);
    }

    @Override
    public void d(String tag, String message, Throwable throwable) {
        log(DEBUG,tag,message,throwable);
    }

    @Override
    public void e(String tag, String message) {
        log(ERROR,tag,message,null);
    }

    @Override
    public void e(String tag, String message, Throwable throwable) {
        log(ERROR,tag,message,throwable);
    }

    @Override
    public void w(String tag, String message) {
        log(WARN,tag,message,null);
    }

    @Override
    public void w(String tag, String message, Throwable throwable) {
        log(WARN,tag,message,throwable);
    }

    @Override
    public void i(String tag, String message) {
        log(INFO,tag,message,null);
    }

    @Override
    public void i(String tag, String message, Throwable throwable) {
        log(INFO,tag,message,throwable);
    }

    @Override
    public void v(String tag, String message) {
        log(VERBOSE,tag,message,null);
    }

    @Override
    public void v(String tag, String message, Throwable throwable) {
        log(VERBOSE,tag,message,throwable);
    }

    @Override
    public void wtf(String tag, String message) {
        log(ASSERT,tag,message,null);
    }

    @Override
    public void wtf(String tag, String message, Throwable throwable) {
        log(ASSERT,tag,message,throwable);
    }

    /**
     * 日志输入方法
     *
     * @param logLevel  日志ji'b级别
     * @param tag
     * @param message
     * @param throwable
     */
    private void log(int logLevel, String tag, String message,
                      Throwable throwable) {
        while (message.length() > MAX_LENGTH) {
            print(logLevel,tag,message.substring(0, MAX_LENGTH),throwable);
            message = message.substring(MAX_LENGTH);
        }
        print(logLevel,tag,message,throwable);
    }

    /**
     * 最终打印方法
     *
     * @param logLevel
     * @param tag
     * @param message
     * @param throwable
     */
    private void print(int logLevel,  String tag, String message,
                        Throwable throwable) {
        switch (logLevel) {
            case VERBOSE:
                if(throwable==null){
                    Log.v(tag, message);
                }else{
                    Log.v(tag, message,throwable);
                }
                break;
            case DEBUG:
                if(throwable==null){
                    Log.d(tag, message);
                }else {
                    Log.d(tag, message,throwable);
                }
                break;
            case INFO:
                if(throwable==null){
                    Log.i(tag, message);
                }else {
                    Log.i(tag, message,throwable);
                }
                break;
            case WARN:
                if(throwable==null){
                    Log.w(tag, message);
                }else{
                    Log.w(tag, message,throwable);
                }
                break;
            case ERROR:
                if(throwable==null){
                    Log.e(tag, message);
                }else {
                    Log.e(tag, message,throwable);
                }
                break;
            case ASSERT:
                if(throwable==null){
                    Log.wtf(tag, message);
                }else{
                    Log.wtf(tag, message,throwable);
                }
                break;
        }
    }
}
