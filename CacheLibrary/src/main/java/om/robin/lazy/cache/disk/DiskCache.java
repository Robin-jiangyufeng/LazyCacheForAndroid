/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
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
package om.robin.lazy.cache.disk;

import java.io.File;
import java.io.IOException;

import com.lazy.library.cache.disk.read.ReadFromDisk;
import com.lazy.library.cache.disk.write.WriteInDisk;

/**
 * Interface for disk cache
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.2
 */
public interface DiskCache {
	/**
	 * Returns root directory of disk cache
	 *
	 * @return Root directory of disk cache
	 */
	File getDirectory();
	
	/***
	 * 获取当前磁盘缓存大小(单位byte字节)
	 * @return
	 * long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	long size();

	/**
	 * Returns file of cached key
	 *
	 * @param key
	 *            Original key URI
	 * @return File of cached key or <b>null</b> if key wasn't cached
	 */
	File getFile(String key);

	/**
	 * 查询一条缓存数据
	 * 
	 * @param key
	 *            缓存数据的标识
	 * @param readFromDisk
	 * @return V
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	<V> V get(String key, ReadFromDisk<V> readFromDisk);

	/**
	 * Saves key V in disk cache.
	 * 
	 * @param <V>
	 *
	 * @param key
	 *            Original key URI
	 * @param writeIn
	 *            key V
	 * @param value
	 * @return <b>true</b> - if bitmap was saved successfully; <b>false</b> - if
	 *         bitmap wasn't saved in disk cache.
	 * @throws IOException
	 */
	<V> boolean put(String key, WriteInDisk<V> writeIn, V value)
			throws IOException;

	/**
	 * Saves key V in disk cache.
	 * 
	 * @param <V>
	 *
	 * @param key
	 *            Original key URI
	 * @param writeIn
	 *            key V
	 * @param value
	 * @param maxLimitTime
	 *            缓存有效期(单位秒)
	 * @return <b>true</b> - if bitmap was saved successfully; <b>false</b> - if
	 *         bitmap wasn't saved in disk cache.
	 * @throws IOException
	 */
	<V> boolean put(String key, WriteInDisk<V> writeIn, V value,
					long maxLimitTime) throws IOException;

	/**
	 * delete key file associated with incoming URI
	 *
	 * @param key
	 *            key URI
	 * @return <b>true</b> - if key file is deleted successfully; <b>false</b> -
	 *         if key file doesn't exist for incoming URI or key file can't be
	 *         deleted.
	 */
	boolean remove(String key);

	/** Closes disk cache, releases resources. */
	void close();

	/** Clears disk cache. */
	void clear();
}
