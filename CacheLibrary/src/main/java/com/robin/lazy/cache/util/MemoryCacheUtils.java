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

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.robin.lazy.cache.memory.EntryRemovedProcess;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.memory.SizeOfCacheCalculator;
import com.robin.lazy.cache.memory.impl.EntryRemovedProcessMemoryCache;
import com.robin.lazy.cache.memory.impl.LruMemoryCache;
import com.robin.lazy.cache.memory.impl.SizeOfMemoryCache;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存缓存工具
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class MemoryCacheUtils {

	private MemoryCacheUtils() {
	}

	/**
	 * 创建一个有固定大小的内存缓存操作工具
	 * 
	 * @param memoryCacheSize
	 *            内存缓存的最大限度(单位兆MB)
	 * @param calculator
	 *            单个内存缓存占用内存大小计算器
	 * @return SizeOfMemoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public static SizeOfMemoryCache createMemoryCache(
			int memoryCacheSize, SizeOfCacheCalculator<?> calculator) {
		if (memoryCacheSize <= 0) {
			new NullPointerException("memoryCacheSize小于0");
		}
		return new SizeOfMemoryCache(memoryCacheSize, calculator);
	}

	/**
	 * 创建一个有固定大小的内存缓存操作工具
	 * 
	 * @param context
	 * @param ratio
	 *            占当前可用内存的比例(最大为1)
	 * @param calculator
	 *            单个内存缓存占用内存大小计算器
	 * @return SizeOfMemoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public static SizeOfMemoryCache createMemoryCache(Context context,
			float ratio, SizeOfCacheCalculator<?> calculator) {
		int memoryCacheSize = 0;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();// 获取可用内存大小(单位MB)
		if (hasHoneycomb() && isLargeHeap(context)) {
			memoryClass = getLargeMemoryClass(am);
		}
		memoryCacheSize = (int) (1024 * 1024 * memoryClass * ratio);
		return new SizeOfMemoryCache(memoryCacheSize, calculator);
	}
	
	/**
	 * 创建能够自定义处理被回收的数据的内存缓存类
	 * 
	 * @param maxSize 可以储存的缓存数据数量最大上限(单位未知)
	 * @param entryRemovedProcess 删除处理者
	 * @return LruMemoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public static EntryRemovedProcessMemoryCache createLruMemoryCache(int maxSize,
			EntryRemovedProcess<?> entryRemovedProcess) {
		return new EntryRemovedProcessMemoryCache(maxSize,entryRemovedProcess);
	}

	/**
	 * 创建默认的Lru内存缓存类
	 * 
	 * @param maxSize
	 *            可以储存的缓存数据数量最大上限(单位未知)
	 * @return LruMemoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public static LruMemoryCache createLruMemoryCache(int maxSize) {
		return new LruMemoryCache(maxSize);
	}

	private static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static boolean isLargeHeap(Context context) {
		return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
	}

	/**
	 * 获取可用内存大小(单位MB)
	 * 
	 * @param am
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static int getLargeMemoryClass(ActivityManager am) {
		return am.getLargeMemoryClass();
	}


	/**
	 * 根据key查找对应的数据
	 * @param key
	 * @param memoryCache
	 * @param <V>
	 * @return
	 */
	public static <V> List<V> findCachedForkey(String key,
			MemoryCache memoryCache) {
		List<V> values = new ArrayList<V>();
		for (String k : memoryCache.keys()) {
			if (k.startsWith(key)) {
				V value = memoryCache.get(k);
				values.add(value);
			}
		}
		return values;
	}

	/**
	 * 根据key查找对应的数据
	 * @param key
	 * @param memoryCache
	 * @param <V>
	 * @return
	 */
	public static List<String> findCacheKeysForkey(String key,
			MemoryCache memoryCache) {
		List<String> values = new ArrayList<String>();
		for (String k : memoryCache.keys()) {
			if (k.startsWith(key)) {
				values.add(k);
			}
		}
		return values;
	}

	/**
	 * 根据key删除对应的数据
	 * @param key
	 * @param memoryCache
	 */
	public static void removeFromCache(String key, MemoryCache memoryCache) {
		List<String> keysToRemove = new ArrayList<String>();
		for (String k : memoryCache.keys()) {
			if (k.startsWith(key)) {
				keysToRemove.add(k);
			}
		}
		for (String keyToRemove : keysToRemove) {
			memoryCache.remove(keyToRemove);
		}
	}
}
