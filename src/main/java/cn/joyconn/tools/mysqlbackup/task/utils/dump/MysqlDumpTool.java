package cn.joyconn.tools.mysqlbackup.task.utils.dump;

import java.io.File;
import java.io.IOException;
import cn.joyconn.tools.mysqlbackup.task.utils.LogHelper;

public class MysqlDumpTool {
    public static void backup(String hostIP,String port, String userName, String password, String savePath,String fileName, String databaseName) {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mysqldump ")/*.append(" --opt")*/.append(" -h").append(hostIP).append(" -P").append(port);
        stringBuilder.append(" --user=").append(userName).append(" --password=").append(password) .append(" --events  --routines --triggers");
        stringBuilder.append(" --default-character-set=utf8 ").append(databaseName)
                .append(" |gzip ")
                .append(" >").append(savePath+fileName);
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
