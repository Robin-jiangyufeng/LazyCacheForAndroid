/*
 * 文 件 名:  CacheLoadConfiguration.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月15日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache;

import android.content.Context;

import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.impl.LimitedAgeDiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.memory.impl.LimitedAgeMemoryCache;
import com.robin.lazy.cache.util.DiskCacheUtils;

/**
 * 缓存加载配置
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CacheLoaderConfiguration {

	/** 默认的过期时间(单位分钟) */
	private final static long MAX_LIMIT_TIEM_DEFAULT = 5;

	/**
	 * 磁盘缓存类
	 */
	private DiskCache diskCache;

	/***
	 * 内存缓存类
	 */
	private MemoryCache memoryCache;

	/** 缓存默认有效期(单位分钟) */
	private long maxAge = MAX_LIMIT_TIEM_DEFAULT;

	/**
	 * 
	 * @param diskCacheFileNameGenerator
	 * @param diskCacheSize  磁盘缓存大小 (单位字节byte)
	 * @param diskCacheFileCount 磁盘缓存文件的最大限度
	 * @param memoryCache  内存缓存
	 * @param maxAge 有效期(单位分钟)
	 */
	public CacheLoaderConfiguration(Context context,
			FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
			int diskCacheFileCount, MemoryCache memoryCache, long maxAge) {
		this(context, diskCacheFileNameGenerator, diskCacheSize,
				diskCacheFileCount, memoryCache);
		this.maxAge = maxAge;
	}

	/**
	 * <默认构造函数>
	 * 
	 * @param diskCacheFileNameGenerator
	 * @param diskCacheSize 磁盘缓存大小(单位字节byte)
	 * @param diskCacheFileCount 磁盘缓存文件的最大限度
	 * @param memoryCache 内存缓存
	 */
	public CacheLoaderConfiguration(Context context,
			FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
			int diskCacheFileCount, MemoryCache memoryCache) {
		diskCache = DiskCacheUtils.createDiskCache(context,
				diskCacheFileNameGenerator, diskCacheSize, diskCacheFileCount);
		this.memoryCache = memoryCache;
	}

	/***
	 * 获取基本类型的磁盘缓存
	 * 
	 * @return DiskCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public DiskCache getDiskCache() {
		return diskCache;
	}

	/**
	 * 获取有缓存有有限期的磁盘缓存
	 *
	 * @return DiskCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public DiskCache getLimitAgeDiskCache() {
		if (maxAge > 0) {
			return new LimitedAgeDiskCache(diskCache, maxAge * 60);
		}
		return new LimitedAgeDiskCache(diskCache, MAX_LIMIT_TIEM_DEFAULT * 60);
	}

	/***
	 * 设置磁盘缓存
	 * 
	 * @param diskCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setDiskCache(DiskCache diskCache) {
		this.diskCache = diskCache;
	}

	/***
	 * 获取基本类型内存缓存
	 * 
	 * @return MemoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public MemoryCache getMemoryCache() {
		return memoryCache;
	}

	/***
	 * 获取缓存有过期时间的缓存加载工具
	 * 
	 * @return MemoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public MemoryCache getLimitedAgeMemoryCache() {
		if (maxAge > 0) {
			return new LimitedAgeMemoryCache(memoryCache, maxAge * 60);
		}
		return new LimitedAgeMemoryCache(memoryCache,
				MAX_LIMIT_TIEM_DEFAULT * 60);
	}

	/***
	 * 设置内存缓存
	 * 
	 * @param memoryCache
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setMemoryCache(MemoryCache memoryCache) {
		this.memoryCache = memoryCache;
	}
	
	/**
	 * 清理 void
	 * 
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void close() {
		if (diskCache != null) {
			diskCache.close();
			diskCache = null;
		}
		if (memoryCache != null) {
			memoryCache.close();
			memoryCache = null;
		}
	}


}
