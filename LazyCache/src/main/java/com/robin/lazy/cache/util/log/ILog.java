package com.robin.lazy.cache.util.log;

/**
 * @desc: 日志输出接口
 * @projectName：LazyNetForAndroid
 * @className： ILog
 * @author： jiangyufeng
 * @createTime： 2018/10/12 下午5:35
 */
public interface ILog {

    void d(String tag, String message);

    void d(String tag, String message, Throwable throwable);

    void e(String tag, String message);

    void e(String tag, String message, Throwable throwable);

    void w(String tag, String message);

    void w(String tag, String message, Throwable throwable);

    void i(String tag, String message);

    void i(String tag, String message, Throwable throwable);

    void v(String tag, String message);

    void v(String tag, String message, Throwable throwable);

    void wtf(String tag, String message);

    void wtf(String tag, String message, Throwable throwable);
}
