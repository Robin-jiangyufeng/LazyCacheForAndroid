/*
 * 文 件 名:  CacheGetEntity.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月16日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.entity;

import com.robin.lazy.cache.disk.read.ReadFromDisk;

/**
 * 缓存请求参数实体
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月16日]
 * @param <V>
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CacheGetEntity<V> {
	/***
	 * 磁盘缓存读取
	 */
	private ReadFromDisk<V> readFromDisk;

	/**
	 * <默认构造函数>
	 */
	public CacheGetEntity(ReadFromDisk<V> readFromDisk) {
		this.readFromDisk = readFromDisk;
	}

	public ReadFromDisk<V> getReadFromDisk() {
		return readFromDisk;
	}

	public void setReadFromDisk(ReadFromDisk<V> readFromDisk) {
		this.readFromDisk = readFromDisk;
	}
	
	
}
