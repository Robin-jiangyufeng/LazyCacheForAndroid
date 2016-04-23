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
package com.robin.lazy.cache.disk.impl;

import com.robin.lazy.logger.LazyLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.disk.read.ReadFromDisk;
import com.robin.lazy.cache.disk.write.WriteInDisk;

/**
 * Base disk cache.
 *
 * @author Sergey Tarasevich
 * @see FileNameGenerator
 * @since 1.0.0
 */
public class BaseDiskCache implements DiskCache {

	private static final String ERROR_ARG_NULL = " argument must be not null";
	private static final String TEMP_key_POSTFIX = ".tmp";

	protected final File cacheDir;
	protected final File reserveCacheDir;

	protected final FileNameGenerator fileNameGenerator;

	/**
	 * @param cacheDir
	 *            Directory for file caching
	 * @param reserveCacheDir
	 *            null-ok; Reserve directory for file caching. It's used when
	 *            the primary directory isn't available.
	 * @param fileNameGenerator
	 *            Name generator} for cached files
	 */
	public BaseDiskCache(File cacheDir, File reserveCacheDir,
			FileNameGenerator fileNameGenerator) {
		if (cacheDir == null) {
			throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
		}
		if (fileNameGenerator == null) {
			throw new IllegalArgumentException("fileNameGenerator"
					+ ERROR_ARG_NULL);
		}

		this.cacheDir = cacheDir;
		this.reserveCacheDir = reserveCacheDir;
		this.fileNameGenerator = fileNameGenerator;
	}
	
	@Override
	public long size() {
		return getDirSize(getDirectory());
	}
	
	/**
	 * 获取Dir大小
	 * @param file
	 * @return
	 * long
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	private long getDirSize(File file) {     
        //判断文件是否存在     
        if (file.exists()) {     
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                long size = 0;     
                for (File f : children)     
                    size += getDirSize(f);     
                return size;     
            } else {//如果是文件则直接返回其大小,以byte为单位   
                long size = file.length();        
                return size;     
            }     
        } else {     
            LazyLogger.e("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;     
        }     
    }

	@Override
	public File getDirectory() {
		return cacheDir;
	}

	@Override
	public File getFile(String key) {
		String fileName = fileNameGenerator.generate(key);
		File dir = cacheDir;
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			if (reserveCacheDir != null
					&& (reserveCacheDir.exists() || reserveCacheDir.mkdirs())) {
				dir = reserveCacheDir;
			}
		}
		return new File(dir, fileName);
	}

	@Override
	public <V> V get(String key, ReadFromDisk<V> readFromDisk) {
		File file = getFile(key);
		if (file == null || !file.exists()) {
			return null;
		}
		return readFromDisk.readOut(file);
	}

	@Override
	public <V> boolean put(String key, WriteInDisk<V> writeIn, V value)
			throws IOException {
		boolean savedSuccessfully = false;
		File keyFile = getFile(key);
		File tmpFile = new File(keyFile.getAbsolutePath() + TEMP_key_POSTFIX);
		if (writeIn != null) {
			savedSuccessfully = writeIn.writeIn(new FileOutputStream(tmpFile),
					value);
		}
		if (savedSuccessfully && !tmpFile.renameTo(keyFile)) {
			savedSuccessfully = false;
		}
		if (!savedSuccessfully) {
			tmpFile.delete();
		}
		return savedSuccessfully;
	}

	@Override
	public <V> boolean put(String key, WriteInDisk<V> writeIn, V value,
			long maxLimitTime) throws IOException {
		return put(key, writeIn, value);
	}

	@Override
	public boolean remove(String key) {
		return getFile(key).delete();
	}

	@Override
	public void close() {
		// Nothing to do
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}

}