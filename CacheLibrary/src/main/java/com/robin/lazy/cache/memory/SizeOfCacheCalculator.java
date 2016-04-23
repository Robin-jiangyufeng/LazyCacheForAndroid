/*
 * 文 件 名:  SizeOfCacheCalculator.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月17日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.memory;

import android.graphics.Bitmap;

/**
 * 内存大小计算器
 * 
 * @author  jiangyufeng
 * @version  [版本号, 2015年12月17日]
 * @param <V>
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SizeOfCacheCalculator<V> {

	/**
	 * 计算一个缓存数据的大小,默认是1(就是一个数据)
	 * 
	 * @param key
	 * @param value
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public int sizeOf(String key, V value) {
		if (value instanceof Bitmap) {
			Bitmap bitmap = (Bitmap) value;
			return bitmap.getRowBytes() * bitmap.getHeight();
		} else if (value instanceof byte[]) {
			return ((byte[]) value).length;
		}
		return 1;
	}
}

