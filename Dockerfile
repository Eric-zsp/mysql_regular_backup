FROM mariadb:10.4.8

#指定作者
MAINTAINER  Eric

#文件到镜像中
COPY  file/percona-xtrabackup-24_2.4.20-1.bionic_amd64.deb  /workbasedir/


RUN \
    # apt-get update && \
    # apt-get install -y curl wget lsb-release python python-pip && \
    echo "Install openjdk" && \
    apt-get update &&\
    apt-get install -y openjdk-8-jdk &&\ 
    update-alternatives --config java 
RUN echo "Install Percona XtraBackup depends" && \
    apt-get update &&\
    apt-get install -y libatomic1 libcurl4-openssl-dev libdbd-mysql-perl libdbi-perl libev4  rsync 
RUN echo "Install Percona XtraBackup" && \   
    cd /workbasedir/ &&\
    dpkg -i percona-xtrabackup-24_2.4.20-1.bionic_amd64.deb && \
    rm -f percona-xtrabackup-24_2.4.20-1.bionic_amd64.deb && \
    apt-get update && \
    apt-get install -y percona-xtrabackup-80   


#暴露给容器外的端口: http mysql
EXPOSE 8080 3306


