/**
 * 文 件 名:  LimitedAgePropertiesConfig.java
 * 版    权:  Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  江钰锋 00501
 * 修改时间:  16/8/9
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 时间缓存期限配置文件
 *
 * @author 江钰锋 00501
 * @version [版本号, 16/8/9]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LimitedAgePropertiesConfig {
    private Properties mProperties;
    private File file;

    public LimitedAgePropertiesConfig(File fileDirectory) {
        file = new File(fileDirectory, "limitedage.properties");
    }

    /**
     * 设置long类型配置
     *
     * @param key
     * @param value
     */
    public void setLong(String key, long value) {
        if (value > 0) {
            setString(key, String.valueOf(value));
        }
    }

    /**
     * 获取long类型配置数据
     *
     * @param key
     * @param defaultVales
     * @return
     */
    public long getLong(String key, long defaultVales) {
        try {
            return Long.parseLong(getString(key, String.valueOf(defaultVales)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultVales;
    }

    /**
     * 设置sting类型配置
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        if (value != null) {
            Properties props = getProperties();
            props.setProperty(key, value);
            setProperties(props);
        }
    }

    /***
     * 获取对应的配置
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getString(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    /**
     * 删除对应的配置
     *
     * @param key
     */
    public void remove(String key) {
        Properties props = getProperties();
        props.remove(key);
        setProperties(props);
    }

    /**
     * 返回配置关于配置实体
     *
     * @return 返回配置实体
     */
    private Properties getProperties() {
        if (mProperties == null) {
            mProperties = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                mProperties.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return mProperties;
    }

    /**
     * 设置配置的内容
     *
     * @param properties
     */
    private void setProperties(Properties properties) {
        if (properties == null) return;
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            properties.store(out, null);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清理
     */
    public void clear() {
        Properties props = getProperties();
        props.clear();
        setProperties(props);
    }

}
