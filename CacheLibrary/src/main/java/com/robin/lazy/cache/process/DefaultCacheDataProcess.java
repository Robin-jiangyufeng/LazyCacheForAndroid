/*
 * 文 件 名:  DefaultCacheDataProcess.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2016年3月17日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.process;
/**
 * 默认的缓存数据加工处理器
 * 
 * @author  jiangyufeng
 * @version  [版本号, 2016年3月17日]
 * @param <V>
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DefaultCacheDataProcess<V> implements CacheDataProcess<V>{

	@Override
	public V process(V data) {
		return data;
	}

}

