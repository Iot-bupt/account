package cn.edu.bupt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zyf on 2018/4/11.
 */
@RestController
@RequestMapping(path= "/test")

public class CustomerController {

    @GetMapping(path= "/Customer")
    public ModelAndView CustomerView(){
        ModelAndView result = new ModelAndView("customer_information");
        return result;
    }
}
