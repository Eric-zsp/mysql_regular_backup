package cn.joyconn.tools.mysqlbackup.task.utils.dump;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.util.Strings;

import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;

public class MariadbDumpTool {
    public static void backup(String hostIP,String port, String userName, String password, String savePath, String databaseName,List<String> tables
        ,boolean compress,String incrementalBasedir,String extParams
        ) {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        // if (!savePath.endsWith(File.separator)) {
        //     savePath = savePath + File.separator;
        // }
        StringBuilder stringBuilder = new StringBuilder();
        // stringBuilder.append("mariabackup  ").append(" --host").append(hostIP).append(" --port=").append(port)
        //             .append(" --no-timestamp --socket=/var/lib/mysql/mysql.sock");
                    
        stringBuilder.append("mariabackup  --defaults-file=/etc/mysql/my.cnf").append(" --host ").append(hostIP).append(" --port ").append(port)
            .append(" --no-timestamp");
        // stringBuilder.append(" --socket=/var/lib/mysql/mysql.sock");
        stringBuilder.append(" --backup --no-lock");
        stringBuilder.append(" --user ").append(userName).append(" --password ").append(password);
        if(compress){
            stringBuilder.append(" --compress ");
        }
        if(Strings.isNotBlank(incrementalBasedir)){            
            stringBuilder.append(" --incremental --incremental-basedir ").append(incrementalBasedir);
        }
        if(tables!=null&&tables.size()>0){
            stringBuilder.append(" --databases \"");
            for(String table:tables){
                stringBuilder.append(databaseName).append(".").append(table).append(" ");
            }
            stringBuilder.append("\"");
        }else{
            stringBuilder.append(" --databases \"").append(databaseName).append("\"");
        }
        if(Strings.isNotBlank(extParams)){
            stringBuilder.append(" ").append(extParams);
        }
        stringBuilder.append(" --target-dir ").append(savePath);
        try {
           Process process = Runtime.getRuntime().exec(stringBuilder.toString());
           int processResult = process.waitFor();
            if (processResult == 0) {// 0 表示线程正常终止。
                LogHelper.logger().info("数据库备份成功");
            }
        } catch (IOException e) {
            LogHelper.logger().info("数据库备份异常");
            e.printStackTrace();
        } catch (InterruptedException e) {
            LogHelper.logger().info("数据库备份异常");
            e.printStackTrace();
        }
    }

}
