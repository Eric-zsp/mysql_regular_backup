# mysql定时备份

#### 项目介绍
1. 基于mariadb、xtrabackup的远程备份工具，支持mysql5、mariadb的备份，暂不支持mysql8.0的备份。  
2. 支持添加计划任务
3. 支持全量备份、增量备份
4. 支持多个远程数据库服务器
5. 支持备份文件上传到其他存储中


#### 安装教程
安装docker后运行
```
docker run --name mysql-regular-backup  --restart=always  --privileged -d \
-v /data/stdm1/dockerData/mysql-regular-backup/mariadb/data/:/var/lib/mysql \
-v /data/stdm1/dockerData/mysql-regular-backup/mariadb/conf:/etc/mysql/conf.d \
-v /data/stdm1/dockerData/mysql-regular-backup/workbasedir:/workbasedir \
-v /data/stdm1/dockerData/mysql-regular-backup/backupData:/backupData \
-p 10011:3306 -p 10012:8080 \
docker pull registry.cn-beijing.aliyuncs.com/eric_zsp/mysql-regular-backup:0.0.1
```


#### 使用说明

1. 参数说明  
```
// 自定义mariadb数据文件挂载位置
-v /data/stdm1/dockerData/mysql-regular-backup/mariadb/data/:/var/lib/mysql \
// 自定义mariadb配置文件挂载位置
-v /data/stdm1/dockerData/mysql-regular-backup/mariadb/conf:/etc/mysql/conf.d \
// 应用挂载位置
-v /data/stdm1/dockerData/mysql-regular-backup/workbasedir:/workbasedir \
// 备份文件挂载位置
-v /data/stdm1/dockerData/mysql-regular-backup/backupData:/backupData \
// mariadb映射端口
-p 10011:3306 \
// 管理、配置页面访问映射端口
-p 10012:8080 \
```




