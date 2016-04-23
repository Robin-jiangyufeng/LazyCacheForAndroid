/*
 * 文 件 名:  EntryRemovedProcessMemoryCache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2016年1月13日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.memory.impl;

import com.robin.lazy.cache.memory.EntryRemovedProcess;

/**
 *可以自定义数据被删除后的垃圾回收处理的缓存
 * 
 * @author jiangyufeng
 * @version [版本号, 2016年1月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EntryRemovedProcessMemoryCache extends LruMemoryCache {

	/**
	 * 垃圾回收处理者
	 */
	@SuppressWarnings("rawtypes")
	private EntryRemovedProcess mEntryRemovedProcess;
	
	public EntryRemovedProcessMemoryCache(int maxSize,
			EntryRemovedProcess<?> entryRemovedProcess) {
		super(maxSize);
		this.mEntryRemovedProcess = entryRemovedProcess;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <V> void entryRemoved(boolean evicted, String key, V oldValue,
			V newValue) {
		if (mEntryRemovedProcess != null) {
			mEntryRemovedProcess.entryRemoved(evicted, key, oldValue, newValue);
		} else {
			super.entryRemoved(evicted, key, oldValue, newValue);
		}
	}
}
