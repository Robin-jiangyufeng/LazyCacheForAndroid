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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.robin.lazy.cache.memory.EntryRemovedProcess;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.memory.SizeOfCacheCalculator;
import com.robin.lazy.cache.memory.impl.EntryRemovedProcessMemoryCache;
import com.robin.lazy.cache.memory.impl.LruMemoryCache;
import com.robin.lazy.cache.memory.impl.SizeOfMemoryCache;
import com.robin.lazy.util.bitmap.ImageSize;

/**
 * Utility for generating of keys for memory cache, key comparing and other work
 * with memory cache
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.6.3
 */
public final class MemoryCacheUtils {

	private static final String URI_AND_SIZE_SEPARATOR = "_";
	private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

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
	 * Generates key for memory cache for incoming image (URI + size).<br />
	 * Pattern for cache key - <b>[imageUri]_[width]x[height]</b>.
	 */
	public static String generateKey(String imageUri, ImageSize targetSize) {
		return new StringBuilder(imageUri).append(URI_AND_SIZE_SEPARATOR)
				.append(targetSize.getWidth())
				.append(WIDTH_AND_HEIGHT_SEPARATOR)
				.append(targetSize.getHeight()).toString();
	}

	public static Comparator<String> createFuzzyKeyComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String k1, String k2) {
				String key1 = k1.substring(0,
						k1.lastIndexOf(URI_AND_SIZE_SEPARATOR));
				String key2 = k2.substring(0,
						k2.lastIndexOf(URI_AND_SIZE_SEPARATOR));
				return key1.compareTo(key2);
			}
		};
	}

	/**
	 * Searches all bitmaps in memory cache which are corresponded to incoming
	 * URI.<br />
	 * <b>Note:</b> Memory cache can contain multiple sizes of the same key if
	 * only you didn't set
	 * @param <V>
	 */
	public static <V> List<V> findCachedBitmapsForkey(String key,
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
	 * Searches all keys in memory cache which are corresponded to incoming URI.<br />
	 * <b>Note:</b> Memory cache can contain multiple sizes of the same key if
	 * only you didn't set
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
	 * Removes from memory cache all keys for incoming URI.<br />
	 * <b>Note:</b> Memory cache can contain multiple sizes of the same key if
	 * only you didn't set
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
