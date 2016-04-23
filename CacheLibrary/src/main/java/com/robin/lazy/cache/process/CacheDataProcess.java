/*
 * 文 件 名:  CacheDataProcess.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.process;
/**
 * 缓存数据加工接口
 * 
 * @author  jiangyufeng
 * @version  [版本号, 2015年12月14日]
 * @param <V>范型
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface CacheDataProcess<V> {

	/**
	 * 加工缓存的数据
	 * @param data
	 * @return 返回加工处理后的数据
	 * V
	 * @see [类、类#方法、类#成员]
	 */
	V process(V data);
}

