package cn.joyconn.tools.mysqlbackup.task.jobs;


import org.quartz.JobExecutionContext;
import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.jobs.baseJob.SingleChannelBaseJob;
import cn.joyconn.tools.mysqlbackup.task.jobs.baseJob.SingletonBaseJob;
import cn.joyconn.tools.mysqlbackup.task.models.BackupTaskModel;

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
            try {
                Collection<BackupTaskModel> backupTaskModels = GlobleImpl.getGlobleCfgStatic().getBackupTaskModels();
                if (backupTaskModels!=null){
                    for(BackupTaskModel backupTaskModel:backupTaskModels){
                        if(backupTaskModel!=null){
                            calendar.setTime(now);
                            calendar.add(Calendar.DATE,0-backupTaskModel.getP_retentionTime());
                            File file = new File(GlobleImpl.getGlobleCfgStatic().getSavepath()+"/"+backupTaskModel.getP_dbname());
                            if (file.exists()) {
                                File[] files = file.listFiles();
                                if (files != null) {
                                    String _fileName = dateFormat.format(calendar.getTime())+".sql.gz";
                                    for(File f:files){
                                        if(f.isFile()){
                                            if(_fileName.compareTo(f.getName())>0){
                                               f.delete();
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }catch (Exception ex){

            }

        }catch (Exception ex){

        }
    }



    @Override
    public SingletonBaseJob creatObject() {
        return this;
    }
}
