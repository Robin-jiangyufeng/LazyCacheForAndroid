/*
 * 文 件 名:  InputStreamWriteInCache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月9日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.write;


import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.util.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * InputStream写入缓存
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class InputStreamWriteInDisk extends WriteInDisk<InputStream> {

	private static final int DEFAULT_BUFFER_SIZE = 4 * 1024; // 4 Kb

	/** 缓冲流大小 */
	private int bufferSize = DEFAULT_BUFFER_SIZE;

	private IoUtils.CopyListener mListener;

	public InputStreamWriteInDisk(IoUtils.CopyListener listener) {
		this.mListener = listener;
	}

	@Override
	public boolean writeIn(OutputStream out,InputStream values) {
		boolean isSucess = false;
		try {
			isSucess = IoUtils.copyStream(values, out, mListener,
					bufferSize);
			out.flush();
		} catch (IOException e) {
			LazyLogger.e(e, "InputStream写入缓存错误");
		} catch (Exception e) {
			LazyLogger.e(e, "InputStream写入缓存错误");
		} finally {
			IoUtils.closeSilently(out);
		}
		return isSucess;

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
