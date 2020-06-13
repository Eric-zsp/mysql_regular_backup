package cn.joyconn.tools.mysqlbackup.task.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class BackupTaskModel {
    private String p_id;
    private String p_name;
    private String p_host;
    private Integer p_port;
    private String p_user;
    private String p_dbname;
    private String p_pwd;


    /**
     * 状态 0停用 1正常
     */
    private int p_state;
    /**
     * 状态 0空闲 1运行中
     */
    private int p_runsSate;

    /**
     * 开始时间 如果处于空闲状态则会和最后一次运行时间一致
     */
    private Date p_beginTime;
    /**
     * 最后一次运行时间
     */
    private Date p_lastTime;
    /**
     * 远程服务器类型 0不上传 1ftp  2sftp  3http 4百度云盘 （暂时仅支持2）
     */
    private int p_remoteType;
    /**
     * 远程服务器地址
     */
    private String p_remoteStr;
    /**
     * 运行周期 corn表达式
     * 已过期 多个数据库的备份改由单线程顺序执行（避免因为备份而造成数据库压力）
     */
    @Deprecated
    private String p_runCorn;
    /**
     * 完整备份周期类型 1 月 2周
     */
    private int p_fullBackupTimeType;
    /**
     * 完整备份执行日期（周期内）
     * p_fullBackupTimeType=月->第几日
     * p_fullBackupTimeType=周->周几
     */
    private int p_fullBackupTimeIndex;

    private String p_remarks;
    /**
     * 备份文件保留时间（天）
     */
    private int p_retentionTime;

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public Integer getP_port() {
        return p_port;
    }

    public void setP_port(Integer p_port) {
        this.p_port = p_port;
    }

    public String getP_host() {
        return p_host;
    }

    public void setP_host(String p_host) {
        this.p_host = p_host;
    }

    public String getP_user() {
        return p_user;
    }

    public void setP_user(String p_user) {
        this.p_user = p_user;
    }

    public String getP_dbname() {
        return p_dbname;
    }

    public void setP_dbname(String p_dbname) {
        this.p_dbname = p_dbname;
    }

    public String getP_pwd() {
        return p_pwd;
    }

    public void setP_pwd(String p_pwd) {
        this.p_pwd = p_pwd;
    }

    public int getP_state() {
        return p_state;
    }

    public void setP_state(int p_state) {
        this.p_state = p_state;
    }

    public int getP_runsSate() {
        return p_runsSate;
    }

    public void setP_runsSate(int p_runsSate) {
        this.p_runsSate = p_runsSate;
    }

    public Date getP_beginTime() {
        return p_beginTime;
    }

    public void setP_beginTime(Date p_beginTime) {
        this.p_beginTime = p_beginTime;
    }

    public Date getP_lastTime() {
        return p_lastTime;
    }

    public void setP_lastTime(Date p_lastTime) {
        this.p_lastTime = p_lastTime;
    }

    public int getP_remoteType() {
        return p_remoteType;
    }

    public void setP_remoteType(int p_remoteType) {
        this.p_remoteType = p_remoteType;
    }

    public String getP_remoteStr() {
        return p_remoteStr;
    }

    public void setP_remoteStr(String p_remoteStr) {
        this.p_remoteStr = p_remoteStr;
    }

    public String getP_runCorn() {
        return p_runCorn;
    }

    public void setP_runCorn(String p_runCorn) {
        this.p_runCorn = p_runCorn;
    }

    public int getP_fullBackupTimeType() {
        return p_fullBackupTimeType;
    }

    public void setP_fullBackupTimeType(int p_fullBackupTimeType) {
        this.p_fullBackupTimeType = p_fullBackupTimeType;
    }

    public int getP_fullBackupTimeIndex() {
        return p_fullBackupTimeIndex;
    }

    public void setP_fullBackupTimeIndex(int p_fullBackupTimeIndex) {
        this.p_fullBackupTimeIndex = p_fullBackupTimeIndex;
    }

    public String getP_remarks() {
        return p_remarks;
    }

    public void setP_remarks(String p_remarks) {
        this.p_remarks = p_remarks;
    }

    public int getP_retentionTime() {
        return p_retentionTime;
    }

    public void setP_retentionTime(int p_retentionTime) {
        this.p_retentionTime = p_retentionTime;
    }
}
