package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Customer.CustomerRepository;
import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.Tenant.TenantRepository;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsRepository;
import cn.edu.bupt.dao.User.UserRepository;
import cn.edu.bupt.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
    public List<Customer> findCustomers () {
        return customerService.findCustomersByTenantId(0,2,1).getContent();
    }

    @GetMapping(path="/findUsersByC") // Map ONLY GET Requests
    public List<User> findUsersByC () {
        return userService.findCustomerUsers(0,2,1).getContent();
    }

    @GetMapping(path="/findTAdmin") // Map ONLY GET Requests
    public List<User> findTenantAdmin () {
        return userService.findTenantAdmins(0,2,1).getContent();
    }

    @GetMapping(path="/addUser") // Map ONLY GET Requests
    public String addNewUser () {
        User user = new User();
        user.setCustomer(customerRepository.findById(1).get());
        user.setTenant(tenantRepository.findById(2).get());
        user.setAuthority(Authority.SYS_ADMIN);
        user.setName("User Name");
        user.setEmail("test7@test.com");
        User savedUser = userRepository.save(user);
        String encodedPassword = passwordEncoder.encode("password");
        userCredentialsRepository.save(new UserCredentials(savedUser,encodedPassword));
        return savedUser.toString();
    }

    @GetMapping(path="/addUserCre") // Map ONLY GET Requests
    public String addNewUserCre () {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser(userRepository.findById(1).get());
        userCredentials.setPassword("password");
        return userCredentialsRepository.save(userCredentials).toString();
    }
}
