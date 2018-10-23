package top.funnycloud.mysqlbackup.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.funnycloud.mysqlbackup.task.configuration.GlobleImpl;
import top.funnycloud.mysqlbackup.task.handle.TaskBusHandle;

import java.text.SimpleDateFormat;

//import com.pi4j.io.gpio.*;

@Component
public class FSRunner implements CommandLineRunner {

    @Autowired
    GlobleImpl globleImpl;

    @Override
    public void run(String... args) throws Exception {
//        if(test1()){
//            test2();
//            return;
//        }
        globleImpl.initAutoWired();
        TaskBusHandle.initAllInquiryTask();


    }


    boolean test1(){
//        int i=0;
//        while(true) {
//            try{
//                Thread.sleep(1000);
//            }catch (Exception ex){}
//            i=0;
//            if(1==0){
//                break;
//            }
//        }
        return true;
    }

    void test2(){
//        new tttt().aaa();
//        Pattern pointFurmal_pointPatten= Pattern.compile("##[0-9a-zA-Z\\-]*##");
//        String sss =" var a=Math.max(##V101UA##,##V101UB##);var b=Math.max(##V101UC##,##V101UB##);var c=Math.max(a,b);(c-(1+4+5)/3)/c;";
//        Matcher matcher = pointFurmal_pointPatten.matcher(sss);
//        while(matcher.find()) {
//            System.out.println(matcher.group());
//            if(matcher.group().equals("##V101UA##")){
//                sss= sss.replace(matcher.group(),"1");
//            }else if(matcher.group().equals("##V101UB##")){
//                sss= sss.replace(matcher.group(),"4");
//            }else if(matcher.group().equals("##V101UC##")){
//                sss= sss.replace(matcher.group(),"5");
//            }
//            System.out.println(matcher.start()+"...."+matcher.end());
//        }
//        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//        String strs = "var a=Math.max(1,4);var b=Math.max(5,4);var c=Math.max(a,b);(c-(1+4+5)/3)/c;";
//        try {
//            Double ddd = Double.valueOf(String.valueOf(jse.eval(strs)));
//            System.out.println(ddd);
//        } catch (Exception t) {
//
//            System.out.println(t.getMessage());
//        }
//        LogHelper.logger().info("\""+"yyyy-MM-dd HH:mm:ss.SSS 123456789012345678901234 abc".substring(0,23)+"\"");
//        LogHelper.logger().info("\""+"yyyy-MM-dd HH:mm:ss.SSS 123456789012345678901234 abc".substring(24,48)+"\"");
//        LogHelper.logger().info("\""+"yyyy-MM-dd HH:mm:ss.SSS 123456789012345678901234 abc".substring(49)+"\"");
//        LogHelper.logger().info("000001119ww00111".replace("0",""));
//        LogHelper.logger().info("0123456789".substring(5,8));

//        InqueryTaskDataModel inqueryTaskDataModel = new InqueryTaskDataModel();
//        inqueryTaskDataModel.setInquriyTaskID(123);
//        inqueryTaskDataModel.setRecordTime(new Date());
//        DataCalculationHandle.getInstance().culPointDataDuledData(inqueryTaskDataModel);

//        String ss= "3133323639353730353937";
//        byte[] commondByteArr = TypeConvert.transOrig2ByteArray(ss);
//        byte[] commondByteArr1 = TypeConvert.toBytes(ss);
//        LogHelper.logger().info(ss);

//        LogHelper.logger("tcpServer").info("TcpServer recvie:");
//        double a = TypeConvert.getHexValue("ffff","uint32",1);
//        Double b = 12.3;
//        String sss = Double.toHexString(b);
//
//        String _pathName = (GlobleCfg.getFinalCfgModel().getDataDir() + "/logs/inqueriyTaskPointData").replace("//", "/");
//        File basePath = new File(_pathName);
//        int i= "2018-03".compareTo("2018-03");
//        int j= "2018-03-02".compareTo("2018-03-02-1.log");
//        int k= "2018-03-03".compareTo("2018-03-02-1.log");
//        int l= "2018-03-02 17:26:02.183".compareTo("2018-03-02.log");
//        int m= "2018-03-02 17:26:02.183".compareTo("2018-03");
//
//        int o= "2018-03-03".compareTo("2018-03-02 17:26:02.183");
//        int p= "2018-03".compareTo("2018-02");
//        LogHelper.logger().info(String.valueOf(sss));
//        LogHelper.logger().info(String.valueOf(a));
//        LogHelper.logger("inqueriyTaskPointData").info("fggfgf");
//        LogHelper.logger("tcpServer").info("111");

//        for(i=0;i<10000;i++){
//
//            LogHelper.logger("inqueriyTaskPointData").info("哈哈哈哈萨萨克大家疯狂拉升JFK垃圾ask领导即使对方尽快哈森林防火的撒尽快回复拉萨阿三的灵魂分厘卡身份卡都是垃圾阿斯顿返回拉萨空间划分等级考试");
//        }
//        while (true){
//            Scanner sc = new Scanner(System.in);
//            String ss = sc.nextLine();
//            String chennelID=TCPServer.getInqueriChannelMap().get(1);
//           Channel ch =  TCPServer.getChannelMap().get(chennelID);
//           ch.writeAndFlush(TypeConvert. toBytes(ss));
//            System.out.println(ss);
//        }

        //给tcpclient发送消息
//        TCPClient tcpClient = new TCPClient("127.0.0.1",20111);
//        // TCPClient tcpClient = new TCPClient("localhost",20111);
//        tcpClient.sendMsg("11");

//        TCPServer.getMap().get("127.0.0.1").writeAndFlush("010300000177047C");
    }



}