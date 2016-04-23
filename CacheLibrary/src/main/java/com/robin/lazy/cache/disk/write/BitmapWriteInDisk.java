/*
 * 文 件 名:  BitmapWriteInCache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月9日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.write;

import android.graphics.Bitmap;

import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.util.IoUtils;

import java.io.BufferedOutputStream;
import java.io.OutputStream;

/**
 * 把图片写入缓存
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BitmapWriteInDisk extends WriteInDisk<Bitmap> {

	private static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb

	private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;

	private static final int DEFAULT_COMPRESS_QUALITY = 100;

	/** 图片格式 */
	private Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;

	/** 缓冲流大小 */
	private int bufferSize = DEFAULT_BUFFER_SIZE;

	/** 压缩质量 */
	private int compressQuality = DEFAULT_COMPRESS_QUALITY;

	/** 是否回收 */
	private boolean isRecycle;

	public BitmapWriteInDisk(boolean isRecycle) {
		this.isRecycle = isRecycle;
	}

	@Override
	public boolean writeIn(OutputStream out,Bitmap values) {
		OutputStream os = new BufferedOutputStream(out, bufferSize);
		boolean isSuccess = false;
		try {
			isSuccess = values.compress(compressFormat, compressQuality,
					os);
			os.flush();
		} catch (Exception e) {
			LazyLogger.e(e, "Bitmap写入缓存错误");
		} finally {
			IoUtils.closeSilently(os);
			if (isRecycle) {
				values.recycle();
			}
		}
		return isSuccess;
	}

	/**
	 * 设置图片格式
	 * 
	 * @param compressFormat
	 *            格式 void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
		this.compressFormat = compressFormat;
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

	/***
	 * 设置图片质量
	 * 
	 * @param compressQuality
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setCompressQuality(int compressQuality) {
		this.compressQuality = compressQuality;
	}

}
