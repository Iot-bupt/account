package cn.edu.bupt.controller;

import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.Tenant.TenantService;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.entity.Authority;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * Created by CZX on 2018/4/13.
 */
@RestController
@RequestMapping("/api/v1")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserCredentialsService userCredentialsService;

    public static final String USER_ID = "userId";
    public static final String YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION = "You don't have permission to perform this operation!";
    public static final String YOU_MUST_SPECIFY_THE_PASSWORD = "You must specify the password for new users!";
    public static final String USER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING = "User ID should be specified when updating!";

    @RequestMapping(value = "/user",params = {"userId"}, method = RequestMethod.GET)
    @ResponseBody
    public String getUserById(@RequestParam Integer userId) throws IOTException{
        try {
            SecurityUser authUser = getCurrentUser();
            if (authUser.getAuthority() == Authority.CUSTOMER_USER && !authUser.getId().equals(userId)) {
                throw new IOTException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                        IOTErrorCode.PERMISSION_DENIED);
            }
            return checkUserId(userId).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    权限：SYS_ADMIN
//    POST请求Headers中Content-Type为application/json，Body为raw形式的Json。
//    eg.{"tenant_id":"1","name":"User1 Name", "additional_info":"", "email":"12test@qq.com","password":"123456"}
    @RequestMapping(value = "/tenantAdmin", method = RequestMethod.POST)
    @ResponseBody
    public String createTenantAdmin(@RequestBody String userInfo) throws IOTException {
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        User user = Json2User(userString);
        user.setTenant(tenantService.findTenantById(userString.get("tenant_id").getAsInt()));
        user.setAuthority(Authority.TENANT_ADMIN);
        user.setCustomer(customerService.findCustomerById(1));
        try {
            if (userString.get("password").getAsString() == null) {
                throw new IOTException(YOU_MUST_SPECIFY_THE_PASSWORD,
                        IOTErrorCode.BAD_REQUEST_PARAMS);
            }
            User savedUser = checkNotNull(userService.saveUser(user));
            UserCredentials userCredentials = new UserCredentials(savedUser,passwordEncoder.encode(userString.get("password").getAsString()));
            userCredentialsService.saveUserCredentials(userCredentials);
            return savedUser.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    权限：Tenant_admin
//    eg.{"customer_id":"2","name":"User1 Name", "additional_info":"", "email":"12test@qq.com","password":"123456"}
    @RequestMapping(value = "/customerUser", method = RequestMethod.POST)
    @ResponseBody
    public String createCustomerUser(@RequestBody String userInfo) throws IOTException {
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        User user = Json2User(userString);
        user.setCustomer(customerService.findCustomerById(userString.get("customer_id").getAsInt()));
        user.setAuthority(Authority.CUSTOMER_USER);
        try {
            if (userString.get("password").getAsString() == null) {
                throw new IOTException(YOU_MUST_SPECIFY_THE_PASSWORD,
                        IOTErrorCode.BAD_REQUEST_PARAMS);
            }
            user.setTenant(getCurrentUser().getTenant());
            User savedUser = checkNotNull(userService.saveUser(user));
            UserCredentials userCredentials = new UserCredentials(savedUser,passwordEncoder.encode(userString.get("password").getAsString()));
            userCredentialsService.saveUserCredentials(userCredentials);
            return savedUser.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    eg.{"id":"1", "customer_id":"2","name":"User1 Name", "additional_info":"", "email":"12test@qq.com"}
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public String updateUser(@RequestBody String userInfo) throws IOTException {
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        if(userString.get("id").getAsString().equals("")) {
            throw new IOTException(USER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING,
                    IOTErrorCode.BAD_REQUEST_PARAMS);
        }
        User user = userService.findUserById(userString.get("id").getAsInt());
        user.setEmail(userString.get("email").getAsString());
        user.setName(userString.get("name").getAsString());
        user.setAdditional_info(userString.get("additional_info").getAsString());
        try {
            SecurityUser authUser = getCurrentUser();
            if (authUser.getAuthority() == Authority.CUSTOMER_USER && !authUser.getId().equals(user.getId())) {
                throw new IOTException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                        IOTErrorCode.PERMISSION_DENIED);
            }
            if (authUser.getAuthority() == Authority.TENANT_ADMIN) {
                user.setTenant(getCurrentUser().getTenant());
                user.setCustomer(customerService.findCustomerById(userString.get("customer_id").getAsInt()));
            }
            User savedUser = checkNotNull(userService.saveUser(user));
            return savedUser.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    //@PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @Transactional
    @RequestMapping(value = "/user",params = {"userId"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@RequestParam Integer userId) throws IOTException {
        checkParameter(USER_ID, userId);
        try {
            checkUserId(userId);
            userService.deleteUser(userId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/tenant", params = { "tenantId","users","limit","page"}, method = RequestMethod.GET)
    @ResponseBody
    public String getTenantAdmins(
            @RequestParam("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam int page) throws IOTException {
        checkParameter("tenantId", tenantId);
        try {
            return checkNotNull(userService.findTenantAdmins(page,limit,tenantId).getContent().toString());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer", params = { "customerId","users","limit","page" }, method = RequestMethod.GET)
    @ResponseBody
    public String getCustomerUsers(
            @RequestParam("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam int page) throws IOTException {
        checkParameter("customerId", customerId);
        try {
            checkCustomerId(customerId);
            return checkNotNull(userService.findCustomerUsers(page,limit,customerId).getContent().toString());
        } catch (Exception e) {
            throw handleException(e);
        }
    }
    private User Json2User(JsonObject userString){
        User user = new User();
        user.setEmail(userString.get("email").getAsString());
        user.setName(userString.get("name").getAsString());
        user.setAdditional_info(userString.get("additional_info").getAsString());
//        user.setCustomer(customerService.findCustomerById(userString.get("customer_id").getAsInt()));
//        user.setTenant(tenantService.findTenantById(userString.get("tenant_id").getAsInt()));
//        user.setAuthority(Authority.parse(userString.get("authority").getAsString()));
        return user;
    }

}
