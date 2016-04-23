/*
 * 文 件 名:  DiskFileDecode.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月11日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.read;

import java.io.File;

/**
 * 读取磁盘缓存方法接口
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @param <V>
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ReadFromDisk<V> {

	/***
	 * 读文文件中内容
	 * @param file
	 * @return
	 * V
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	V readOut(File file);
}
