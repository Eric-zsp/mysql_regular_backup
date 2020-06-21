FROM mariadb:10.4.8

#指定作者
MAINTAINER Eric

VOLUME [ "/etc/mysql", "/var/lib/mysql","/data"]
RUN mkdir -p /data
#文件到镜像中
COPY  file/*  /data/

RUN echo "Install openjdk" && \
    apt-get update &&\
    apt-get install -y openjdk-8-jdk   
RUN dpkg --purge --force-depends ca-certificates-java &&\
    apt-get install ca-certificates-java
RUN echo "Install Percona XtraBackup depends" && \
    apt-get update &&\
    apt-get install -y libatomic1 libcurl4-openssl-dev libdbd-mysql-perl libdbi-perl libev4  rsync 
RUN echo "Install Percona XtraBackup" && \   
    cd /data/ &&\
    dpkg -i percona-xtrabackup-24_2.4.20-1.bionic_amd64.deb && \
    rm -f percona-xtrabackup-24_2.4.20-1.bionic_amd64.deb && \
    apt-get update && \
    apt-get install -y percona-xtrabackup-24   


#暴露给容器外的端口: http mysql
EXPOSE 8019 3306

#执行的命令
CMD  ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java -Duser.timezone=GMT+08 -jar /data/backup-app/mysql_regular_backup-1.0-SNAPSHOT.jar"]   

