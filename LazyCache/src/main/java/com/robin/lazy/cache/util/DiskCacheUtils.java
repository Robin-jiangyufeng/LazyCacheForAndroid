/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.robin.lazy.cache.util;

import android.content.Context;

import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.impl.BaseDiskCache;
import com.robin.lazy.cache.disk.impl.ext.LruDiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.disk.naming.HashCodeFileNameGenerator;
import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.util.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * 磁盘缓存相关工具
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class DiskCacheUtils {

	private DiskCacheUtils() {
	}

	/**
	 * 创建一个文件名称转换器
	 * @return
	 */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/**
	 * Creates default implementation of {@link DiskCache} depends on incoming
	 * parameters
	 * @param diskCacheSize 可使用磁盘最大大小(单位byte,字节)
	 */
	public static DiskCache createDiskCache(Context context,
			FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
			int diskCacheFileCount) {
		File reserveCacheDir = createReserveDiskCacheDir(context);
		if (diskCacheSize > 0 || diskCacheFileCount > 0) {
			File individualCacheDir = StorageUtils
					.getIndividualCacheDirectory(context);
			try {
				return new LruDiskCache(individualCacheDir, reserveCacheDir,
						diskCacheFileNameGenerator, diskCacheSize,
						diskCacheFileCount);
			} catch (IOException e) {
				LazyLogger.e(e, "获取LruDiskCache错误");
				// continue and create unlimited cache
			}
		}
		File cacheDir = StorageUtils.getCacheDirectory(context);
		return new BaseDiskCache(cacheDir, reserveCacheDir,
				diskCacheFileNameGenerator);
	}

	/**
	 * Creates reserve disk cache folder which will be used if primary disk
	 * cache folder becomes unavailable
	 */
	private static File createReserveDiskCacheDir(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context, false);
		File individualDir = new File(cacheDir, "uil-cache");
		if (individualDir.exists() || individualDir.mkdir()) {
			cacheDir = individualDir;
		}
		return cacheDir;
	}

	/**
	 * Returns {@link File} of cached key or <b>null</b> if key was not cached
	 * in disk cache
	 */
	public static File findInCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() ? file : null;
	}

	/**
	 * Removed cached key file from disk cache (if key was cached in disk cache
	 * before)
	 *
	 * @return <b>true</b> - if cached key file existed and was deleted;
	 *         <b>false</b> - otherwise.
	 */
	public static boolean removeFromCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() && file.delete();
	}
}
