package cn.edu.bupt.controller;

import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.Tenant.TenantService;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.entity.Authority;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CZX on 2018/4/13.
 */
@RestController
@RequestMapping("/api")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TenantService tenantService;

    public static final String USER_ID = "userId";
    public static final String YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION = "You don't have permission to perform this operation!";

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String getUserById(@PathVariable(USER_ID) Integer userId) throws IOTException{
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

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public String saveUser(@RequestBody String userInfo) throws IOTException {
        User user = Json2User(userInfo);
        try {
            SecurityUser authUser = getCurrentUser();
            if (authUser.getAuthority() == Authority.CUSTOMER_USER && !authUser.getId().equals(user.getId())) {
                throw new IOTException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                        IOTErrorCode.PERMISSION_DENIED);
            }
            if (getCurrentUser().getAuthority() == Authority.TENANT_ADMIN) {
                user.setTenant(getCurrentUser().getTenant());
            }
            User savedUser = checkNotNull(userService.saveUser(user));
            return savedUser.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private User Json2User(String userInfo){
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        User user = new User();
        if(!userString.get("id").getAsString().equals("")) {
            user.setId(userString.get("id").getAsInt());
        }
        user.setEmail(userString.get("email").getAsString());
        user.setName(userString.get("name").getAsString());
        user.setAdditional_info(userString.get("additional_info").getAsString());
        user.setCustomer(customerService.findCustomerById(userString.get("customer_id").getAsInt()));
        user.setTenant(tenantService.findTenantById(userString.get("tenant_id").getAsInt()));
        user.setAuthority(Authority.parse(userString.get("authority").getAsString()));
        return user;
    }

}
