package cn.joyconn.tools.mysqlbackup.task.utils;

import java.io.IOException;

public class ProcessUtil {
    public static Process doProcess(String cmd) throws IOException {
        Process process = null;
        String os = System.getProperty("os.name"); 
        if(os.toLowerCase().startsWith("win")){ 
            process = Runtime.getRuntime().exec(new String[]{"cmd", "/c" ,cmd});
        }else{
            process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c" ,cmd});
        }
        return process;
    }
}