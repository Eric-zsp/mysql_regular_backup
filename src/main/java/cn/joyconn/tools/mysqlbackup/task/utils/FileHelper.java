package cn.joyconn.tools.mysqlbackup.task.utils;

import java.io.*;

public class FileHelper {
    public static String readStrFromFile(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException var6) {
            System.err.println("The OS does not support " + encoding);
            var6.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String fileName, String content) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}
