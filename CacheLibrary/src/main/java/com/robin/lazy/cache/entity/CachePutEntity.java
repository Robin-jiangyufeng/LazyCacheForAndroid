/*
 * 文 件 名:  InsertCacheEntity.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.entity;

import com.robin.lazy.cache.disk.write.WriteInDisk;

/**
 * 缓存存储参数实体
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月14日]
 * @param <V>
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CachePutEntity<V> {

	private WriteInDisk<V> writeInDisk;
	
	public CachePutEntity(WriteInDisk<V> writeInDisk) {
		this.writeInDisk = writeInDisk;
	}

	/**
	 * 获取磁盘缓存写入方式接口
	 * @return
	 * WriteInDisk<V>
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public WriteInDisk<V> getWriteInDisk() {
		return writeInDisk;
	}

	/***
	 * 设置磁盘缓存写入方式接口
	 * @param writeInDisk
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setWriteInDisk(WriteInDisk<V> writeInDisk) {
		this.writeInDisk = writeInDisk;
	}
}
