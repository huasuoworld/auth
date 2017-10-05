keytool -genkey -keystore E:\db\keystore\server.jks -storepass 123456 -keyalg RSA -keypass 123456
keytool -export -keystore E:\db\keystore\server.jks -storepass 123456 -file E:\db\keystore\server.cer 
keytool -import -keystore E:\db\keystore\server.jks -storepass 123456 -file E:\db\keystore\server.cer

keytool -genkey -keystore c:\sean_app\client.jks -storepass 1234sp -keyalg RSA -keypass 1234kp
keytool -export -keystore c:\sean_app\client.jks -storepass 1234sp -file c:\sean_app\client.cer
keytool -import -keystore c:\sean_app\serverTrust.jks -storepass 1234sp -file c:\sean_app\client.cer 

2017-10-05	分布式定时任务
hua.world.scheduling.ScheduleClusterCheck 类定义拦截器，拦截定时任务
1、当定时任务开始时，创建zookeeper节点
2、任务当结束时，sleap时间为定时任务 50%～80%的总时间
3、删除该节点
3、其他机器触发创建zookeeper节点时，创建失败，节点已经存在，不再执行定时任务

![image](https://github.com/huasuoworld/auth/tree/master/src/main/resources/task.png)

zookeeper版本3.4.6
zookeeper集群部署:
http://zookeeper.apache.org/doc/trunk/zookeeperAdmin.html
![image](https://github.com/huasuoworld/auth/tree/master/src/main/resources/zookeeperCluster.png)

zoo.cfg配置文件
1、复制zookeeper配置文件 conf/zoo_sample.cfg
cp zoo_sample.cfg zoo.cfg
2、修改配置文件，增加（修改）以下参数

tickTime=2000
dataDir=/var/lib/zookeeper/
clientPort=2181
initLimit=5
syncLimit=2
server.1=zoo1:2888:3888
server.2=zoo2:2888:3888
server.3=zoo3:2888:3888
