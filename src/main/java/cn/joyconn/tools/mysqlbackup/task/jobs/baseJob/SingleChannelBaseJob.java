package cn.joyconn.tools.mysqlbackup.task.jobs.baseJob;

import org.quartz.JobExecutionContext;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;

/**
 * 单通道Job (每个Job对象执行时会判断上次任务是否执行完，未执行完则跳过本次任务)
 */
public abstract class SingleChannelBaseJob extends SingletonBaseJob {
    public SingleChannelBaseJob(String name){
        this.setJobName(name);
    }
    Boolean jobDoing =false;//任务是否处于执行中
    /**
     * 任务名称
     */
    private  String jobName;



    @Override
    public void doExecute(JobExecutionContext jobExecutionContext) {
        //String _inquiryTaskName=jobExecutionContext.getJobDetail().getKey().getName();

//        Thread th=Thread.currentThread();
//        LogHelper.logger().info(jobName+":"+th.getId());

        if(jobDoing){
            return;
        }
        try {
            jobDoing=true;
            doCollect(jobExecutionContext);

        }catch (Exception ex){
            LogHelper.logger().error(ex.getMessage());
        }finally {
            jobDoing=false;
        }


    }

    public  abstract void closeJob();
    public abstract void doCollect(JobExecutionContext jobExecutionContext);

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
