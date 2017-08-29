/*
 * 文 件 名:  InputStreamReadFormDisk.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月11日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.read;

import com.robin.lazy.logger.LazyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 读取文件流
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class InputStreamReadFormDisk implements ReadFromDisk<InputStream> {

	@Override
	public InputStream readOut(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LazyLogger.e(e, "读取InputStream错误");
		} catch (Exception e) {
			LazyLogger.e(e, "读取InputStream错误");
		}
		return input;
	}
}
