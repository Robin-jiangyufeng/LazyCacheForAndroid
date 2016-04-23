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
package om.robin.lazy.cache.memory;

import java.util.Collection;
import java.util.Map;

import com.lazy.library.cache.Cache;

/**
 * Interface for memory cache
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @param <V>
 * @since 1.9.2
 */
public interface MemoryCache extends Cache{

	/** Returns all keys of cache */
	Collection<String> keys();
	
	/** Returns all Map of cache 
	 * @param <V>*/
	Map<String, ?> snapshot();
	
}
