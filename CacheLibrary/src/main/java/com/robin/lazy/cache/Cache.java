/*
 * 文 件 名:  Cache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月11日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache;
/**
 * 缓存接口
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface Cache {
	
	/**
	 * Puts value into cache by key
	 * @param <V>
	 *
	 * @return <b>true</b> - if value was put into cache successfully, <b>false</b> - if value was <b>not</b> put into
	 * cache
	 */
	<V> boolean put(String key, V value);
	
	/**
	 * Puts value into cache by key
	 * @param <V>
	 *@param maxLimitTime 内存缓存数据的有效期(单位秒)
	 * @return <b>true</b> - if value was put into cache successfully, <b>false</b> - if value was <b>not</b> put into
	 * cache
	 */
	<V> boolean put(String key, V value, long maxLimitTime);

	/** 
	 * Returns value by key. If there is no value for key then null will be returned. 
	 * @param <V>*/
	<V> V get(String keyk);

	/** Removes item by key */
	boolean remove(String key);
	
	/***
	 * 重置内存缓存的最大限度大小
	 * @param maxSize
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	void resize(int maxSize);
	
	/**
	 * 关闭缓存，关闭后不能再使用此缓存
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	void close();

	/***
	 * 清理掉当前缓存,对象完全被清理
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	void clear();
}
