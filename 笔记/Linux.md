## 命令

```shell
# 切换root
su root
```

## JDK

[下载地址](https://www.oracle.com/java/technologies/downloads/)

[Oracle账号](http://bugmenot.com/view/oracle.com)

```shell
# 编辑环境变量
vim /etc/profile

# 添加JAVA环境变量配置
export JAVA_HOME=/data/jdk8
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=$JAVA_HOME/lib:$CLASSPATH

# 环境变量立即生效
source /etc/profile

# 保存后测试
java -version
```

## Zookeeper

```shell
# 下载Zookeeper
wget https://dlcdn.apache.org/zookeeper/zookeeper-3.7.1/apache-zookeeper-3.7.1-bin.tar.gz

# 解压
tar -zxvf apache-zookeeper-3.7.1-bin.tar.gz

# 重命名
mv apache-zookeeper-3.7.1-bin zookeeper

# 编辑环境变量
vim /etc/profile

# 添加Zookeeper环境变量配置
export ZOOKEEPER_HOME=/data/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin

# 环境变量立即生效
source /etc/profile

# 重命名配置文件
mv zookeeper/conf/zoo_sample.cfg zookeeper/conf/zoo.cfg

# 启动Zookeeper
./bin/zkServer.sh start

# 停止Zookeeper
./bin/zkServer.sh stop

# 重启Zookeeper
./bin/zkServer.sh restart

# 查询Zookeeper状态
./bin/zkServer.sh status

# 启动Zookeeper客户端
./bin/zkCli.sh

# 查询当前目录节点
ls -R /
```