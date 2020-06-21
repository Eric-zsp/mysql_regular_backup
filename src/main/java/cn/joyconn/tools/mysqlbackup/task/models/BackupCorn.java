package cn.joyconn.tools.mysqlbackup.task.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 备份执行计划
 */
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackupCorn {
    /**
     * 完整备份周期类型 1月 2周 3日
     */
    private int p_timeType;
    /**
     * 完整备份执行日期（周期内）
     * p_timeType=月->第几日
     * p_timeType=周->周几
     * p_timeType=日->第几个小时
     */
    private List<Integer> p_timeIndex;

    /**
     * 备份方式 1完整备份 2增量备份
     */
    private int p_backupMode;

    @ApiModelProperty("备份周期类型 1月 2周 3日")
    public int getP_timeType() {
        return p_timeType;
    }

    public void setP_timeType(int p_timeType) {
        this.p_timeType = p_timeType;
    }

    @ApiModelProperty(" 备份执行日期（周期内）    p_timeType=月->第几日    p_timeType=周->周几    p_timeType=日->第几个小时")
    public List<Integer> getP_timeIndex() {
        return p_timeIndex;
    }

    public void setP_timeIndex(List<Integer> p_timeIndex) {
        this.p_timeIndex = p_timeIndex;
    }

    @ApiModelProperty("备份类型 1完整备份 2增量备份")
    public int getP_backupMode() {
        return p_backupMode;
    }

    public void setP_backupMode(int p_backupMode) {
        this.p_backupMode = p_backupMode;
    }

    
}