        LazyCacheForAndroid
-----------------------------------
#  项目介绍
### 项目地址
  * [LazyCacheForAndroid](https://github.com/Robin-jiangyufeng/LazyCacheForAndroid)

### 介绍:
  * 这是一个android上的数据缓存框架,具有缓存和加载数据速度快,缓存数据类型全,能够实现任意缓存时间等优点

### 功能:
  * 1.目前已经实现的可以缓存String,Serialiable,Bitmap,InputStream,Bytes等类型数据,当然你也可以自己进行扩展实现自己需要缓存的类型数据
  * 2.支持多级缓存,目前已实现lru算法的磁盘缓存和lru算法的内存缓存,根据优先级进行缓存,当然你也可以扩展实现多级缓存,只要实现Cache接口,设置缓存优先级即可
  * 3.可以设置全局数据缓存的时间,也可以单独设置一条数据缓存的时间
  * 4.有更多功能
   
### 使用场景:
  * 1.替换SharePreference当做配置文件
  * 2.缓存网络数据,比如json,图片数据等
  * 3.自己想...

#   使用方法
### 库引入方式
   * Gradle: 
     ````compile 'com.robin.lazy.cache:CacheLibrary:1.0.0'````
   * Maven:
     ````java
       <dependency>
          <groupId>com.robin.lazy.cache</groupId>
          <artifactId>CacheLibrary</artifactId>
          <version>1.0.0</version>
          <type>pom</type>
        </dependency>
      ````
        
### 初始化
   * 想要直接使用CacheLoaderManager进行数据储存的话,请先进行初始化,初始化方式如下:
```java
  /***
	 * 初始化缓存的一些配置
	 * 
	 * @param diskCacheFileNameGenerator
	 * @param diskCacheSize 磁盘缓存大小
	 * @param diskCacheFileCount 磁盘缓存文件的最大限度
	 * @param maxMemorySize 内存缓存的大小
	 * @return CacheLoaderConfiguration
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
CacheLoaderManager.getInstance().init(Context context,FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
                                      			int diskCacheFileCount, int maxMemorySize);
```
### 缓存数据
   * 以下代码只列举了储存String类型的数据,其它数据类型储存类似,具体请阅读 CacheLoaderManager.java
```java
   /**
	 * save String到缓存
	 * @param key 
	 * @param value 要缓存的值
	 * @param maxLimitTime 缓存期限(单位分钟)
	 * @return 是否保存成功
	 * boolean
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
CacheLoaderManager.getInstance().saveString(String key,String value,long maxLimitTime);
```
### 加载缓存数据
   * 以下代码只列举了加载String类型的数据方法,其它数据加载类似,具体请阅读 CacheLoaderManager.java
```java
   /**
     * 加载String
     * @param key
     * @return 等到缓存数据
     * String
     * @throws
     * @see [类、类#方法、类#成员]
     */
CacheLoaderManager.getInstance().loadString(String key);
```
### 其它
   * 上面介绍的是很小的一部分已经实现的功能,其中有还有很多功能可以高度定制,扩展性很强,更多功能待你发现;
   
# 关于作者Robin
* 屌丝程序员
* GitHub: [Robin-jiangyufeng](https://github.com/Robin-jiangyufeng)
* QQ:429257411
* 交流QQ群 236395044
