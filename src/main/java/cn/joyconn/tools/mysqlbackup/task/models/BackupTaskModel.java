package cn.joyconn.tools.mysqlbackup.task.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
import java.util.Map;


@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackupTaskModel {
    private String p_id;
    /**
     * 任务名称
     */
    private String p_name;
    /**
     * 数据库连接地址
     */
    private String p_host;
    /**
     * 数据库连接端口
     */
    private Integer p_port;

    /**
     * 备份类型 1:innobackupex24(mysql 5) 2:mariadb
     */
    private Integer p_backType;
    /**
     * 数据库连接用户名
     */
    private String p_user;
    /**
     * 数据库连接密码（密文）
     */
    private String p_pwd;
    /**
     * 指定库和表，每个库可以指定要备份的表，不指定则备份整个库
     */
    private Map<String,List<String>> p_dbAndTables;

    /**
     * 自定义备份参数
     */
    private String p_backupParmas;

    /**
     * 状态 0停用 1正常
     */
    private int p_state;
    /**
     * 运行状态 0空闲 1运行中
     */
    private int p_runsSate;
    /**
     * 压缩备份
     */
    private boolean p_compress;
    /**
     * 开始时间 如果处于空闲状态则会和最后一次运行时间一致
     */
    private Date p_beginTime;
    /**
     * 最后一次运行时间
     */
    private Date p_lastTime;
  
    /**
     * 备份执行计划
     */
    private List<BackupCorn> p_backupCorns;

    

    private String p_remarks;
    /**
     * 备份文件保留时间（天）
     */
    private int p_retentionTime;


      /**
     * 备份文件远程存储类型  1sftp 2http 3百度云盘 4阿里云oss （暂时仅支持0,2）
     */
    private List<Integer> p_remoteType;
    /**
     * 备份文件远程存储连接配置,不同的存储类型对应不同的配置
     */
    private Map<Integer,String> p_remoteCfg;

    
    @ApiModelProperty("ID")
    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    @ApiModelProperty("任务名称")
    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    @ApiModelProperty("数据库连接地址")
    public String getP_host() {
        return p_host;
    }

    public void setP_host(String p_host) {
        this.p_host = p_host;
    }

    @ApiModelProperty("数据库连接端口")
    public Integer getP_port() {
        return p_port;
    }

    public void setP_port(Integer p_port) {
        this.p_port = p_port;
    }

    @ApiModelProperty("备份类型 1:innobackupex24(mysql 5) 2:mariadb")
    public Integer getP_backType() {
        return p_backType;
    }

    public void setP_backType(Integer p_backType) {
        this.p_backType = p_backType;
    }

    @ApiModelProperty("数据库连接用户名")
    public String getP_user() {
        return p_user;
    }

    public void setP_user(String p_user) {
        this.p_user = p_user;
    }

    @ApiModelProperty("数据库连接密码(密文)")
    public String getP_pwd() {
        return p_pwd;
    }

    public void setP_pwd(String p_pwd) {
        this.p_pwd = p_pwd;
    }

    @ApiModelProperty("指定库和表，每个库可以指定要备份的表，不指定则备份整个库")
    public Map<String, List<String>> getP_dbAndTables() {
        return p_dbAndTables;
    }

    public void setP_dbAndTables(Map<String, List<String>> p_dbAndTables) {
        this.p_dbAndTables = p_dbAndTables;
    }

    @ApiModelProperty(" 状态 0停用 1正常")
    public int getP_state() {
        return p_state;
    }

    public void setP_state(int p_state) {
        this.p_state = p_state;
    }

    @ApiModelProperty("运行状态 0空闲 1运行中")
    public int getP_runsSate() {
        return p_runsSate;
    }

    public void setP_runsSate(int p_runsSate) {
        this.p_runsSate = p_runsSate;
    }

    @ApiModelProperty("开始时间 如果处于空闲状态则会和最后一次运行时间一致")
    public Date getP_beginTime() {
        return p_beginTime;
    }

    public void setP_beginTime(Date p_beginTime) {
        this.p_beginTime = p_beginTime;
    }

    @ApiModelProperty("最后一次运行时间")
    public Date getP_lastTime() {
        return p_lastTime;
    }

    public void setP_lastTime(Date p_lastTime) {
        this.p_lastTime = p_lastTime;
    }

    @ApiModelProperty("备份执行计划")
    public List<BackupCorn> getP_backupCorns() {
        return p_backupCorns;
    }

    public void setP_backupCorns(List<BackupCorn> p_backupCorns) {
        this.p_backupCorns = p_backupCorns;
    }

    @ApiModelProperty("备注")
    public String getP_remarks() {
        return p_remarks;
    }

    public void setP_remarks(String p_remarks) {
        this.p_remarks = p_remarks;
    }

    @ApiModelProperty("备份文件保留时间（天）")
    public int getP_retentionTime() {
        return p_retentionTime;
    }

    public void setP_retentionTime(int p_retentionTime) {
        this.p_retentionTime = p_retentionTime;
    }

    @ApiModelProperty("备份文件远程存储类型  1ftp  2sftp  3http 4百度云盘 5阿里云oss （暂时仅支持0,2）")
    public List<Integer> getP_remoteType() {
        return p_remoteType;
    }

    public void setP_remoteType(List<Integer> p_remoteType) {
        this.p_remoteType = p_remoteType;
    }

    @ApiModelProperty("备份文件远程存储连接配置,不同的存储类型对应不同的配置")
    public Map<Integer, String> getP_remoteCfg() {
        return p_remoteCfg;
    }

    public void setP_remoteCfg(Map<Integer, String> p_remoteCfg) {
        this.p_remoteCfg = p_remoteCfg;
    }

    @ApiModelProperty("自定义备份参数")
    public String getP_backupParmas() {
        return p_backupParmas;
    }

    public void setP_backupParmas(String p_backupParmas) {
        this.p_backupParmas = p_backupParmas;
    }

    
    @ApiModelProperty("是否压缩备份")
    public boolean isP_compress() {
        return p_compress;
    }

    public void setP_compress(boolean p_compress) {
        this.p_compress = p_compress;
    }


    
}
