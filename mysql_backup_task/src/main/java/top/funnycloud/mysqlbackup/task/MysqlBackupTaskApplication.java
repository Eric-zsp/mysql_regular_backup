package top.funnycloud.mysqlbackup.task;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Created by Eric.Zhang on 2017/9/19.
 */

@ImportResource("classpath:config/*.xml")
@SpringBootApplication
//@EnableScheduling
//@EnableSwagger2
public class MysqlBackupTaskApplication  extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        Starters.startH2Server();
        return application.sources(MysqlBackupTaskApplication.class);

    }

    public static void main(String[] args) {
        SpringApplication.run(MysqlBackupTaskApplication.class, args);
       // System.setProperty(net.sf.ehcache.CacheManager.ENABLE_SHUTDOWN_HOOK_PROPERTY,"true");

//        Starters.startH2Server();
//        SpringApplication app = new SpringApplication(MysqlBackupTaskApplication.class);
//        app.setBannerMode(Banner.Mode.OFF);
//        app.run(args);

    }

}
