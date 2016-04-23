/*
 * 文 件 名:  SizeOfMemoryCache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月17日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.memory.impl;

import com.robin.lazy.cache.memory.SizeOfCacheCalculator;

/**
 * 重新计算每个缓存数据占的大小的缓存类
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月17日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SizeOfMemoryCache extends LruMemoryCache {

	/** 一个缓存占用的内存计算器 */
	@SuppressWarnings("rawtypes")
	private SizeOfCacheCalculator cacheCalculator;

	public SizeOfMemoryCache(int maxSize, SizeOfCacheCalculator<?> cacheCalculator) {
		super(maxSize);
		this.cacheCalculator = cacheCalculator;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <V> int sizeOf(String key, V value) {
		if (key != null && value == null) {
			return super.sizeOf(key, value);
		} else if (key == null && value == null) {
			return 0;
		}
		if (cacheCalculator != null) {
			return cacheCalculator.sizeOf(key, value);
		}
		return super.sizeOf(key, value);
	}
}
