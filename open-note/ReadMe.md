# open-note模块说明

本模块是开源笔记项目的核心。使用前后端分离的开发方式。使用到的后端技术栈有Spring Boot、Spring Cache、Spring Security、MyBaits、MySQL、Elasticsearch、Redis等，前端技术栈有Vue、JQuery、Bootstrap、Font Awesome，项目管理工具有Git、Maven。目前项目运行于Ubuntu 20.04环境，并使用了Docker。使用的JDK版本为11。

**注意事项：**
- 由于本项目已启用Spring Cache, 所以处理关于缓存方面的代码要小心，尽量避免出错。
- 采用DB与Elasticsearch配合的方案，DB负责数据存储，Elasticsearch负责关键词检索。Elasticsearch可以在多维度上检索与关键词相关的数据，并为每个匹配结果生成一个相关度分数。当服务收到搜索请求时，首先根据关键词到Elasticsearch中进行检索，然后根据检索结果去DB中查询信息，并在应用层进行数据整理和排序。

**分层说明：**

- config：配置层。
    - bean：与Spring配置文件对应。
- controller：前后端交互层。
- dao：于数据库交互。
- dto：使用DTO类来继承entity实体类，在DTO类里放一些业务字段，并提供get、set方法。当在业务逻辑层或者交互层用到一些数据库中不存在的字段时，就需要在DTO类里放这些字段，这些字段的意义就相当于一些经处理过的数据库字段。
- entity：实体模型，与数据库表属性一一对应。entity包内的类属性是和表字段一一对应的，一般不推荐在entity类中添加与表字段无关的属性。
- handler：处理层。
- security：安全层。
- service：服务层，处于dao和controller之间，处于处理复杂的逻辑任务。
- staticManage：管理静态资源。

**开发规范**
- redis：
    - key命名规范：https://www.cnblogs.com/joshua317/p/11995197.html 。

**参考文章：**

本项目在构建过程中参考过的文章：

- Spring Security数据库身份认证和角色授权 https://www.jianshu.com/p/c3b79a625d84
- springboot2.x+springsecurity使用ajax实现异步登录 https://blog.csdn.net/qq_34869990/article/details/103360678
- Elasticsearch最佳实践之使用场景 https://bruce.blog.csdn.net/article/details/82917861