package cn.joyconn.tools.mysqlbackup.task.jobs.baseJob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SingletonBaseJob implements Job {

    static Map<String,SingletonBaseJob> singletonBaseJobObjectMap = new ConcurrentHashMap<>();
    static ReentrantLock lock = new ReentrantLock();

    public abstract void doExecute(JobExecutionContext jobExecutionContext);
    public abstract SingletonBaseJob creatObject();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String singletonKey = jobExecutionContext.getJobDetail().getKey().getName();
        if(!singletonBaseJobObjectMap.containsKey(singletonKey)){
            try{
                lock.lock();
                if(!singletonBaseJobObjectMap.containsKey(singletonKey)){
                    singletonBaseJobObjectMap.put(singletonKey,creatObject());
                }
            }catch (Exception ex){

            }finally {
                lock.unlock();
            }
        }
        if(singletonBaseJobObjectMap.containsKey(singletonKey)){

            SingletonBaseJob job = singletonBaseJobObjectMap.get(singletonKey);
            if(job!=null){
                job.doExecute(jobExecutionContext);
            }
        }
    }
}
