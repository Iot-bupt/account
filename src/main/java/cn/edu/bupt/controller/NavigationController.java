package cn.edu.bupt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by CZX on 2018/9/14.
 */
@Controller
@Slf4j
//@RequestMapping("/api/v1/account")  //方便获取static下资源
public class NavigationController {

    @RequestMapping("/home")
    public String  index() {
        return "home";
    }

    @RequestMapping("/rolePool")
    public String  getRolePool() {
        return "RolePool";
    }

    @RequestMapping("/userPool")
    public String  getUserPool() {
        return "UserPool";
    }
}
