/*
 * 文 件 名:  WriteInCacheBase.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月9日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.write;

import java.io.OutputStream;

/**
 * 数据写入磁盘缓存方法类
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月9日]
 * @param <V>
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class WriteInDisk<V> {

	/***
	 * 把数据写入OutputStream中
	 * @param value 要存入磁盘中的值
	 * @param out
	 * @return boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public abstract boolean writeIn(OutputStream out,V value);
}
