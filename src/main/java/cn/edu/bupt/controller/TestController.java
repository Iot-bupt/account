package cn.edu.bupt.controller;

import cn.edu.bupt.dao.CustomerRepository;
import cn.edu.bupt.dao.TenantRepository;
import cn.edu.bupt.dao.UserCredentialsRepository;
import cn.edu.bupt.dao.UserRepository;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CZX on 2018/4/8.
 */
@RestController
@RequestMapping(path="/test")
public class TestController {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @GetMapping(path="/addTenant") // Map ONLY GET Requests
    public String addNewTenant () {
        Tenant tenant = new Tenant();
        //tenant.setId("TID");
        tenant.setTitle("Ttitle");
        return tenantRepository.save(tenant).toString();
    }

    @GetMapping(path="/addCustomer") // Map ONLY GET Requests
    public String addNewCustomer () {
        Customer customer = new Customer();
        //customer.setId(1);
        customer.setTenant_id(1);
        customer.setTitle("Ctitle");
        return customerRepository.save(customer).toString();
    }

    @GetMapping(path="/addUser") // Map ONLY GET Requests
    public String addNewUser () {
        User user = new User();
        user.setId(1);
        user.setCustomer_id(1);
        user.setTenant_id(1);
        user.setAuthority("SYS_ADMIN");
        user.setName("User Name");
        return userRepository.save(user).toString();
    }

    @GetMapping(path="/addUserCre") // Map ONLY GET Requests
    public String addNewUserCre () {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setId(1);
        userCredentials.setUser_id(1);
        userCredentials.setPassword("password");
        return userCredentialsRepository.save(userCredentials).toString();
    }
}
