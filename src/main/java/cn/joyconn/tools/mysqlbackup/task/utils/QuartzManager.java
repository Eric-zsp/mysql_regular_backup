package cn.joyconn.tools.mysqlbackup.task.utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import cn.joyconn.tools.mysqlbackup.task.models.QuartJobDetailModel;

public class QuartzManager {
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    static ConcurrentHashMap<String,HashMap<String,String>> jobsVarible = new ConcurrentHashMap<>();
    public static HashMap<String,String> getJobVarible(String jobName){
        return jobsVarible.get(jobName);
    }
    public static void putJobVarible(String jobName,HashMap<String,String> jobParams){
        jobsVarible.put(jobName,jobParams);
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param quartJobDetailModel 任务配置对象
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static JobDetail addJob(QuartJobDetailModel quartJobDetailModel, Class jobClass, String cron,boolean runNow) {

        return addJob(quartJobDetailModel.getJobName(),
                quartJobDetailModel.getJobGroupName(),
                quartJobDetailModel.getTiggerName(),
                quartJobDetailModel.getTiggerGroupName(),
                quartJobDetailModel.getJobClass(),
                quartJobDetailModel.getCorn(),runNow);
    }
    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param jobGroupName  任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static JobDetail addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName, Class jobClass, String cron,boolean runNow) {

        return addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron,runNow,null);
    }
    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param jobGroupName  任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     * @param cronScheduleBuilderHandle   对创建job过程中用到的CronScheduleBuilder额外操作处理方法
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static JobDetail addJob(String jobName, String jobGroupName,
                                   String triggerName, String triggerGroupName, Class jobClass, String cron,boolean runNow, Consumer<CronScheduleBuilder> cronScheduleBuilderHandle) {
        JobDetail jobDetail=null;
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                    .build();

            //  sched.schedule().
            //  simpleThreadPool.setThreadCount();
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            // 触发器时间设定
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            if(cronScheduleBuilderHandle!=null){
                try {
                    cronScheduleBuilderHandle.accept(cronScheduleBuilder);
                }catch (Exception ex){

                }
            }
            triggerBuilder.withSchedule(cronScheduleBuilder);

            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            trigger.getMisfireInstruction();
            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);

            triggerBuilder.startNow();
            if(runNow){
                sched.triggerJob(jobDetail.getKey());
            }
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            LogHelper.logger().error("添加一个任务的触发时间异常："+e.getMessage());
//            throw new RuntimeException(e);
        }
        return jobDetail;
    }

    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param cron   时间设置，参考quartz说明文档
     *               @return 1：修改成功，0：修改失败，-1:任务不存在
     */
    public static int modifyJobTime( String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return -1;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);

                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                /** 方式一 ：调用 rescheduleJob 开始 */
                sched.rescheduleJob(triggerKey, trigger);
                // 方式一 ：修改一个任务的触发时间
                /** 方式一 ：调用 rescheduleJob 结束 */


          

                /** 方式二：先删除，然后在创建一个新的Job  */
//                JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
//                Class<? extends Job> jobClass = jobDetail.getJobClass();
//                removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
//                addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
                /** 方式二 ：先删除，然后在创建一个新的Job */
            }
        } catch (Exception e) {
            LogHelper.logger().error("修改一个任务的触发时间异常："+e.getMessage());
            //throw new RuntimeException(e);
            return 0;
        }
        return 1;
    }

    /**
     * @Description: 移除一个任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {
            LogHelper.logger().error("删除一个任务的触发时间异常："+e.getMessage());
//            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public static void startJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        /**
     * @Description:启动所有定时任务
     */
    public static void startJob(String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return ;
            }
            sched.triggerJob(trigger.getJobKey());
            
        } catch (Exception e) {
            LogHelper.logger().error("触发任务失败："+e.getMessage());
            //throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     */
    public static JobDetail getJobDetail(String jobName, String jobGroupName,String triggerName, String triggerGroupName) {
        JobDetail jobDetail=null;
        try {
            
            Scheduler sched = schedulerFactory.getScheduler();
            
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            Trigger trigger = sched.getTrigger(triggerKey);
            if (trigger == null) {
                return jobDetail;
            }
            jobDetail = sched.getJobDetail(trigger.getJobKey());
            
        } catch (Exception e) {
            LogHelper.logger().error("触发任务失败："+e.getMessage());
            //throw new RuntimeException(e);
        }
        return jobDetail;
    }

    /**
     * 
     */
    public static Trigger getTrigger(String triggerName, String triggerGroupName) {
        Trigger trigger=null;
        try {
            
            Scheduler sched = schedulerFactory.getScheduler();
            
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);            
             trigger = sched.getTrigger(triggerKey);
            
            
        } catch (Exception e) {
            LogHelper.logger().error("触发任务失败："+e.getMessage());
            //throw new RuntimeException(e);
        }
        return trigger;
    }

//    public static int getThreadPool(){
//        schedulerFactory.
//    }
}
