package cn.joyconn.tools.mysqlbackup.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cn.joyconn.tools.mysqlbackup.task.configuration.GlobleImpl;
import cn.joyconn.tools.mysqlbackup.task.handle.TaskBusHandle;


//import com.pi4j.io.gpio.*;

@Component
public class FSRunner implements CommandLineRunner {

    @Autowired
    GlobleImpl globleImpl;

    @Override
    public void run(String... args) throws Exception {

        TaskBusHandle.initAllInquiryTask();


    }




}