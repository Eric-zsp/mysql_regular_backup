package cn.joyconn.tools.mysqlbackup.task.jobs;


import org.quartz.JobExecutionContext;
import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.handle.BackupAndUpload;
import cn.joyconn.tools.mysqlbackup.task.jobs.baseJob.SingleChannelBaseJob;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;

import java.util.Collection;
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
