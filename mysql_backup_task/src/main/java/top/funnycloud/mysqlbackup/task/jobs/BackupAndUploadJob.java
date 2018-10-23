package top.funnycloud.mysqlbackup.task.jobs;


import org.quartz.JobExecutionContext;
import top.funnycloud.mysqlbackup.task.configuration.GlobleImpl;
import top.funnycloud.mysqlbackup.task.handle.BackupAndUpload;
import top.funnycloud.mysqlbackup.task.jobs.baseJob.SingleChannelBaseJob;
import top.funnycloud.mysqlbackup.task.models.BackupTaskModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.text.ParseException;
/**
 * 历史数据日志上传
 */
public class BackupAndUploadJob extends SingleChannelBaseJob {

    /**
     * 任务名称
     */
    private static String name="BackupAndUploadJob";

    public BackupAndUploadJob() {
        super(name);
    }


    public static String getName() {
        return name;
    }

    @Override
    public void closeJob() {

    }

    @Override
   public void doCollect(JobExecutionContext jobExecutionContext) {
        try {
            Collection<BackupTaskModel> backupTaskModels = GlobleImpl.getGlobleCfgStatic().getBackupTaskModels();
            if (backupTaskModels!=null){
                for(BackupTaskModel backupTaskModel:backupTaskModels){
                    if(backupTaskModel!=null){
                        BackupAndUpload.dowork(backupTaskModel,false);
                    }
                }
            }

        }catch (Exception ex){

        }

    }

    @Override
    public BackupAndUploadJob creatObject() {
        return this;
    }



}
