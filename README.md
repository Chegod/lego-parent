# lego-parent

从网上找资料做了一个商城项目，使用 Gradle 管理工程，SSM 框架进行开发，mysql 5.7作为数据库。

## 商城架构

该项目采用 SOA 面向服务的分布式架构，把工程拆分成服务层、表现层两个工程。服务层中包含业务逻辑，只需要对外提供服务即可。
表现层只需要处理和页面的交互，业务逻辑都是调用服务层的服务来实现的，在这里，表现层通过 Dubbo 远程调用服务层的服务。

### Dubbo介绍

<div align=center><img width="530" height="417" src="https://github.com/chegod/lego-parent/blob/master/otherResources/dubbo%E6%9E%B6%E6%9E%84.png"/></div>

|节点|角色说明|
|:---|:---|
|Provider|暴露服务的服务提供方|
|Consumer|调用远程服务的服务消费方|
|Registry|服务注册与发现的注册中心，使用zookeeper作为注册中心|
|Monitor|统计服务的调用次数和调用时间的监控中心，[Dubbokeeper作为监控中心](http://118.24.93.102:8070/dubbokeeper/)|
|Container|服务运行容器|
## 商城入口

用Nginx作为商城的入口。因为一个ip可以绑定多个域名，所以我们只需要访问 Nginx 就行了，Nginx 通过域名区分用户要访问的web应用,然后将请求转发到对应的 Tomcat服务器上。
* [域名规划](https://github.com/Chegod/lego-parent/blob/master/otherResources/nginx.conf)
* [nginx配置文件](https://github.com/Chegod/lego-parent/blob/master/otherResources/%E4%B9%90%E8%B4%AD%E5%95%86%E5%9F%8Ehosts%E6%96%87%E4%BB%B6.txt)

## 后台管理系统

管理商品、订单、类目、商品规格属性，涉及到上传图片。<br>
搭建一个图片服务器，专门保存图片。使用 FastDFS 分布式文件系统，存储空间可以横向扩展，可以实现服务器的高可用,支持每个节点有备份机。

## 前台系统

用户可以在前台系统中进行注册、登录、访问首页、浏览商品、下单等操作。

## Solr 搜索系统

使用 Solr 搜索服务器搜索商品。Solr 提供了图形管理页面，支持高级的全文搜索功能，易于监控，高度可扩展和容错，减轻了服务器的压力。<br>
当索引量很大，搜索请求并发很高，这时需要使用Solr提供的分布式搜索方案 SolrCloud。


## SSO 单点登录系统

单点登录系统是使用 redis 模拟 session ，实现 session 的统一管理。<br>
用户只需要登录一次就可以进入多个系统，而不需要重新登录，这不仅仅带来了更好的用户体验，更重要的是降低了安全的风险和管理的消耗。<br>
因为其他系统调用单点登录系统的服务，就涉及到 cookie 跨域和 ajax 跨域。
* cookie 可以设置 domain 属性，跨一级域名。
* ajax 可以设置 jsonp ，通过 spring 框架提供的方法，将响应结果拼接成一个js语句。

## 购物车和订单系统

展示购物车和订单页面时，需要判断用户是否登录，此时可以创建一个 springmvc 的拦截器，在执行处理器方法之前判断用户是否登录。

## Redis做缓存

商城的首页访问量很高，商品的信息也会经常被访问，所以将首页信息和商品信息转成 json 字符串，存入 redis 中，
每次查 mysql 数据库之前先查询 redis中 的数据，更高效快速，减轻了 mysql 数据库的压力。
