//package cn.joyconn.tools.mysqlbackup.task.web.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//
///**
// * Created by Eric.Zhang on 2017/7/20.
// */
//@Configuration
//@EnableSwagger2
//public class Swagger_openApi_Config {
//
//
//    @Bean
//    public Docket openApi_2_0() {
//
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("openApi_2_0")
//                .apiInfo(openApi2_0Info())
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("cn.joyconn.tools.iotdata.finalstation.web.controllers.api"))
//                .build();
//    }
//    private ApiInfo openApi2_0Info() {
//        return new ApiInfoBuilder()
//                .title("终端设备数据调试接口")//大标题
//                .description("OpenAPI")//详细描述
//                .version("2.0")//版本
////                .termsOfServiceUrl("NO terms of service")
////                .contact(new Contact("泽佑","www.zybros.com", "787591269@qq.com"))//作者
////                .license("The Apache License, Version 2.0")
////                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
//                .build();
//    }
//
//
//
//
//
//
//
//}
