package cn.joyconn.tools.mysqlbackup.task.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import cn.joyconn.tools.mysqlbackup.task.handle.TaskBusHandle;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;
import cn.joyconn.tools.mysqlbackup.task.utils.AESUtils;
import cn.joyconn.tools.mysqlbackup.task.utils.DBObjectID;
import cn.joyconn.tools.mysqlbackup.task.utils.FileHelper;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class GlobleCfg {


    @Value("${databack.dataPath}")
     String dataPath;
    @Value("${databack.dbEnKey}")
     String  dbEnKey;
    @Value("${databack.savepath}")
    String  savepath;
    @Value("${databack.msyqlDumpPath}")
    String  msyqlDumpPath;
    public String getDataPath(){
        return dataPath;
    }
    public String getDbEnKey(){
        return dbEnKey;
    }
    public String getSavepath(){
        return savepath;
    }

    ObjectMapper objectMapper =new ObjectMapper();
    static ReentrantReadWriteLock readWriteBackupCfgLock = new ReentrantReadWriteLock();
    static ConcurrentHashMap<String,BackupTaskModel> backupTaskModelMap;
    public  Collection<BackupTaskModel> getBackupTaskModels() {
        if(backupTaskModelMap==null){
            readDBCfgData();
        }
        return backupTaskModelMap.values();
    }

    public  BackupTaskModel getBackupTaskModel(String id) {
        getBackupTaskModels();

        return backupTaskModelMap.get(id);
    }
    public  BackupTaskModel setBackupTaskModel(BackupTaskModel model) {
        getBackupTaskModels();
        if(model!=null){
            if(model.getP_id()==null||model.getP_id().equals("")){
                model.setP_id(new DBObjectID().toString());
            }
            String dblink="";
            try{
                dblink = AESUtils.decryptStr(model.getP_pwd(),dbEnKey);
            }catch (Exception ex){
                dblink = model.getP_pwd();
            }
            model.setP_pwd(AESUtils.encryptStr(dblink,dbEnKey));
            backupTaskModelMap.put(model.getP_id(),model);
            saveDBCfgData();
            TaskBusHandle.startOrResetBackupAndUploadRemote(model.getP_id());

        }
        return model;
    }

    public  void removeBackupTaskModel(String id) {
        getBackupTaskModels();
        if(id!=null){

            backupTaskModelMap.remove(id);
            saveDBCfgData();
            TaskBusHandle.deleteBackupAndUploadRemote(id);

        }
    }



    private String getDbCfgPath(){
        String abPath = "";
        if(dataPath.startsWith("classpath:")) {
            try {
                File file = ResourceUtils.getFile(dataPath);
                abPath = file.getAbsolutePath();
            } catch (Exception ex){
                LogHelper.logger().error(ex.getMessage());
            }
        }else{
            File file = new File(dataPath); 
            abPath = file.getAbsolutePath();
        }
        return abPath;
    }
    private boolean readDBCfgData() {
        Boolean result =false;
        try {

            readWriteBackupCfgLock.readLock().lock();
            if(backupTaskModelMap==null){
                backupTaskModelMap = new ConcurrentHashMap<>();
                String jsonStr = "";
                jsonStr = FileHelper.readStrFromFile(getDbCfgPath());
                if (jsonStr != null && !jsonStr.equals("")) {
                    LogHelper.logger().info(jsonStr);
                    try {
                        BackupTaskModel[] backupTaskModels = objectMapper.readValue(jsonStr,BackupTaskModel[].class);

                        if(backupTaskModels!=null){
                            for(BackupTaskModel backupTaskModel:backupTaskModels){
                                backupTaskModelMap.put(backupTaskModel.getP_id(),backupTaskModel);
                            }
                        }
                        LogHelper.logger().info("加载配置完毕");

                    } catch (Exception ex) {
                        LogHelper.logger().error("反序列化配置文件时报错:" + ex.getMessage());
                        result= false;
                    }
                } else {
                    LogHelper.logger().error("未能读取配置文件时有效内容");
                    result = false;
                }
            }

        } catch (Exception ex) {
            LogHelper.logger().error("读取配置文件时失败");
            result = false;
        }finally {
            readWriteBackupCfgLock.readLock().unlock();
        }
        return result;
    }
    private void saveDBCfgData(){
        try {

            readWriteBackupCfgLock.writeLock().lock();
            if(backupTaskModelMap!=null){
                String saveFilePath = getDbCfgPath();

                if(saveFilePath!=null&&!saveFilePath.equals("")){
                    String saveStr = objectMapper.writeValueAsString(backupTaskModelMap.values());
                    FileHelper.writeFile(saveFilePath,saveStr,"utf8");
                }

            }

        } catch (Exception ex) {
            LogHelper.logger().error("写入配置文件时失败");
        }finally {
            readWriteBackupCfgLock.writeLock().unlock();
        }
    }

    public String getMsyqlDumpPath() {
        return msyqlDumpPath;
    }

    public void setMsyqlDumpPath(String msyqlDumpPath) {
        this.msyqlDumpPath = msyqlDumpPath;
    }
}
