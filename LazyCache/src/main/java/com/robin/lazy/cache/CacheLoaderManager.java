/*
 * 文 件 名:  CacheLoadManager.java
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
import android.graphics.Bitmap;

import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.disk.read.BitmapReadFromDisk;
import com.robin.lazy.cache.disk.read.BytesReadFromDisk;
import com.robin.lazy.cache.disk.read.InputStreamReadFormDisk;
import com.robin.lazy.cache.disk.read.SerializableReadFromDisk;
import com.robin.lazy.cache.disk.read.StringReadFromDisk;
import com.robin.lazy.cache.disk.write.BitmapWriteInDisk;
import com.robin.lazy.cache.disk.write.BytesWriteInDisk;
import com.robin.lazy.cache.disk.write.InputStreamWriteInDisk;
import com.robin.lazy.cache.disk.write.SerializableWriteInDisk;
import com.robin.lazy.cache.disk.write.StringWriteInDisk;
import com.robin.lazy.cache.entity.CacheGetEntity;
import com.robin.lazy.cache.entity.CachePutEntity;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.util.MemoryCacheUtils;
import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.util.IoUtils;
import com.robin.lazy.util.bitmap.ImageDecodingInfo;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 缓存加载管理者
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CacheLoaderManager {

	private volatile static CacheLoaderManager instance;

	private CacheLoaderConfiguration cacheLoaderConfiguration;

	/** 基础的缓存加载任务 */
	private LoadCacheTask cacheTask;

	/** Returns singleton class instance */
	public static CacheLoaderManager getInstance() {
		if (instance == null) {
			synchronized (CacheLoaderManager.class) {
				if (instance == null) {
					instance = new CacheLoaderManager();
				}
			}
		}
		return instance;
	}

	private CacheLoaderManager() {

	}

	/***
	 * 初始化缓存的一些配置
	 *
	 * @param diskCacheFileNameGenerator
	 * @param diskCacheSize 磁盘缓存大小
	 * @param diskCacheFileCount 磁盘缓存文件的最大限度
	 * @param maxMemorySize 内存缓存的大小
	 * @return CacheLoaderConfiguration
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void init(Context context,FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
			int diskCacheFileCount, int maxMemorySize) {
		if (this.cacheLoaderConfiguration != null) {
			this.cacheLoaderConfiguration.close();
			this.cacheLoaderConfiguration = null;
		}
		MemoryCache memoryCache= MemoryCacheUtils.createLruMemoryCache(maxMemorySize);
		cacheLoaderConfiguration = new CacheLoaderConfiguration(context,
				diskCacheFileNameGenerator, diskCacheSize, diskCacheFileCount,
				memoryCache);
		cacheTask = new LoadCacheTask(
				cacheLoaderConfiguration.getLimitAgeDiskCache(),
				cacheLoaderConfiguration.getLimitedAgeMemoryCache());
	}

	/**
	 * 初始化缓存配置
	 *
	 * @param cacheLoaderConfiguration
	 * @return CacheLoaderConfiguration
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void init(CacheLoaderConfiguration cacheLoaderConfiguration) {
		if (this.cacheLoaderConfiguration != null) {
			this.cacheLoaderConfiguration.close();
			this.cacheLoaderConfiguration = null;
		}
		this.cacheLoaderConfiguration = cacheLoaderConfiguration;
		cacheTask = new LoadCacheTask(
				cacheLoaderConfiguration.getLimitAgeDiskCache(),
				cacheLoaderConfiguration.getLimitedAgeMemoryCache());
	}

	/***
	 * 加载缓存中对应的字节数组
	 * @param key
	 * @return
	 * byte[]
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public byte[] loadBytes(String key){
		if(!isInitialize())
			return null;
		CacheGetEntity<byte[]> cacheGetEntity=new CacheGetEntity<byte[]>(new BytesReadFromDisk());
		return cacheTask.query(key, cacheGetEntity);
	}

	/**
	 * 加载Bitmap
	 * @param key
	 * @return
	 * Bitmap
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
    public Bitmap loadBitmap(String key,ImageDecodingInfo imageDecodingInfo){
    	if(!isInitialize())
			return null;
    	CacheGetEntity<Bitmap> cacheGetEntity=new CacheGetEntity<Bitmap>(new BitmapReadFromDisk(imageDecodingInfo));
		return cacheTask.query(key, cacheGetEntity);
	}

    /**
     * 加载String
     * @param key
     * @return 等到缓存数据
     * String
     * @throws
     * @see [类、类#方法、类#成员]
     */
	public String loadString(String key){
		if(!isInitialize())
			return null;
		CacheGetEntity<String> cacheGetEntity=new CacheGetEntity<String>(new StringReadFromDisk());
		return cacheTask.query(key, cacheGetEntity);
	}

	/**
	 * 获取Serializable
	 * @param key
	 * @return
	 * Serializable
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V extends Serializable> V loadSerializable(String key){
		if(!isInitialize())
			return null;
		CacheGetEntity<V> cacheGetEntity=new CacheGetEntity<V>(
				new SerializableReadFromDisk<V>());
		return cacheTask.query(key, cacheGetEntity);
	}

	/**
	 * 加载InputStream
	 * @param key
	 * @return
	 * InputStream
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public InputStream loadInputStream(String key){
		if(!isInitialize())
			return null;
		CacheGetEntity<InputStream> cacheGetEntity=new CacheGetEntity<InputStream>(
				new InputStreamReadFormDisk());
		return cacheTask.query(key, cacheGetEntity);
	}

	/**
	 * save bytes到缓存
	 * @param key
	 * @param value
	 * @param maxLimitTime 缓存期限(单位分钟)
	 * @return
	 * boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public boolean saveBytes(String key,byte[] value,long maxLimitTime){
		if(!isInitialize())
			return false;
		CachePutEntity<byte[]> cachePutEntity=new CachePutEntity<byte[]>(new BytesWriteInDisk());
		return cacheTask.insert(key, cachePutEntity, value, maxLimitTime * 60);
	}

	/**
	 * save Bitmap到缓存
	 * @param key
	 * @param value
	 * @param maxLimitTime 缓存期限(单位分钟)
	 * @param isRecycle 使用玩是否释放回收
	 * @return
	 * boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public boolean saveBitmap(String key,Bitmap value,long maxLimitTime,boolean isRecycle){
		if(!isInitialize())
			return false;
		CachePutEntity<Bitmap> cachePutEntity=new CachePutEntity<Bitmap>(new BitmapWriteInDisk(isRecycle));
		return cacheTask.insert(key, cachePutEntity, value, maxLimitTime * 60);
	}

	/**
	 * save String到缓存
	 * @param key
	 * @param value 要缓存的值
	 * @param maxLimitTime 缓存期限(单位分钟)
	 * @return 是否保存成功
	 * boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public boolean saveString(String key,String value,long maxLimitTime){
		if(!isInitialize())
			return false;
		CachePutEntity<String> cachePutEntity=new CachePutEntity<String>(new StringWriteInDisk());
		return cacheTask.insert(key, cachePutEntity, value, maxLimitTime * 60);
	}

	/**
	 * save Serializable到缓存
	 * @param <V>
	 * @param key
	 * @param values
	 * @param maxLimitTime 缓存期限(单位分钟)
	 * @return
	 * boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public <V extends Serializable> boolean saveSerializable(String key,V values,long maxLimitTime){
		if(!isInitialize())
			return false;
		CachePutEntity<V> cachePutEntity=new CachePutEntity<V>(new SerializableWriteInDisk<V>());
		return cacheTask.insert(key, cachePutEntity, values, maxLimitTime * 60);
	}

    /**
     * save inputStream到缓存
     * @param key
     * @param values
     * @param listener 进度监听器
     * @param maxLimitTime 缓存期限(单位分钟)
     * @return
     */
	public boolean saveInputStream(String key,InputStream values,IoUtils.CopyListener listener,long maxLimitTime){
		if(!isInitialize())
			return false;
		CachePutEntity<InputStream> cachePutEntity=new CachePutEntity<InputStream>(
                new InputStreamWriteInDisk(listener));
		return cacheTask.insert(key, cachePutEntity, values, maxLimitTime * 60);
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
		if(!isInitialize())
			return false;
		return cacheTask.delete(key);
	}

	/**
	 * 获取缓存大小
	 * @return
	 * long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public long size(){
		if(!isInitialize())
			return 0;
		return cacheTask.size();
	}

	/**
	 * 是否初始化
	 * @return
	 * boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	private boolean isInitialize(){
		if(cacheTask==null){
			LazyLogger.e("缓存任务没有初始化");
			return false;
		}
		return true;
	}

	/***
	 * 清理掉当前缓存,可以继续使用
	 *
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void clear() {
		if(cacheTask!=null){
			cacheTask.clear();
		}
	}

	/**
	 * 关闭缓存,关闭后将不能再使用缓存了
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void close(){
		if(cacheTask!=null){
			cacheTask.close();
			cacheTask = null;
		}
		if (cacheLoaderConfiguration != null) {
			cacheLoaderConfiguration.close();
			cacheLoaderConfiguration = null;
		}
		if(instance!=null){
			instance = null;
		}
	}
}
