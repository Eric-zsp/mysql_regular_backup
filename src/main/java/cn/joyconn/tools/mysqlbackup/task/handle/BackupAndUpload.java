package cn.joyconn.tools.mysqlbackup.task.handle;

import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.models.BackupCorn;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;
import cn.joyconn.tools.mysqlbackup.task.utils.AESUtils;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;
import cn.joyconn.tools.mysqlbackup.task.utils.dump.MariadbDumpTool;
import cn.joyconn.tools.mysqlbackup.task.utils.dump.MysqlDumpTool;
import cn.joyconn.tools.mysqlbackup.task.utils.dump.XtraBackupDumpTool;
import cn.joyconn.tools.mysqlbackup.task.utils.SFTPUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.util.Strings;

public class BackupAndUpload {
    /**
     *
     * @param backupTaskModel 备份设置对象
     * @param forceFullback 强制完整备份
     */
    public static void dowork(BackupTaskModel backupTaskModel,boolean forceFullback){
        Calendar now = Calendar.getInstance();
        boolean fullback = forceFullback;
        boolean toback = forceFullback;
        int dayIndex = 0;
        if(!fullback){
            if(backupTaskModel.getP_state()==0){
                return;
            }
            if(backupTaskModel.getBackupCorns()!=null){
                for(BackupCorn backupCorn:backupTaskModel.getBackupCorns()){
                    if(backupCorn.getP_timeType()==1){
                        dayIndex = now.get(Calendar.DAY_OF_MONTH);
                    }else  if(backupCorn.getP_timeType()==2){
                        dayIndex = now.get(Calendar.DAY_OF_MONTH);
                    }else{
                        dayIndex = now.get(Calendar.HOUR_OF_DAY);
                    }

                    if(dayIndex==backupCorn.getP_timeIndex()){
                        toback = true;//需要执行备份操作
                        if(backupCorn.getP_backupType()==1){
                            fullback=true;//全量备份
                            break;
                        }
                    }
                }
            }
            
           
        }

        if(toback){
            
            backupTaskModel.setP_beginTime(now.getTime());
            backupTaskModel.setP_runsSate(1);
            GlobleImpl.getGlobleCfgStatic().setBackupTaskModel(backupTaskModel);

           

            doBackup(backupTaskModel,fullback,now.getTime()); 

            backupTaskModel.setP_lastTime(new Date());
            backupTaskModel.setP_runsSate(0);           
            GlobleImpl.getGlobleCfgStatic().setBackupTaskModel(backupTaskModel);
        }
       

    }

    private static void doBackup(BackupTaskModel backupTaskModel,boolean fullback,Date curTime){
        if(backupTaskModel.getP_dbAndTables()==null){
            return;
        }        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTimeStr = dateFormat.format(new Date());
        String fileName ="";
        
        for(String dbName:backupTaskModel.getP_dbAndTables().keySet()){
            String saveDir = GlobleImpl.getGlobleCfgStatic().getSavepath() + File.separator+backupTaskModel.getP_id() + File.separator+dateTimeStr.substring(0,10)+ File.separator+dbName+ File.separator;
            File saveFile = new File(saveDir);
            saveDir = saveFile.getAbsolutePath();
            if (!saveDir.endsWith(File.separator)) {
                saveDir = saveDir + File.separator;
            }            
            fileName = dbName + (fullback?"-full":"-increment") + "-" +  dateTimeStr + "-"  + backupTaskModel.getP_id() + ".sql.gz";
            String dbPWD = "";
            try{
                dbPWD = AESUtils.decryptStr(backupTaskModel.getP_pwd(),GlobleImpl.getGlobleCfgStatic().getDbEnKey());
            }catch (Exception ex){

            }
            try{
                switch(backupTaskModel.getP_backType()){
                    case 1:XtraBackupDumpTool.backup(
                                backupTaskModel.getP_host(),
                                backupTaskModel.getP_port().toString(),
                                backupTaskModel.getP_user(),
                                dbPWD,
                                saveDir,
                                fileName,
                                dbName,
                                backupTaskModel.getP_dbAndTables().get(dbName)                           
                                );
                            break;
                    case 2:MariadbDumpTool.backup(
                            backupTaskModel.getP_host(),
                            backupTaskModel.getP_port().toString(),
                            backupTaskModel.getP_user(),
                            dbPWD,
                            saveDir,
                            fileName,
                            dbName,
                            backupTaskModel.getP_dbAndTables().get(dbName)
                        );
                        break;
                }
            }catch (Exception ex){
                LogHelper.logger().error(ex.getMessage());
            }
            if(backupTaskModel.getP_remoteType().size()>0){
                for(Integer remoteType:backupTaskModel.getP_remoteType()){
                    if(remoteType>0 && backupTaskModel.getP_remoteCfg().containsKey(remoteType)){
                        if(remoteType==1){
                            uploadSftp(backupTaskModel.getP_remoteCfg().get(remoteType),
                                    dbName + File.separator + dateTimeStr.substring(0,8) + File.separator + backupTaskModel.getP_id() + File.separator,
                                    fileName,
                                    saveDir + File.separator + fileName
                                );
                        }
                        
            
                    }
                }
            }
        }
        

    }

    /**
     * sftp 上传
     * @param cfg  sftp连接配置 -> {host:'',user:'',pwd:'',port:1 , targetDir:'' }
     * @param directory
     * @param sftpFileName
     * @param uploadFile
     */
    private static void uploadSftp(String cfg,String directory,String sftpFileName,String uploadFile){
        Map<String,String> sftpCfgMap = null;
        if(Strings.isNotBlank(cfg)){
            try {
                sftpCfgMap = new ObjectMapper().readValue(cfg, new TypeReference<Map<String,String>>() {});
            } catch (Exception e) {
                LogHelper.logger().error("解析sftp连接配置失败,cfg配置："+cfg+"。错误信息:"+e.getMessage() );
            }
        }
        if(sftpCfgMap==null){
            return;
        }
        String username=sftpCfgMap.get("user"),  password=sftpCfgMap.get("pwd"),  host=sftpCfgMap.get("host"),targetDir=sftpCfgMap.get("targetDir");
        int port=0;
        if(Strings.isNotBlank(sftpCfgMap.get("port"))){
            port= Integer.valueOf(sftpCfgMap.get("port"));
        }
        if(port==0||Strings.isBlank(host)){
            return;
        }
        try {
            SFTPUtil sftpUtil = new SFTPUtil(username, password, host, port);
            sftpUtil.login();
            sftpUtil.upload(targetDir+File.separator+directory,sftpFileName, uploadFile);
            sftpUtil.logout();
        }catch (Exception ex){
            LogHelper.logger().error(ex.getMessage());
        }
    }
}
