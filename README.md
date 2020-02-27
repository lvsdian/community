## 码匠社区  

### 参考文档  

[thymeleaf文档](http://www.thymeleaf.org)  
[makrdown 插件](https://pandao.github.io)  
[UCloud 对象存储文档](https://docs.ucloud.cn/storage_cdn/ufile/tools/sdk)  
[UCloud 对象存储github example](https://github.com/ucloud/ufile-sdk-java)  
[UCloud 对象存储控制台](https://console.ucloud.cn/ufile/ufile)   
[图标字体库](https://www.iconfont.cn/)

### flyway  
flywaydb：`mvn flyway:migrate`  

### mybatis  
mybatis generator：`mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate` 
 
### 部署   
- spring boot能根据profile的不同，运行不同的application.properties,这个项目中有dev和production两种环境。项目从本地传到github后，服务器端clone下来，通过`copy application.properties application-production.properties`生成一个production环境的application.properties,
修改application-production.properties后，通过`java -jar -Dspring.profiles.active=production *.jar`命令运行production环境下的项目(需要先打包)  
- maven也提供profile，本项目中，dev环境下，flyway使用的本地h2,production环境下，flyway使用的服务器端的mysql.参考
[maven profile (Per User)](http://maven.apache.org/guides/introduction/introduction-to-profiles.html)，在服务器 ~/.m2/settings.xml中
配置profile，完整代码如下：  
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository/>
  <interactiveMode/>
  <offline/>
  <pluginGroups/>
  <servers/>
  <mirrors/>
  <proxies/>
  <profiles>
      <!--开发环境-->
      <profile>
          <id>dev</id>
          <properties>
              <db.url>jdbc:h2:C:/Users/LSD/Documents/h2database/community</db.url>
              <db.user>root</db.user>
              <db.password>root</db.password>
          </properties>
          <activation>
              <activeByDefault>true</activeByDefault>
          </activation>
      </profile>
      <!--生产环境-->
      <profile>
          <id>production</id>
          <properties>
              <db.url>jdbc:mysql://47.102.132.185:3306/lvsd</db.url>
              <db.user>root</db.user>
              <db.password>cn.AndiOS.lsd123456</db.password>
          </properties>
      </profile>
  </profiles>
  <activeProfiles/>
</settings>
```
配置完dev与production两个profile后，服务器中指定运行环境使用flyway，生成数据库：  
`mvn clean compile  flyway:migrate  -Pproduction`  
执行完flyway后，项目打包，`mvn clean compile package`，运行jar即可


### command  
1. 安装环境  
    - `yum update`
    - `yum install git`    
    - `yum install maven`  
    - [安装mysql](https://blog.csdn.net/qq_40605913)
2. 创建目录，clone项目  
    - `mkdir App`  
    -  `cd App`  
    -  `git clone ***.git`
3. 配置spring boot profile  
    - `copy application.properties application-production.properties`   
    - `vim application-production.properties`
4. 配置maven的profile   
    - `mvn clean compile flyway:migrate  -Pproduction`  
    - `mvn clean compile package`  
5. 启动  
    - `java -jar -Dspring.profiles.active=production *.jar`   



