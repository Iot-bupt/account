package cn.edu.bupt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by CZX on 2018/9/14.
 */
@Controller
@Slf4j
@RequestMapping("/api/v1/account")  //方便获取static下资源
public class NavigationController {

    @RequestMapping("/home")
    public String  index() {
        return "template/home";
    }

    @RequestMapping("/rolePool")
    public String  getRolePool() {
        return "template/RolePool";
    }

    @RequestMapping("/userPool")
    public String  getUserPool() {
        return "template/UserPool";
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null ,'PERMISSION_TEST')")
    @ResponseBody
    public String testPermission(){
        return "Permission Test!";
    }
}
