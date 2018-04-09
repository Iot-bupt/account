package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Customer.CustomerRepository;
import cn.edu.bupt.dao.Tenant.TenantRepository;
import cn.edu.bupt.dao.User.UserCredentialsRepository;
import cn.edu.bupt.dao.User.UserRepository;
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
        tenant.setTitle("Ttitle");
        return tenantRepository.save(tenant).toString();
    }

    @GetMapping(path="/addCustomer") // Map ONLY GET Requests
    public String addNewCustomer () {
        Customer customer = new Customer();
        customer.setTenant(tenantRepository.findById(1).get());
        customer.setTitle("Ctitle");
        return customerRepository.save(customer).toString();
    }

    @GetMapping(path="/findCustomers") // Map ONLY GET Requests
    public String findCustomers () {
        return customerRepository.findCustomersByTenant(tenantRepository.findById(1).get()).toString();
    }

    @GetMapping(path="/addUser") // Map ONLY GET Requests
    public String addNewUser () {
        User user = new User();
        user.setCustomer(customerRepository.findById(1).get());
        user.setTenant(tenantRepository.findById(1).get());
        user.setAuthority("SYS_ADMIN");
        user.setName("User Name");
        return userRepository.save(user).toString();
    }

    @GetMapping(path="/addUserCre") // Map ONLY GET Requests
    public String addNewUserCre () {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser(userRepository.findById(1).get());
        userCredentials.setPassword("password");
        return userCredentialsRepository.save(userCredentials).toString();
    }
}
