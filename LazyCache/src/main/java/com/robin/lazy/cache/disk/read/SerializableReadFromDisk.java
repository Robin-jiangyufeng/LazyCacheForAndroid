/*
 * 文 件 名:  SerialzableReadFromDisk.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * 从文件中读取对象
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月11日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SerializableReadFromDisk<V extends Serializable> implements ReadFromDisk<V> {

	@SuppressWarnings("unchecked")
	@Override
	public V readOut(File file) {
		V result = null;
		ObjectInputStream read = null;
		try {
			read = new ObjectInputStream(new FileInputStream(file));
			result=(V)read.readObject();
		} catch (StreamCorruptedException e) {
			LazyLogger.e(e, "读取Serialzable错误");
		} catch (IOException e) {
			LazyLogger.e(e, "读取Serialzable错误");
		} catch (ClassNotFoundException e) {
			LazyLogger.e(e, "读取Serialzable错误");
		}finally{
			IoUtils.closeSilently(read);
		}
		return result;
	}

}
