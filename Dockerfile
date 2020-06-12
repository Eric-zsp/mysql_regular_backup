FROM mariadb:10.4.8

#指定作者
MAINTAINER  Eric

#文件到镜像中
# COPY  finalstation/  /finalstation/

#暴露给容器外的端口: http mysql
EXPOSE 8080 3306

RUN \
    # apt-get update && \
    # apt-get install -y curl wget lsb-release python python-pip && \
    echo "Install Percona XtraBackup" && \
    wget https://www.percona.com/downloads/Percona-XtraBackup-LATEST/Percona-XtraBackup-8.0.12/binary/debian/bionic/x86_64/percona-xtrabackup-dbg-80_8.0.12-1.bionic_amd64.deb && \
    dpkg -i percona-xtrabackup-dbg-80_8.0.12-1.bionic_amd64.deb && \
    rm -f percona-xtrabackup-dbg-80_8.0.12-1.bionic_amd64.deb && \
    apt-get update && \
    apt-get install -y percona-xtrabackup-80 && \  
    apt-get install openjdk-8-jdk \ 
    rm -rf /var/lib/apt/lists/*





