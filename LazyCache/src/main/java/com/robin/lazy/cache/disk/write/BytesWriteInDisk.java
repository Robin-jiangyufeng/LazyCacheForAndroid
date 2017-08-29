/*
 * 文 件 名:  BytesWriteInDisk.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月16日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.disk.write;


import com.robin.lazy.logger.LazyLogger;
import com.robin.lazy.util.IoUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * byte数组写入文件
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月16日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BytesWriteInDisk extends WriteInDisk<byte[]> {

	@Override
	public boolean writeIn(OutputStream out,byte[] values) {
		boolean isSucess = false;
		try {
			out.write(values);
			out.flush();
		} catch (IOException e) {
			LazyLogger.e(e, "byte数组写入文件错误");
		} catch (Exception e) {
			LazyLogger.e(e, "byte数组写入文件错误");
		}
		IoUtils.closeSilently(out);
		return isSucess;
	}

}
