#码匠社区

- idea中按住alt，鼠标可以纵向选中 

[thymeleaf文档](http://www.thymeleaf.org)  
[makrdown 插件](https://pandao.github.io)  
[UCloud 对象存储文档](https://docs.ucloud.cn/storage_cdn/ufile/tools/sdk)  
[UCloud 对象存储github example](https://github.com/ucloud/ufile-sdk-java)  
[UCloud 对象存储控制台](https://console.ucloud.cn/ufile/ufile)   
[图标字体库](https://www.iconfont.cn/)
 
flywaydb：`mvn flyway:migrate`  
mybatis generator：`mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate`  


##部署
spring boot能根据profile的不同，运行不同的application.properties,这个项目中有dev和production两种环境。
项目从本地传到github后，服务器端clone下来，通过  
`copy application.properties application-production.properties`  
生成一个production环境的application.properties,配置完application-production.properties后，通过  
`java -jar -Dspring.profiles.active=production *.jar`  
运行项目

同理，maven也提供profile
本项目中，dev环境下，flyway使用的本地h2,production环境下，flyway使用的mysql.
参考[maven profile (Per User)](http://maven.apache.org/guides/introduction/introduction-to-profiles.html)，
在服务器 ~/.m2/settings.xml中配置profile
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
服务器中使用flyway时，指定运行环境：  
`mvn clean compile  flyway:migrate  -Pproduction`  
即可使用production环境下的flyway
执行完flyway后，项目打包，运行即可

1. 安装环境：  
1 `yum update`  
2 `yum install git`    
3 `yum install maven`  
4 [安装mysql](https://blog.csdn.net/qq_40605913)  
2. 创建目录，clone项目：  
1 `mkdir App`  
2 `cd App`  
3 `git clone ***.git`  
3. 运行：       
1`copy application.properties application-production.properties`   
2 `vim application-production.properties`
3  配置maven的profile   
4 `mvn clean compile flyway:migrate  -Pproduction`  
5 `mvn clean compile package`  
6 `java -jar -Dspring.profiles.active=production *.jar`  


