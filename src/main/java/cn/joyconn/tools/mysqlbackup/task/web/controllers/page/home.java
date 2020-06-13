package cn.joyconn.tools.mysqlbackup.task.web.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping({"/home"})
public class home {
    @RequestMapping({"index"})
    String index(Map<String, Object> map, HttpServletRequest request) {

        return "/webUI/index";
    }

}
