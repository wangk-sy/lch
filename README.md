# lch

该项目是一个简单的spring boot项目，是本人初次上手springboot，该项目的主要功能是将爬取的网站进行管理以及预览，再加上一个用户管理模块，目前项目的功能尚少
，在后续也会逐渐将所学的内容运用在上面。

项目是前后端分离的，安全认证框架使用了shiro+jwt，目前并不完善，只有简单的认证逻辑，后续决定采用spring-security+oauth2+jwt来实现认证授权。
项目采用统一的异常处理和结果返回，数据库使用MySQL，持久层框架是mybatis-plus，在mybatis的基础上做了增强，简化了持久层的开发。
***



**在项目过程中使用的技术**：

* 添加新用户后，会向新用户发送一封邮件，使用了RabbitMQ消息队列；
* 在网站的管理模块中，检索使用了elasticsearch技术，加快检索速度；
* 在网站的管理模块中，导入数据功能使用了POI；
* 在用户个人中心中，用户头像上传功能使用了fastDFS分布式存储系统

