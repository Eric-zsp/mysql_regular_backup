
利用mysqldump实现mysql的定期完整备份和增量备份。(增量未实现)
支持备份文件上传到远程服务器。
远程服务器上传协议暂时支持sftp，后期逐步增加阿里云oss、百度云盘、私有http服务器。

#关于 mysql dump 路径
    windows中如果路径包含空格，则用双引号包起来
#关于gzip
    linux 确保系统环境中包含gzip
    windows： 把gzip.exe 放到程序执行目录中

#管理页面
 http://localhost:port/home/index
