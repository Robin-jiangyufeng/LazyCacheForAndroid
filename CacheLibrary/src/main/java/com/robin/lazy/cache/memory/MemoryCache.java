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
package com.robin.lazy.cache.memory;

import com.robin.lazy.cache.Cache;

import java.util.Collection;
import java.util.Map;

/**
 * 内存缓存操作接口
 *
 * @author jiangyufeng
 * @version [版本号, 2015年12月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface MemoryCache extends Cache {

	/**
	 * 获取所有的key
	 * @return
	 */
	Collection<String> keys();

	/**
	 * 获取缓存数据集合
	 * @return
	 */
	Map<String, ?> snapshot();
	
}
