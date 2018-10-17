/*
 * 文 件 名:  BytesReadFromDisk.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月16日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.read;

import com.robin.lazy.cache.util.log.CacheLog;
import com.robin.lazy.util.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 从文件中读取byte数组
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月16日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BytesReadFromDisk implements ReadFromDisk<byte[]> {
	
	private final static String LOG_TAG=BytesReadFromDisk.class.getSimpleName();

	private static final int DEFAULT_BUFFER_SIZE = 1 * 1024; // 4 Kb

	/** 缓冲流大小 */
	private int bufferSize = DEFAULT_BUFFER_SIZE;

	@Override
	public byte[] readOut(File file) {
		byte[] bytes = null;
		InputStream input = null;
		ByteArrayOutputStream baos = null;
		try {
			byte[] tmp = new byte[bufferSize];
			input = new FileInputStream(file);
			baos = new ByteArrayOutputStream();
			int size = 0;
			while ((size = input.read(tmp)) != -1) {
				baos.write(tmp, 0, size);
			}
			bytes = baos.toByteArray();
		} catch (FileNotFoundException e) {
			CacheLog.e(LOG_TAG,"从文件读取byte字节数组失败,没有找到文件",e);
		} catch (IOException e) {
			CacheLog.e(LOG_TAG, "从文件读取byte字节数组失败",e);
		} catch (Exception e) {
			CacheLog.e(LOG_TAG, "从文件读取byte字节数组失败",e);
		} finally {
			IoUtils.closeSilently(input);
			IoUtils.closeSilently(baos);
		}
		return bytes;
	}

	/**
	 * 设置缓冲流大小
	 * 
	 * @param bufferSize
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}
