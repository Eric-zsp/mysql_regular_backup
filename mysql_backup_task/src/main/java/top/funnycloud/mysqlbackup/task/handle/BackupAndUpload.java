package top.funnycloud.mysqlbackup.task.handle;

import top.funnycloud.mysqlbackup.task.configuration.GlobleCfg;
import top.funnycloud.mysqlbackup.task.configuration.GlobleImpl;
import top.funnycloud.mysqlbackup.task.models.BackupTaskModel;
import top.funnycloud.mysqlbackup.task.utils.AESUtils;
import top.funnycloud.mysqlbackup.task.utils.LogHelper;
import top.funnycloud.mysqlbackup.task.utils.MysqlDumpTool;
import top.funnycloud.mysqlbackup.task.utils.SFTPUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class BackupAndUpload {
    /**
     *
     * @param backupTaskModel 备份设置对象
     * @param forceFullback 强制完整备份
     */
    public static void dowork(BackupTaskModel backupTaskModel,boolean forceFullback){
        Calendar now = Calendar.getInstance();
        backupTaskModel.setP_beginTime(now.getTime());
        backupTaskModel.setP_runsSate(1);
        GlobleImpl.getGlobleCfgStatic().setBackupTaskModel(backupTaskModel);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = dateFormat.format(now.getTime())+".sql.gz";
        String saveDir = GlobleImpl.getGlobleCfgStatic().getSavepath()+"/"+backupTaskModel.getP_dbname()+"/";
        File saveFile = new File(saveDir);
        saveDir = saveFile.getAbsolutePath();
        if (!saveDir.endsWith(File.separator)) {
            saveDir = saveDir + File.separator;
        }
        boolean fullback = forceFullback;
        int dayIndex = 0;
        if(!forceFullback){
            if(backupTaskModel.getP_fullBackupTimeType()==1){
                dayIndex = now.get(Calendar.DAY_OF_MONTH);
            }else{
                dayIndex = now.get(Calendar.DAY_OF_WEEK);
            }
            if(dayIndex==backupTaskModel.getP_fullBackupTimeIndex()){
                fullback=true;
            }
        }

        String dbPWD = "";
        try{
            dbPWD = AESUtils.decryptStr(backupTaskModel.getP_pwd(),GlobleImpl.getGlobleCfgStatic().getDbEnKey());
        }catch (Exception ex){

        }
        try{
            MysqlDumpTool.exportDatabaseTool(GlobleImpl.getGlobleCfgStatic().getDumppath(),
                    backupTaskModel.getP_host(),
                    backupTaskModel.getP_port().toString(),
                    backupTaskModel.getP_user(),
                    dbPWD,
                    saveDir,
                    fileName,
                    backupTaskModel.getP_dbname()
                    );
        }catch (Exception ex){
            LogHelper.logger().error(ex.getMessage());
        }
        if(backupTaskModel.getP_remoteType()>0){
            if(backupTaskModel.getP_remoteType()==2){
                uploadSftp(backupTaskModel.getP_remoteStr(),fileName.substring(0,8)+"/"+GlobleImpl.getGlobleCfgStatic().getLocalHostName(),backupTaskModel.getP_dbname()+fileName,saveDir+File.separator+fileName);
            }
        }
        backupTaskModel.setP_lastTime(new Date());
        backupTaskModel.setP_runsSate(0);
        GlobleImpl.getGlobleCfgStatic().setBackupTaskModel(backupTaskModel);

    }

    private static void uploadSftp(String url,String directory,String sftpFileName,String uploadFile){
        String username="",  password="",  host="";
        int port=0;
        //region 转换sftp服务器信息
        if(url!=null&&!url.equals("")){
            if(url.length()>6){
                url=url.substring("ftp://".length());
                int index = url.indexOf(":");
                if(index>0){
                    username = url.substring(0,index);
                    url=url.substring(index+1);
                }
                index = url.indexOf("@");
                if(index>0){
                    password = url.substring(0,index);
                    url=url.substring(index+1);
                }
                index = url.indexOf(":");
                if(index>0){
                    host = url.substring(0,index);
                    url=url.substring(index+1);
                }
                index = url.indexOf("/");
                if(index>0){
                    try {
                        port =Integer.valueOf(url.substring(0, index));
                    }catch (Exception ex){

                    }
                }
            }
        }
        //endregion

        try {
            SFTPUtil sftpUtil = new SFTPUtil(username, password, host, port);
            sftpUtil.login();
            sftpUtil.upload(directory,sftpFileName, uploadFile);
            sftpUtil.logout();
        }catch (Exception ex){
            LogHelper.logger().error(ex.getMessage());
        }
    }
}
