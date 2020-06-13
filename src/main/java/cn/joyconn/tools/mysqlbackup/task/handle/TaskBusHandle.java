package cn.joyconn.tools.mysqlbackup.task.handle;


import org.quartz.JobDetail;
import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.jobs.BackupAndUploadJob;
import cn.joyconn.tools.mysqlbackup.task.jobs.DataClearJob;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;
import cn.joyconn.tools.mysqlbackup.task.utils.QuartzManager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//任务总线
public class TaskBusHandle {
    private static QuartzManager quartzManager;
    public static void setQuartzManager (QuartzManager qm){
        quartzManager = qm;
    }

    static ConcurrentHashMap<String,HashMap<String,String>> jobsVarible = new ConcurrentHashMap<>();




    /**
     * 启动或重启当前内存中的所有inquinquiryTask
     */
    public static void initAllInquiryTask(){
        startBackupAndUploadTask();
        startDataClearTask();

    }


    public static HashMap<String,String> getJobVarible(String jobName){
        return jobsVarible.get(jobName);
    }

    static void startBackupAndUploadTask( ){
        try {
            String corn=GlobleImpl.getGlobleCfgStatic().getRunCorn();
            //region 数据采集任务
            String jobName="BackupAndUploadJob";
            String jobGroupName="BackupAndUploadJob";
            String tiggerName=jobName+"_Tigger";
            String tiggerGroupName= "BackupAndUploadJobTigger";
            Class jobClass = BackupAndUploadJob.class;
            int update=quartzManager.modifyJobTime(jobName,jobGroupName,tiggerName,tiggerGroupName,corn);
            if(update<0){//任务不存在
                JobDetail jobDetail = quartzManager.addJob(jobName,
                        jobGroupName,
                        tiggerName,
                        tiggerGroupName,
                        jobClass,
                        corn);//每两秒执行一次

            }
            //endregion


        }catch (Exception ex){
            LogHelper.logger().error("启动BackupAndUploadJob时失败,");
        }
    }

    static void startDataClearTask( ){
        try {
            String corn=GlobleImpl.getGlobleCfgStatic().getRunCorn();
            //region 数据采集任务
            String jobName="DataClearJob";
            String jobGroupName="DataClearJob";
            String tiggerName=jobName+"_Tigger";
            String tiggerGroupName= "DataClearJobTigger";
            Class jobClass = DataClearJob.class;
            int update=quartzManager.modifyJobTime(jobName,jobGroupName,tiggerName,tiggerGroupName,corn);
            if(update<0){//任务不存在
                JobDetail jobDetail = quartzManager.addJob(jobName,
                        jobGroupName,
                        tiggerName,
                        tiggerGroupName,
                        jobClass,
                        corn);//每两秒执行一次

            }
            //endregion


        }catch (Exception ex){
            LogHelper.logger().error("启动DataClearJob时失败,");
        }
    }
}
