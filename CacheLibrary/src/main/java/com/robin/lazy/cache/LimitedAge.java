/*
 * 文 件 名:  LimitedAge.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月15日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache;
/**
 * 数据有效时间处理类
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LimitedAge {
	/** 保存时候的时间(单位毫秒) */
	private long saveTime;
	/** 磁盘缓存文件的最大有效时间(单位秒) */
	private long maxLimitTime;

	public LimitedAge(long saveTime, long maxLimitTime) {
		this.saveTime = saveTime;
		this.maxLimitTime = maxLimitTime;
		if (this.maxLimitTime <= 0) {
			this.maxLimitTime = Long.MAX_VALUE;
		}
	}

	/***
	 * 检测是否过期
	 * 
	 * @return boolean 返回是否过期(为true则过期)
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public boolean checkExpire() {
		if (System.currentTimeMillis() - saveTime > (maxLimitTime * 1000)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 计算数据剩余有效时间(单位秒)
	 * @return
	 * long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public long limitedTime(){
		return (maxLimitTime*1000-(System.currentTimeMillis()-saveTime))/1000;
	}

	/***
	 * 获取数据保存时的时间(单位毫秒)
	 * 
	 * @return long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public long getSaveTime() {
		return saveTime;
	}

	/***
	 * 设置缓数据存时的时间(单位毫秒)
	 * 
	 * @param saveTime
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	/***
	 * 获取缓存数据的有效时间(单位秒)
	 * 
	 * @return long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public long getMaxLimitTime() {
		return maxLimitTime;
	}

	/**
	 * 设置缓存的有效时间(单位秒)
	 * 
	 * @param maxLimitTime
	 *            void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void setMaxLimitTime(long maxLimitTime) {
		this.maxLimitTime = maxLimitTime;
	}

}
