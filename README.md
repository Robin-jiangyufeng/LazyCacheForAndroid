# LazyCacheForAndroid
## 项目介绍
  android缓存库,可换缓存String,Serializable,Bitmap,InputStream,Bytes等数据,还可以扩展实现世界想要储存的类型数据;
支持多级缓存,可以自己定实现自定义多级缓存;
具体使用方法如下:
## 使用方法
### 初始化
    想要直接使用CacheLoaderManager进行数据储存的话,请先进行初始化,初始化方式如下
    
        CacheLoaderManager.getInstance().init(context, new HashCodeFileNameGenerator(), 1024 * 1024 * 8, 50, 20);
        
### 存储数据代码
    以下代码只列举了储存String类型的数据,其它数据类型储存类似,具体请阅读 CacheLoaderManager.java
    
    CacheLoaderManager.getInstance().loadString(key);
   
