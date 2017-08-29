/*
 * 文 件 名:  StrigWriteInCache.java
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 把string 写入OutputStream中
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StringWriteInDisk extends WriteInDisk<String> {

	private final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 编码类型
	 */
	private String responseCharset = DEFAULT_CHARSET;

	@Override
	public boolean writeIn(OutputStream out,String values) {
		Writer writer = null;
		boolean isSucce = false;
		try {
			writer = new OutputStreamWriter(out, responseCharset);
			writer.write(values);
			writer.flush();
			isSucce = true;
		} catch (IOException e) {
			LazyLogger.e(e, "String写入缓存错误");
		} catch (Exception e) {
			LazyLogger.e(e, "String写入缓存错误");
		}finally {
			IoUtils.closeSilently(writer);
		}
		return isSucce;
	}

	/***
	 * 设置String的编码类型
	 * @param responseCharset
	 * void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}
	
}
