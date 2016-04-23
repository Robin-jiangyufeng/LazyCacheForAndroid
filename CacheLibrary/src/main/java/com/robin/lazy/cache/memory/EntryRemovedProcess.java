/*
 * 文 件 名:  EntryRemovedProcess.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2016年1月13日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.memory;
/**
 * 数据被删除后进行垃圾回收的一些处理
 * 
 * @author  jiangyufeng
 * @version  [版本号, 2016年1月13日]
 * @param <V>
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class EntryRemovedProcess<V> {

	/***
	 * 当item被回收或者删掉时调用。改方法当value被回收释放存储空间时被remove调用， 
	 * 或者替换item值时put调用，默认实现什么都没做。
	 * 
	 * @param <V>
	 * @param evicted 是否释放被删除的空间
	 * @param key
	 * @param oldValue 老的数据
	 * @param newValue 新数据 void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void entryRemoved(boolean evicted, String key, V oldValue,
			V newValue) {
		oldValue = null;
	};
}

