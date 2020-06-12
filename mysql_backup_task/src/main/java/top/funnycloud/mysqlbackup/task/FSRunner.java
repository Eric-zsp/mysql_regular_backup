package top.funnycloud.mysqlbackup.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.funnycloud.mysqlbackup.task.configuration.GlobleImpl;
import top.funnycloud.mysqlbackup.task.handle.TaskBusHandle;
import top.funnycloud.mysqlbackup.task.jobs.DataClearJob;

import java.text.SimpleDateFormat;

//import com.pi4j.io.gpio.*;

@Component
public class FSRunner implements CommandLineRunner {

    @Autowired
    GlobleImpl globleImpl;

    @Override
    public void run(String... args) throws Exception {

        globleImpl.initAutoWired();
        TaskBusHandle.initAllInquiryTask();


    }




}