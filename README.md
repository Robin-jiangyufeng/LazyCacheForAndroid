        LazyCacheForAndroid
-----------------------------------
#                     项目介绍
## 介绍:
  * 这是一个android上的数据缓存框架,具有缓存和加载数据速度快,缓存数据类型全,能够实现任意缓存时间等优点

## 功能:
  * 1.目前已经实现的可以缓存String,Serialiable,Bitmap,InputStream,Bytes等类型数据,当然你也可以自己进行扩展实现自己需要缓存的类型数据
  * 2.支持多级缓存,目前已实现lru算法的磁盘缓存和lru算法的内存缓存,根据优先级进行缓存,当然你也可以扩展实现多级缓存,只要实现Cache接口,设置缓存优先级即可
  * 3.可以设置全局数据缓存的时间,也可以单独设置一条数据缓存的时间
  * 4.有更多功能
   
## 使用场景:
  * 1.替换SharePreference当做配置文件
  * 2.缓存网络数据,比如json,图片数据等
  * 3.自己想...

#                     使用方法
## 初始化
   * 想要直接使用CacheLoaderManager进行数据储存的话,请先进行初始化,初始化方式如下:
```java
CacheLoaderManager.getInstance().init(context, new HashCodeFileNameGenerator(), 1024 * 1024 * 8, 50, 20);
```
## 存储数据代码
   * 以下代码只列举了储存String类型的数据,其它数据类型储存类似,具体请阅读 CacheLoaderManager.java
```java
CacheLoaderManager.getInstance().loadString(key);
```
   
