/*
 * 文 件 名:  LruMemoryCache.java
 * 版    权:  Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jiangyufeng
 * 修改时间:  2015年12月14日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.robin.lazy.cache.memory.impl;

import android.support.v4.util.LruCache;

import com.robin.lazy.logger.LazyLogger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import com.robin.lazy.cache.memory.MemoryCache;

/**
 * Lru算法的内存缓存
 * 
 * @author jiangyufeng
 * @version [版本号, 2015年12月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LruMemoryCache implements MemoryCache {

	/** 内存缓存存储类 */
	private LruCache<String, Object> lruCache;

	/***
	 * @param maxSize
	 *            内存最大限度
	 */
	public LruMemoryCache(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		lruCache = new LruCache<String, Object>(maxSize) {
			@Override
			protected int sizeOf(String key, Object value) {
				return LruMemoryCache.this.sizeOf(key, value);
			}

			protected void entryRemoved(boolean evicted, String key,
					Object oldValue, Object newValue) {
				LruMemoryCache.this.entryRemoved(evicted, key, oldValue,
						newValue);
			};
		};
	}

	@Override
	public <V> boolean put(String key, V value) {
		if (key == null || value == null) {
			throw new NullPointerException("key == null || value == null");
		}
		if (lruCache != null) {
			try {
				lruCache.put(key, value);
				return true;
			} catch (NullPointerException e) {
				LazyLogger.e(e, "put to menory fail");
			} catch (Exception e) {
				LazyLogger.e(e, "put to menory fail");
			}
		}
		return false;
	}

	@Override
	public <V> boolean put(String key, V value, long maxLimitTime) {
		return put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}
		if (lruCache != null) {
			V values = null;
			try {
				values = (V) lruCache.get(key);
			} catch (NullPointerException e) {
				LazyLogger.e(e, "缓存数据不存在，不能强制类型转换");
			} catch (ClassCastException e) {
				LazyLogger.e(e, "强制类型转换错误,不符合的类型");
			} catch (Exception e) {
				LazyLogger.e(e, "");
			}
			return values;
		}
		return null;
	}

	@Override
	public boolean remove(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}
		if (lruCache != null && lruCache.remove(key) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, ?> snapshot() {
		if (lruCache != null) {
			return lruCache.snapshot();
		}
		return null;
	}
	
	@Override
	public Collection<String> keys() {
		Map<String, ?> map = snapshot();
		if (map != null) {
			return new HashSet<String>(map.keySet());
		}
		return null;

	}

	@Override
	public void resize(int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		if (lruCache != null) {
			lruCache.resize(maxSize);
		}
	}

	@Override
	public void clear() {
		if (lruCache != null) {
			lruCache.evictAll();
		}
	}

	@Override
	public void close() {
		clear();
		if (lruCache != null) {
			lruCache = null;
		}
	}

	/***
	 * 当item被回收或者删掉时调用。改方法当value被回收释放存储空间时被remove调用， 
	 * 或者替换item值时put调用，默认实现什么都没做。
	 * 
	 * @param <V>
	 * @param evicted 是否释放被删除的空间
	 * @param key
	 * @param oldValue 老的数据
	 * @param newValue 新数据 void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	protected <V> void entryRemoved(boolean evicted, String key, V oldValue,
			V newValue) {
		oldValue = null;
	};

	/**
	 * 计算一个缓存数据的大小,默认是1(就是一个数据)
	 * 
	 * @param <V>
	 * 
	 * @param key
	 * @param value
	 * @return int
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	protected <V> int sizeOf(String key, V value) {
		return 1;
	}

	@Override
	public final String toString() {
		if (lruCache != null) {
			return lruCache.toString();
		}
		return String.format("LruCache[maxSize=%d]", 0);
	}

}
