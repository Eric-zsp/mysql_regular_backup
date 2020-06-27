package cn.joyconn.tools.mysqlbackup.task.models;

public class QuartJobDetailModel {
    private  String jobName;
    private String jobGroupName;
    private  String tiggerName;
    private String tiggerGroupName;
    private Class jobClass;
    private String corn;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getTiggerName() {
        return tiggerName;
    }

    public void setTiggerName(String tiggerName) {
        this.tiggerName = tiggerName;
    }

    public String getTiggerGroupName() {
        return tiggerGroupName;
    }

    public void setTiggerGroupName(String tiggerGroupName) {
        this.tiggerGroupName = tiggerGroupName;
    }

    public Class getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class jobClass) {
        this.jobClass = jobClass;
    }

    public String getCorn() {
        return corn;
    }

    public void setCorn(String corn) {
        this.corn = corn;
    }
}
