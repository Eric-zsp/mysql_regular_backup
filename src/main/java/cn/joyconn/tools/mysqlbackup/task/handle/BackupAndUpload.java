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
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import org.apache.logging.log4j.util.Strings;

public class BackupAndUpload {
    /**
     *
     * @param backupTaskModel 备份设置对象
     */
    public static void dowork(BackupTaskModel backupTaskModel){
        Calendar now = Calendar.getInstance();
        boolean fullback = backupTaskModel.getP_backupMode()==1;

        backupTaskModel.setP_beginTime(now.getTime());
        backupTaskModel.setP_runsSate(1);
        GlobleImpl.getGlobleCfgStatic().setBackupTaskModel(backupTaskModel); 
        doBackup(backupTaskModel,fullback,now.getTime()); 
        backupTaskModel.setP_lastTime(new Date());
        backupTaskModel.setP_runsSate(0);           
        GlobleImpl.getGlobleCfgStatic().setBackupTaskModel(backupTaskModel);
       

    }

    private static void doBackup(BackupTaskModel backupTaskModel,boolean fullback,Date curTime){
        if(backupTaskModel.getP_dbAndTables()==null){
            return;
        }        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTimeStr = dateFormat.format(new Date());
        String saveName ="";
        
        for(String dbName:backupTaskModel.getP_dbAndTables().keySet()){
            String saveBaseDir = GlobleImpl.getGlobleCfgStatic().getSavepath() + File.separator+backupTaskModel.getP_host()+ "-" + backupTaskModel.getP_port() + File.separator;
            File saveFile = new File(saveBaseDir);
            saveBaseDir = saveFile.getAbsolutePath();
            if (!saveBaseDir.endsWith(File.separator)) {
                saveBaseDir = saveBaseDir + File.separator;
            }            
            saveName = dateTimeStr + "-" + dbName +  (fullback?"-full":"-increment") ;
            String dbPWD = "";
            try{
                dbPWD = AESUtils.decryptStr(backupTaskModel.getP_pwd(),GlobleImpl.getGlobleCfgStatic().getDbEnKey());
            }catch (Exception ex){

            }
            try{
                switch(backupTaskModel.getP_backType()){
                    case 0:MysqlDumpTool.backup(
                            backupTaskModel.getP_host(),
                            backupTaskModel.getP_port().toString(),
                            backupTaskModel.getP_user(),
                            dbPWD,
                            saveBaseDir+saveName,
                            dbName,
                            dbName,
                            backupTaskModel.isP_compress());
                            break;
                    case 1:XtraBackupDumpTool.backup(
                                backupTaskModel.getP_host(),
                                backupTaskModel.getP_port().toString(),
                                backupTaskModel.getP_user(),
                                dbPWD,
                                saveBaseDir+saveName,
                                dbName,
                                backupTaskModel.getP_dbAndTables().get(dbName),
                                backupTaskModel.isP_compress(),
                                fullback?"":getLastFullBackupDir(saveBaseDir),
                                backupTaskModel.getP_backupParmas()                         
                                );
                            break;
                    case 2:MariadbDumpTool.backup(
                            backupTaskModel.getP_host(),
                            backupTaskModel.getP_port().toString(),
                            backupTaskModel.getP_user(),
                            dbPWD,
                            saveBaseDir+saveName,
                            dbName,
                            backupTaskModel.getP_dbAndTables().get(dbName),
                            backupTaskModel.isP_compress(),
                            fullback?"":getLastFullBackupDir(saveBaseDir),
                            backupTaskModel.getP_backupParmas()
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
                            String tempFileName = saveName+"" +backupTaskModel.getP_id()+".gz";
                            String cmd = "tar -zcvf "+ saveBaseDir + tempFileName + " " + saveBaseDir+saveName;
                            try {
                                Process process = Runtime.getRuntime().exec(cmd);
                                int processResult = process.waitFor();
                                 if (processResult == 0) {// 0 表示线程正常终止。
                                     LogHelper.logger().info("数据库压缩 "+cmd+"完成");
                                 }
                             } catch (IOException e) {
                                LogHelper.logger().info("数据库压缩 "+cmd+"异常1;"+e.getMessage());
                             } catch (InterruptedException e) {
                                LogHelper.logger().info("数据库压缩 "+cmd+"异常2;"+e.getMessage());
                             }
                             
                             File file = new File(tempFileName);
                             if(file.exists()){
                                uploadSftp(backupTaskModel.getP_remoteCfg().get(remoteType),
                                    dbName + File.separator + dateTimeStr.substring(0,8) + File.separator + backupTaskModel.getP_id() + File.separator,
                                    tempFileName,
                                    saveBaseDir + File.separator + tempFileName
                                );
                                file.delete();
                             }
                            
                        }
                        
            
                    }
                }
            }
        }
        

    }
    private static String getLastFullBackupDir(String baseDir){
        File folder = new File(baseDir);
        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory() && file.getName().toLowerCase().endsWith("-full")) {
                    return true;
                }
                return false;
            }
        });
        if(subFolders!=null&&subFolders.length>0){
            List<File> fullDirs = Lists.newArrayList(subFolders);
            fullDirs.sort((c1,c2)->{
                return c1.getName().compareTo(c2.getName());
            });
            return fullDirs.get(0).getAbsolutePath();
        }
        return "";
        
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
