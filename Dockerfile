FROM mariadb:10.4.8

#指定作者
MAINTAINER  Eric

#文件到镜像中
COPY  finalstation/  /finalstation/

#暴露给容器外的端口: http mysql
EXPOSE 8080 3306

RUN apt-get update && \
    apt-get install -y curl wget lsb-release python python-pip && \
    echo "Install Percona XtraBackup" && \
    wget https://repo.percona.com/apt/percona-release_8.0.12-1.$(lsb_release -sc)_all.deb && \
    dpkg -i percona-release_8.0.12-1.$(lsb_release -sc)_all.deb && \
    rm -f percona-release_8.0.12-1.$(lsb_release -sc)_all.deb && \
    apt-get update && \
    apt-get install -y percona-xtrabackup-80 && \  
    apt-get install openjdk-8-jdk \ 
    rm -rf /var/lib/apt/lists/*





