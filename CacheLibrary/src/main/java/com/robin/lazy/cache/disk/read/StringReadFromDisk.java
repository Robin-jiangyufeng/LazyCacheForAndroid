/*
 * 文 件 名:  StringDiskFileDecode.java
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
import com.robin.lazy.util.IoUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;

/**
 * 把本地文件解码成String
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StringReadFromDisk implements ReadFromDisk<String> {

	private final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 编码类型
	 */
	private String responseCharset = DEFAULT_CHARSET;

	@Override
	public String readOut(File File) {
		String s = null;
		BufferedReader read = null;
		try {
			// 创建字符流缓冲区
			read = new BufferedReader(new InputStreamReader(
					new FileInputStream(File), responseCharset));// 缓冲
			StringBuffer stBuffer = new StringBuffer();
			String temp = null;
			while ((temp = read.readLine()) != null) {
				stBuffer.append(temp);
			}
			s = stBuffer.toString();
		} catch (StreamCorruptedException e) {
			LazyLogger.e(e, "读取String错误");
		} catch (IOException e) {
			LazyLogger.e(e, "读取String错误");
		} finally {
			IoUtils.closeSilently(read);
		}
		return s;
	}
	/***
	 * 设置String的编码类型
	 * 
	 * @param responseCharset
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}

}
