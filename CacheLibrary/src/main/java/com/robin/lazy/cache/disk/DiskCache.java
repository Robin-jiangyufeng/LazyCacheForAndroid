/*
 * 文 件 名:  DiskCache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月11日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.robin.lazy.cache.disk;


import java.io.File;
import java.io.IOException;

import com.robin.lazy.cache.disk.read.ReadFromDisk;
import com.robin.lazy.cache.disk.write.WriteInDisk;

/**
 * 磁盘缓存接口
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface DiskCache {

	/**
	 * 获取磁盘缓存的根目录
	 *
	 * @return 磁盘缓存的根目录
	 */
	File getDirectory();
	
	/***
	 * 获取当前磁盘缓存大小(单位byte字节)
	 * @return
	 * long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	long size();

	/**
	 * 根据key获取缓存文件
	 *
	 * @param key 缓存数据的key
	 * @return 缓存文件(不存在则返回null)
	 */
	File getFile(String key);

	/**
	 * 查询一条缓存数据
	 * 
	 * @param key
	 *            缓存数据的标识
	 * @param readFromDisk
	 * @return V
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	<V> V get(String key, ReadFromDisk<V> readFromDisk);

	/**
	 * 保存数据到磁盘缓存中
	 * 
	 * @param <V>
	 * @param key 缓存数据对应的key
	 * @param writeIn 写数据到磁盘的工具
	 * @param value 缓存数据
	 * @return 是否保存成功
	 * @throws IOException
	 */
	<V> boolean put(String key, WriteInDisk<V> writeIn, V value)
			throws IOException;

	/**
	 * 保存数据到磁盘缓存中(带有缓存期限的)
	 * 
	 * @param <V>
	 * @param key 缓存数据对应的key
	 * @param writeIn 写数据到磁盘的工具
	 * @param value 缓存数据
	 * @param maxLimitTime
	 *            缓存有效期(单位秒)
	 * @return 是否保存成功
	 * @throws IOException
	 */
	<V> boolean put(String key, WriteInDisk<V> writeIn, V value,
					long maxLimitTime) throws IOException;

	/**
	 * 根据key删除缓存文件
	 *
	 * @param key 缓存数据对应的key
	 * @return 是否删除成功
	 */
	boolean remove(String key);

	/** 关闭缓存(执行此方法后就不能再进行缓存读写操作). */
	void close();

	/** 清理掉所有缓存数据. */
	void clear();
}
