package cn.joyconn.tools.mysqlbackup.task.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostBackupTaskModel extends BackupTaskModel{
    
    /**
     * 数据库连接密码（明文）
     */
    private String password;

    private String errorMsg;

    @ApiModelProperty("数据库连接密码（明文）")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ApiModelProperty("数据库连接密码（明文）")
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
   


    
}
