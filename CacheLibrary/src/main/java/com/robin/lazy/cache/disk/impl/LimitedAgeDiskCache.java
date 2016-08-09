/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.robin.lazy.cache.disk.impl;

import com.robin.lazy.cache.LimitedAge;
import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.read.ReadFromDisk;
import com.robin.lazy.cache.disk.write.WriteInDisk;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件有过期时间的磁盘缓存实现
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LimitedAgeDiskCache implements DiskCache {
    /**
     * 时间期限配置文件操作类
     */
    private LimitedAgePropertiesConfig limitedAgePropertiesConfig;
    private DiskCache mDiskCache;
    private final Map<File, LimitedAge> loadingDates = Collections
            .synchronizedMap(new HashMap<File, LimitedAge>());

    private long maxAge;

    /**
     * @param diskCache 磁盘缓存类
     * @param maxAge    缓存数据最大期限(单位秒)
     */
    public LimitedAgeDiskCache(DiskCache diskCache, long maxAge) {
        this.mDiskCache = diskCache;
        this.maxAge = maxAge;
        limitedAgePropertiesConfig = new LimitedAgePropertiesConfig(getDirectory());
    }

    @Override
    public File getDirectory() {
        if (mDiskCache == null) {
            return null;
        }
        return mDiskCache.getDirectory();
    }

    @Override
    public File getFile(String key) {
        if (mDiskCache == null) {
            return null;
        }
        File file = mDiskCache.getFile(key);
        if (file != null && file.exists()) {
            boolean cached;
            LimitedAge loadingDate = loadingDates.get(file);
            if (loadingDate == null) {
                cached = false;
                long savaMaxAge = limitedAgePropertiesConfig.getLong(key, 0);
                if (savaMaxAge <= 0) {
                    savaMaxAge = maxAge;
                }
                loadingDate = new LimitedAge(file.lastModified(), savaMaxAge);
            } else {
                cached = true;
            }

            if (loadingDate.checkExpire()) {
                loadingDates.remove(file);
                limitedAgePropertiesConfig.remove(key);
                mDiskCache.remove(key);
                file.delete();
            } else if (!cached) {
                loadingDates.put(file, loadingDate);
            }
        }
        return file;
    }

    @Override
    public <V> V get(String key, ReadFromDisk<V> readFromDisk) {
        if (mDiskCache == null) {
            return null;
        }
        File file = getFile(key);
        if (file == null || !file.exists()) {
            return null;
        }
        return mDiskCache.get(key, readFromDisk);
    }

    @Override
    public <V> boolean put(String key, WriteInDisk<V> writeIn, V value)
            throws IOException {
        if (mDiskCache == null) {
            return false;
        }
        boolean saved = mDiskCache.put(key, writeIn, value);
        if (loadingDates.get(key) == null) {
            rememberUsage(key, maxAge);
        }
        return saved;
    }

    @Override
    public <V> boolean put(String key, WriteInDisk<V> writeIn, V value,
                           long maxLimitTime) throws IOException {
        if (mDiskCache == null) {
            return false;
        }
        boolean saved = mDiskCache.put(key, writeIn, value, maxLimitTime);
        rememberUsage(key, maxLimitTime);
        return saved;
    }

    @Override
    public long size() {
        if (mDiskCache == null) {
            return 0;
        }
        return mDiskCache.size();
    }

    @Override
    public boolean remove(String key) {
        if (mDiskCache == null) {
            return false;
        }
        loadingDates.remove(getFile(key));
        limitedAgePropertiesConfig.remove(key);
        return mDiskCache.remove(key);
    }

    @Override
    public void clear() {
        if (mDiskCache != null) {
            mDiskCache.clear();
        }
        if (loadingDates != null) {
            loadingDates.clear();
        }
        if (limitedAgePropertiesConfig != null) {
            limitedAgePropertiesConfig.clear();
        }
    }

    @Override
    public void close() {
        if (mDiskCache != null) {
            mDiskCache.close();
            mDiskCache = null;
        }
        if (loadingDates != null) {
            loadingDates.clear();
        }
        limitedAgePropertiesConfig = null;
    }

    /**
     * 记录数据有效时间
     *
     * @param key
     * @param mMaxlimitTime void
     * @throws
     * @see [类、类#方法、类#成员]
     */
    private void rememberUsage(String key, long mMaxlimitTime) {
        File file = getFile(key);
        long currentTime = System.currentTimeMillis();
        file.setLastModified(currentTime);
        limitedAgePropertiesConfig.setLong(key, mMaxlimitTime);
        loadingDates.put(file, new LimitedAge(currentTime, mMaxlimitTime));
    }

    /**
     * 获取数据剩余有效时间(单位秒)
     *
     * @return long
     * @throws
     * @see [类、类#方法、类#成员]
     */
    public long getLimitedTime(String key) {
        File file = getFile(key);
        if (loadingDates != null && loadingDates.containsKey(file)) {
            LimitedAge limitedAge = loadingDates.get(file);
            if (limitedAge != null) {
                return limitedAge.limitedTime();
            }
        }
        return 0;
    }

}