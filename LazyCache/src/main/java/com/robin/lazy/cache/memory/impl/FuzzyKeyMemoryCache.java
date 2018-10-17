/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.robin.lazy.cache.memory.impl;


import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.util.log.CacheLog;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

/**
 * Decorator for {@link MemoryCache}. Provides special feature for cache: some
 * different keys are considered as equals (using {@link Comparator comparator}
 * ). And when you try to put some value into cache by key so entries with
 * "equals" keys will be removed from cache before.<br />
 * <b>NOTE:</b> Used for internal needs. Normally you don't need to use this
 * class.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public class FuzzyKeyMemoryCache implements MemoryCache {

	private final static String LOG_TAG=FuzzyKeyMemoryCache.class.getSimpleName();

	private final MemoryCache cache;
	private final Comparator<String> keyComparator;

	public FuzzyKeyMemoryCache(MemoryCache cache,
			Comparator<String> keyComparator) {
		this.cache = cache;
		this.keyComparator = keyComparator;
	}

	@Override
	public <V> boolean put(String key, V value) {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return false;
		}
		// Search equal key and remove this entry
		synchronized (cache) {
			String keyToRemove = null;
			for (String cacheKey : cache.keys()) {
				if (keyComparator.compare(key, cacheKey) == 0) {
					keyToRemove = cacheKey;
					break;
				}
			}
			if (keyToRemove != null) {
				cache.remove(keyToRemove);
			}
		}
		return cache.put(key, value);
	}

	@Override
	public <V> boolean put(String key, V value, long maxLimitTime) {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return false;
		}
		synchronized (cache) {
			String keyToRemove = null;
			for (String cacheKey : cache.keys()) {
				if (keyComparator.compare(key, cacheKey) == 0) {
					keyToRemove = cacheKey;
					break;
				}
			}
			if (keyToRemove != null) {
				cache.remove(keyToRemove);
			}
		}
		return cache.put(key, value, maxLimitTime);
	}

	@Override
	public <V> V get(String key) {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return null;
		}
		return cache.get(key);
	}

	@Override
	public boolean remove(String key) {
		if (cache == null) {
			CacheLog.e(LOG_TAG ,"MemoryCache缓存操作对象为空",new NullPointerException());
			return false;
		}
		return cache.remove(key);
	}

	@Override
	public Collection<String> keys() {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return null;
		}
		return cache.keys();
	}

	@Override
	public Map<String, ?> snapshot() {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return null;
		}
		return cache.snapshot();
	}

	@Override
	public void resize(int maxSize) {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return;
		}
		cache.resize(maxSize);
	}

	@Override
	public void clear() {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return;
		}
		cache.clear();
	}

	@Override
	public void close() {
		if (cache == null) {
			CacheLog.e(LOG_TAG, "MemoryCache缓存操作对象为空",new NullPointerException());
			return;
		}
		cache.close();
	}
}
