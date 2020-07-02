package cn.joyconn.tools.mysqlbackup.task.handle;

import org.quartz.JobDetail;
import org.quartz.Trigger;

import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.jobs.BackupAndUploadJob;
import cn.joyconn.tools.mysqlbackup.task.jobs.DataClearJob;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;
import cn.joyconn.tools.mysqlbackup.task.utils.QuartzManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//任务总线
public class TaskBusHandle {
    // private static QuartzManager quartzManager;
    // public static void setQuartzManager (QuartzManager qm){
    //     quartzManager = qm;
    // }

    static ConcurrentHashMap<String,HashMap<String,String>> jobsVarible = new ConcurrentHashMap<>();




    /**
     * 启动或重启当前内存中的所有inquinquiryTask
     */
    public static void initAllInquiryTask(){
        startBackupAndUploadTask2();
        startDataClearTask();

    }


    public static HashMap<String,String> getJobVarible(String jobName){
        return jobsVarible.get(jobName);
    }

    static void startBackupAndUploadTask( ){
        // try {
        //     String corn=GlobleImpl.getGlobleCfgStatic().getRunCorn();
        //     //region 数据采集任务
        //     String jobName="BackupAndUploadJob";
        //     String jobGroupName="BackupAndUploadJob";
        //     String triggerName=jobName+"_Trigger";
        //     String triggerGroupName= "BackupAndUploadJobTrigger";
        //     Class jobClass = BackupAndUploadJob.class;
        //     Trigger trigger = QuartzManager.getTrigger(triggerName, triggerGroupName);  
        //     if(trigger == null){//任务不存在
        //         JobDetail jobDetail = QuartzManager.addJob(jobName,
        //                 jobGroupName,
        //                 triggerName,
        //                 triggerGroupName,
        //                 jobClass,
        //                 corn,false);//每两秒执行一次

        //     }  else {
        //         QuartzManager.modifyJobTime(
        //             triggerName,
        //             triggerGroupName,
        //             backupTaskModel.getP_corn());
        // }
        //     //endregion


        // }catch (Exception ex){
        //     LogHelper.logger().error("启动BackupAndUploadJob时失败,");
        // }
    }
    static void startBackupAndUploadTask2( ){
        try {
            Collection<BackupTaskModel> backupTaskModels = GlobleImpl.getGlobleCfgStatic().getBackupTaskModels();
            if (backupTaskModels!=null){
                for(BackupTaskModel backupTaskModel:backupTaskModels){
                    if(backupTaskModel!=null){
                        startOrResetBackupAndUploadRemote(backupTaskModel.getP_id());
                        
                    }
                }
            }


        }catch (Exception ex){
            LogHelper.logger().error("启动BackupAndUploadJob时失败,");
        }
    }

    
    /**
     * 启动或修改一个计划任务
     */
    public   static void startOrResetBackupAndUploadRemote( String id){
        try {

            BackupTaskModel backupTaskModel = GlobleImpl.getGlobleCfgStatic().getBackupTaskModel(id);  
            if(backupTaskModel!=null){
               
                //region 数据采集任务
                String jobName="BackupAndUploadJob"+backupTaskModel.getP_id();
                String jobGroupName="BackupAndUploadJob";
                String triggerName=jobName+"_Trigger";
                String triggerGroupName= "BackupAndUploadJobTrigger";
                Class jobClass = BackupAndUploadJob.class;
                Trigger trigger = QuartzManager.getTrigger(triggerName, triggerGroupName);                               
                if(trigger == null){//任务不存在
                    HashMap<String,String> map = new HashMap<>();
                    map.put("taskID",String.valueOf(id));
                    map.put("quartz_jobName",jobName);
                    map.put("quartz_jobGroupName",jobGroupName);
                    map.put("quartz_triggerName",triggerName);
                    map.put("quartz_triggerGroupName",triggerGroupName);
                    QuartzManager.putJobVarible(jobName,map);
                    JobDetail jobDetail = QuartzManager.addJob(jobName,
                            jobGroupName,
                            triggerName,
                            triggerGroupName,
                            jobClass,
                            backupTaskModel.getP_corn(),false,
                            (a)->a.withMisfireHandlingInstructionDoNothing());

                }  else {
                    QuartzManager.modifyJobTime(
                            triggerName,
                            triggerGroupName,
                            backupTaskModel.getP_corn());
                }
                //endregion
            }



        }catch (Exception ex){
            LogHelper.logger().error("启动一个或重启一个一个task时失败,backup task ID:"+id);
        }
    }
    
    /**
     *  触发一个计划任务
     * @param id
     */
    public   static void triggerBackupAndUploadRemote( String id){
        try {

            BackupTaskModel backupTaskModel = GlobleImpl.getGlobleCfgStatic().getBackupTaskModel(id);  
            if(backupTaskModel!=null){
               
                //region 数据采集任务
                String jobName="BackupAndUploadJob"+backupTaskModel.getP_id();
                String jobGroupName="BackupAndUploadJob";
                String triggerName=jobName+"_Trigger";
                String triggerGroupName= "BackupAndUploadJobTrigger";
                QuartzManager.startJob(triggerName,triggerGroupName);                               
                
                //endregion
            }



        }catch (Exception ex){
            LogHelper.logger().error("启动一个或重启一个一个task时失败,backup task ID:"+id);
        }
    }
    

    public   static void deleteBackupAndUploadRemote( String id){
        try {

            BackupTaskModel backupTaskModel = GlobleImpl.getGlobleCfgStatic().getBackupTaskModel(id);  
            if(backupTaskModel!=null){
               
                //region 数据采集任务
                String jobName="BackupAndUploadJob"+backupTaskModel.getP_id();
                String jobGroupName="BackupAndUploadJob";
                String triggerName=jobName+"_Trigger";
                String triggerGroupName= "BackupAndUploadJobTrigger";
                Class jobClass = BackupAndUploadJob.class;
                QuartzManager.removeJob(jobName,jobGroupName,triggerName,triggerGroupName);                                
                
                //endregion
            }



        }catch (Exception ex){
            LogHelper.logger().error("删除一个task时失败,backup task ID:"+id);
        }
    }


    static void startDataClearTask( ){
        try {
            String corn= "0 0 1 * * ?";
            //region 数据采集任务
            String jobName="DataClearJob";
            String jobGroupName="DataClearJob";
            String triggerName=jobName+"_Trigger";
            String triggerGroupName= "DataClearJobTrigger";
            Class jobClass = DataClearJob.class;
            Trigger trigger = QuartzManager.getTrigger(triggerName, triggerGroupName);   
            if(trigger == null){//任务不存在
                JobDetail jobDetail = QuartzManager.addJob(jobName,
                        jobGroupName,
                        triggerName,
                        triggerGroupName,
                        jobClass,
                        corn,false);

            }  else {
                QuartzManager.modifyJobTime(
                        triggerName,
                        triggerGroupName,
                        corn);
            }
            //endregion


        }catch (Exception ex){
            LogHelper.logger().error("启动DataClearJob时失败,");
        }
    }
}
