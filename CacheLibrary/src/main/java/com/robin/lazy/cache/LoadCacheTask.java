/*
 * 文 件 名:  LoadCacheTask.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月15日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache;

import android.text.TextUtils;

import com.robin.lazy.logger.LazyLogger;

import java.io.File;
import java.io.IOException;

import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.impl.LimitedAgeDiskCache;
import com.robin.lazy.cache.entity.CacheGetEntity;
import com.robin.lazy.cache.entity.CachePutEntity;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.process.CacheDataProcess;

/**
 * 基本的加载缓存任务
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LoadCacheTask {

	/**
	 * 磁盘缓存类
	 */
	private DiskCache diskCache;

	/***
	 * 内存缓存类
	 */
	private MemoryCache memoryCache;

	public LoadCacheTask(DiskCache diskCache, MemoryCache memoryCache) {
		this.diskCache = diskCache;
		this.memoryCache = memoryCache;
	}

	/**
	 * 插入一条缓存数据
	 * 
	 * @param key
	 * @param cachePutEntity
	 * @param value 要存入缓存的数据
	 * @return boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V> boolean insert(String key, CachePutEntity<V> cachePutEntity,V value) {
		return insert(key, cachePutEntity,value, -1);
	}
	
	/***
	 * 插入一条缓存数据
	 * 
	 * @param <V>
	 * @param key 数据标识
	 * @param cachePutEntity
	 * @param value 要存入缓存的数据
	 * @param insertCacheProcess 对插入的数据进行处理的类(处理后在插入缓存)
	 * @param maxLimitTime 有效时间(单位秒)
	 * @return 插入是否成功 boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V> boolean insert(String key, CachePutEntity<V> cachePutEntity,V value,
			CacheDataProcess<V> insertCacheProcess,long maxLimitTime) {
		return insert(key, cachePutEntity,insertCacheProcess.process(value), maxLimitTime);
	}

	/***
	 * 插入一条缓存数据
	 * 
	 * @param <V>
	 * @param key 数据标识
	 * @param cachePutEntity
	 * @param value 要存入缓存的数据
	 * @param maxLimitTime 有效时间(单位秒)
	 * @return 插入是否成功 boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V> boolean insert(String key, CachePutEntity<V> cachePutEntity,
			V value, long maxLimitTime) {
		if (diskCache == null || memoryCache == null || cachePutEntity == null) {
			LazyLogger.e(new NullPointerException(),
					"diskCache or memoryCache or cachePutEntity is null");
			return false;
		}
		boolean isSuccess = false;
		try {
//			V val = memoryCache.get(key);
//			if (val != null) {
//				boolean isExist = ObjectUtils.isEquals(val, value);// 判断缓存中是否以存在该数据
//				if (isExist) {
//					// 当前内存缓存中已经存在这个数据
//					return true;
//				} else {
//					memoryCache.remove(key);
//				}
//			}
			if (maxLimitTime > 0) {
				isSuccess = memoryCache.put(key, value, maxLimitTime);
			} else {
				isSuccess = memoryCache.put(key, value);
			}
			//以下是磁盘缓存
			if(isSuccess){
				File file = diskCache.getFile(key);
				if (file != null && file.exists()) {
					diskCache.remove(key);
				}
				if (maxLimitTime > 0) {
					isSuccess = diskCache.put(key, cachePutEntity.getWriteInDisk(),
							value, maxLimitTime);
				} else {
					isSuccess = diskCache.put(key, cachePutEntity.getWriteInDisk(),
							value);
				}	
			}
		} catch (IOException e) {
			LazyLogger.e(e, "文件写入磁盘失败");
		} catch (Exception e) {
			LazyLogger.e(e, "文件写入磁盘失败");
		}
		return isSuccess;
	}
	
	/***
	 * 查询一条缓存数据
	 * 
	 * @param <V>
	 * @param key
	 * @param cacheGetEntity
	 * @param queryDataProcess 对于查询到的数据进行处理的方法(从缓存中取道数据后再进行加工处理后给使用者)
	 * @return V
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V> V query(String key, CacheGetEntity<V> cacheGetEntity,CacheDataProcess<V> queryDataProcess) {
		return queryDataProcess.process(query(key, cacheGetEntity));
	}

	/***
	 * 查询一条缓存数据
	 * 
	 * @param <V>
	 * @param key
	 * @param cacheGetEntity
	 * @return V
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V> V query(String key, CacheGetEntity<V> cacheGetEntity) {
		if (diskCache == null || memoryCache == null || cacheGetEntity == null) {
			LazyLogger.e(new NullPointerException(),
					"diskCache or memoryCache or cacheGetEntity is null");
			return null;
		}
		V value = null;
		try {
			value = memoryCache.get(key);
			if (value != null) {
				return value;
			}
			value = diskCache.get(key, cacheGetEntity.getReadFromDisk());
			if (value != null) {
				if(diskCache instanceof LimitedAgeDiskCache){
					LimitedAgeDiskCache  limitedAgeDiskCache=(LimitedAgeDiskCache)diskCache;
					long limitedTime=limitedAgeDiskCache.getLimitedTime(key);
					if(limitedTime>0){
						memoryCache.put(key, value, limitedTime);
					}
				}else{
					memoryCache.put(key, value);
				}
			}
		} catch (Exception e) {
			LazyLogger.e(e, "缓存缓存数据错误");
		}
		return value;
	}
	
	/***
	 * 删除一条缓存数据
	 * 
	 * @param key 数据标识
	 * @return 是否删除成功 boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public boolean delete(String key) {
		if (diskCache == null || memoryCache == null) {
			LazyLogger.e(new NullPointerException(),
					"diskCache or memoryCache or cachePutEntity is null");
			return false;
		}
		boolean isSuccess = false;
		if (memoryCache.keys().contains(key)) {
			isSuccess = memoryCache.remove(key);
		}
		if (isSuccess) {
			File file = diskCache.getFile(key);
			if (file != null && file.exists()) {
				isSuccess = diskCache.remove(key);
			}
		}
		return isSuccess;
	}
	
	/***
	 * 计算缓存大小
	 * @return
	 * long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public long size(){
		if (diskCache == null || memoryCache == null) {
			LazyLogger.e(new NullPointerException(),
					"diskCache or memoryCache or cachePutEntity is null");
			return 0;
		}
		String mapString=memoryCache.snapshot().toString();
		long memoryCacheSize = TextUtils.isEmpty(mapString)?0:mapString.getBytes().length;
		return diskCache.size()+memoryCacheSize;
	}

	public DiskCache getDiskCache() {
		return diskCache;
	}

	public void setDiskCache(DiskCache diskCache) {
		this.diskCache = diskCache;
	}

	public MemoryCache getMemoryCache() {
		return memoryCache;
	}

	public void setMemoryCache(MemoryCache memoryCache) {
		this.memoryCache = memoryCache;
	}

	/**
	 * 关闭缓存，对象完全被清理，关闭后不能再使用此缓存 void
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

	/***
	 * 清理掉当前缓存
	 * 
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void clear() {
		if (diskCache != null) {
			diskCache.clear();
		}
		if (memoryCache != null) {
			memoryCache.clear();
		}
	}

}
