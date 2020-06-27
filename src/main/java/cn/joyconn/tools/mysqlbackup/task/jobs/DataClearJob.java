package cn.joyconn.tools.mysqlbackup.task.jobs;


import org.quartz.JobExecutionContext;
import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.jobs.baseJob.SingleChannelBaseJob;
import cn.joyconn.tools.mysqlbackup.task.jobs.baseJob.SingletonBaseJob;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * 历史数据清理
 */
public class DataClearJob extends SingleChannelBaseJob {

    /**
     * 任务名称
     */
    private static String name="DataClearJob";

    public DataClearJob() {
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
            Calendar calendar = Calendar.getInstance();
            Date now =new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateTimeStr = dateFormat.format(new Date());
            try {
                Collection<BackupTaskModel> backupTaskModels = GlobleImpl.getGlobleCfgStatic().getBackupTaskModels();
                if (backupTaskModels!=null){                    
                    // String _fileName = "";
                    // File file;
                    // File[] files;
                    String hostPortDirName;
                    File baseDir = new File(GlobleImpl.getGlobleCfgStatic().getSavepath());
                    File[] hostDirs;
                    File[] saveDirs;
                    String lastDateDir;
                    String saveDirTime;
                    if(baseDir!=null && baseDir.exists() && baseDir.isDirectory()){
                        hostDirs = baseDir.listFiles();
                        for(File hostDir : hostDirs){                       
                            if(hostDir.isDirectory()){
                                for(BackupTaskModel backupTaskModel : backupTaskModels){
                                    if(backupTaskModel!=null){
                                        hostPortDirName=backupTaskModel.getP_host()+"-"+backupTaskModel.getP_port();
                                        if(hostPortDirName.equals(hostDir.getName())){
                                            calendar.setTime(now);
                                            calendar.add(Calendar.DATE,0-backupTaskModel.getP_retentionTime());                                             
                                            lastDateDir = dateFormat.format(calendar.getTime());   
                                            saveDirs = hostDir.listFiles();
                                            if(saveDirs!=null){
                                                for(File saveDir : saveDirs){
                                                    if(saveDir.length()>lastDateDir.length()){
                                                        saveDirTime=saveDir.getName().substring(0,lastDateDir.length());
                                                        if(saveDirTime.compareTo(lastDateDir)<0){
                                                            saveDir.delete();
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } 
                    
                }
            }catch (Exception ex){
                LogHelper.logger().error(ex.getMessage());
            }

        }catch (Exception ex){
            LogHelper.logger().error(ex.getMessage());
        }
    }



    @Override
    public SingletonBaseJob creatObject() {
        return this;
    }
}
