package cn.edu.bupt.controller;

import cn.edu.bupt.Security.HttpUtil;
import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.Tenant.TenantService;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.entity.Authority;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import cn.edu.bupt.entity.UserField;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by CZX on 2018/4/13.
 */
@RestController
@RequestMapping(value = "/api/v1/account")
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
    public static final String YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION = "没有权限执行该操作！";
    public static final String YOU_MUST_SPECIFY_THE_PASSWORD = "请指定新用户的密码！";
    public static final String USER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING = "User ID should be specified when updating!";

    @ApiOperation(value = "根据UserId获取User")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getUserById')")
//    @PreAuthorize("hasPermission('USER')")
    @RequestMapping(value = "/user",params = {"userId"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
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

    @ApiOperation(value = "根据TenantId获取所有User")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getUserByTenantId')")
    @RequestMapping(value = "/tenant/user",params = {"tenantId"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getUserByTenantId(@RequestParam Integer tenantId) throws IOTException{
        return userService.findUserByTenantId(tenantId).toString();
    }

//    权限：SYS_ADMIN
//    POST请求Headers中Content-Type为application/json，Body为raw形式的Json。
//    eg.{"tenant_id":"1","name":"User1 Name", "additional_info":"", "email":"12test@qq.com","password":"123456"}
    @ApiOperation(value = "创建租户管理员")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'createTenantAdmin') ")
    @RequestMapping(value = "/tenantAdmin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String createTenantAdmin(@RequestBody String userInfo) throws IOTException {
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        List<UserField> fields = getFields(new JsonParser().parse(userInfo).getAsJsonObject());
        User user = Json2User(userString);
        user.setTenantId(userString.get("tenant_id").getAsInt());
        user.setAuthority(Authority.TENANT_ADMIN);
        user.setCustomerId(1);
        if(userString.has("phone")&&!userString.get("phone").getAsString().equals("")){
            user.setPhone(userString.get("phone").getAsString());
        }
        if(userString.has("we_chat")&&!userString.get("we_chat").getAsString().equals("")) {
            user.setWe_chat(userString.get("we_chat").getAsString());
        }
        user.setUserFields(fields);
        try {
            if (userString.get("password").getAsString().isEmpty()) {
                throw new IOTException(YOU_MUST_SPECIFY_THE_PASSWORD,
                        IOTErrorCode.BAD_REQUEST_PARAMS);
            }
            checkNotNull(userService.saveUser(user));
            UserCredentials userCredentials = new UserCredentials(user.getId(),passwordEncoder.encode(userString.get("password").getAsString()));
            userCredentialsService.saveUserCredentials(userCredentials);
            return user.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    权限：Tenant_admin
//    eg.{"customer_id":"2","name":"User1 Name", "additional_info":"", "email":"12test@qq.com","password":"123456"}
    @ApiOperation(value = "创建普通用户")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'createCustomerUser')")
    @RequestMapping(value = "/customerUser", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String createCustomerUser(@RequestBody String userInfo) throws IOTException {
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        List<UserField> fields = getFields(new JsonParser().parse(userInfo).getAsJsonObject());

        User user = Json2User(userString);
        user.setCustomerId(userString.get("customer_id").getAsInt());
        user.setAuthority(Authority.CUSTOMER_USER);
        if(userString.has("phone")){
            user.setPhone(userString.get("phone").getAsString());
        }
        if(userString.has("we_chat")) {
            user.setWe_chat(userString.get("we_chat").getAsString());
        }
        user.setUserFields(fields);
        try {
            if (userString.get("password").getAsString() == null) {
                throw new IOTException(YOU_MUST_SPECIFY_THE_PASSWORD,
                        IOTErrorCode.BAD_REQUEST_PARAMS);
            }
            user.setTenantId(getCurrentUser().getTenantId());
            checkNotNull(userService.saveUser(user));
            UserCredentials userCredentials = new UserCredentials(user.getId(),passwordEncoder.encode(userString.get("password").getAsString()));
            userCredentialsService.saveUserCredentials(userCredentials);
            return user.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    eg.{"id":"1", "customer_id":"2","name":"User1 Name", "additional_info":"", "email":"12test@qq.com"}
    @ApiOperation(value = "更新用户信息")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'updateUser')")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void updateUser(@RequestBody String userInfo) throws IOTException {
        JsonObject userString = new JsonParser().parse(userInfo).getAsJsonObject();
        List<UserField> fields = getFields(new JsonParser().parse(userInfo).getAsJsonObject());
        if(userString.get("id").getAsString().equals("")) {
            throw new IOTException(USER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING,
                    IOTErrorCode.BAD_REQUEST_PARAMS);
        }
        User user = userService.findUserById(userString.get("id").getAsInt());
        user.setEmail(userString.get("email").getAsString());
        user.setName(userString.get("name").getAsString());
        user.setAdditional_info(userString.get("additional_info").getAsString());
        // TODO
        if(userString.has("phone")){
            user.setPhone(userString.get("phone").getAsString());
        }
        if(userString.has("we_chat")) {
            user.setWe_chat(userString.get("we_chat").getAsString());
        }
        user.setUserFields(fields);
        try {
            SecurityUser authUser = getCurrentUser();
            if (authUser.getAuthority() == Authority.CUSTOMER_USER && !authUser.getId().equals(user.getId())) {
                throw new IOTException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                        IOTErrorCode.PERMISSION_DENIED);
            }
            if (authUser.getAuthority() == Authority.TENANT_ADMIN) {
                user.setTenantId(getCurrentUser().getTenantId());
                if(user.getAuthority() == Authority.CUSTOMER_USER) {
                    user.setCustomerId(userString.get("customer_id").getAsInt());
                }
            }
            userService.updateUser(user);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "根据UserId删除User")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'deleteUser')")
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

    @ApiOperation(value = "获取某个租户下所有租户管理员")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getTenantAdmins')")
    @RequestMapping(value = "/tenant/users", params = { "tenantId","limit","page"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTenantAdmins(
            @RequestParam Integer tenantId,
            @RequestParam int limit,
            @RequestParam int page) throws IOTException {
        checkParameter("tenantId", tenantId);
        try {
            return checkNotNull(userService.findTenantAdmins(page,limit,tenantId).toString());
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    @ApiOperation(value = "获取某个租户下所有租户管理员的总页数")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getTenantAdmins')")
    @RequestMapping(value = "/tenant/usersPages", params = { "tenantId","limit"}, method = RequestMethod.GET)
    @ResponseBody
    public Integer getTenantAdminsPages(
            @RequestParam Integer tenantId,
            @RequestParam int limit) throws IOTException {
        checkParameter("tenantId", tenantId);
        try {
//            return checkNotNull(userService.findTenantAdmins(0,limit,tenantId).getTotalPages());
            return userService.findTenantAdminsPageNum(limit,tenantId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "获取某个Customer下所有用户")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getCustomerUsers')")
    @RequestMapping(value = "/customer/users", params = { "customerId","limit","page" }, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getCustomerUsers(
            @RequestParam Integer customerId,
            @RequestParam int limit,
            @RequestParam int page) throws IOTException {
        checkParameter("customerId", customerId);
        try {
            checkCustomerId(customerId);
            Integer tenantId = getCurrentUser().getTenantId();
            return checkNotNull(userService.findCustomerUsers(page,limit,tenantId,customerId).toString());
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "获取某个Customer下所有用户的页数")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getCustomerUsers')")
    @RequestMapping(value = "/customer/usersPages", params = { "customerId","limit" }, method = RequestMethod.GET)
    @ResponseBody
    public Integer getCustomerUsersPages(
            @RequestParam Integer customerId,
            @RequestParam int limit) throws IOTException {
        checkParameter("customerId", customerId);
        try {
            checkCustomerId(customerId);
            Integer tenantId = getCurrentUser().getTenantId();
//            return checkNotNull(userService.findCustomerUsers(0,limit,customerId).getTotalPages());
            return userService.findCustomerUsersPageNum(limit,tenantId,customerId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "为某个租户下的所有用户新增一个字段")
    @RequestMapping(value = "/field", method = RequestMethod.POST)
    @ResponseBody
    public Integer addANewField (
            @RequestParam String name,
            @RequestParam(required = false) String desc) throws IOTException {
        String description = desc==null?"":desc;
        SecurityUser authUser = getCurrentUser();
        Integer tenant_id = authUser.getTenantId();
        return userService.addNewField(tenant_id,name,description);
    }


    @ApiOperation(value = "查询租户下的所有字段")
    @RequestMapping(value = "/fields", method = RequestMethod.GET)
    @ResponseBody
    public String findFields (
            @RequestParam Integer tenant_id
    ) throws IOTException {
        return userService.findFields(tenant_id).toString();
    }

    @ApiOperation(value = "删除一个字段")
    @RequestMapping(value = "/field", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteAField (
            @RequestParam Integer id
            ) throws IOTException {
        userService.deleteAField(id);
    }

    @ApiOperation(value = "更新一个字段的描述信息")
    @RequestMapping(value = "/field", method = RequestMethod.PUT)
    @ResponseBody
    public void updateAFieldInfo(
            @RequestParam Integer id,
            @RequestParam String desc) {
        userService.updateAFieldDesc(id,desc);
    }

    @ApiOperation(value = "更新一个用户某一个字段的值")
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/fieldValue", method = RequestMethod.PUT)
    @ResponseBody
    public void updateAFieldValue(
            @RequestParam String name,
            @RequestParam String value) throws IOTException{
        SecurityUser authUser = getCurrentUser();
        Integer tenant_id = authUser.getTenantId();
        Integer user_id = authUser.getId();
        userService.updateAFieldValue(tenant_id,user_id,name,value);
    }

    private User Json2User(JsonObject userString){
        User user = new User();
        user.setEmail(userString.get("email").getAsString());
        user.setName(userString.get("name").getAsString());
        user.setAdditional_info(userString.get("additional_info").getAsString());
        return user;
    }

    private List<UserField> getFields(JsonObject userString){
        List<UserField> list = new ArrayList<>();
        if(userString.has("id")) {userString.remove("id");}
        if(userString.has("tenant_id")) {userString.remove("tenant_id");}
        if(userString.has("customer_id")) {userString.remove("customer_id");}
        if(userString.has("authority")) {userString.remove("authority");}
        if(userString.has("name")) {userString.remove("name");}
        if(userString.has("additional_info")) {userString.remove("additional_info");}
        if(userString.has("email")) {userString.remove("email");}
        if(userString.has("phone")) {userString.remove("phone");}
        if(userString.has("we_chat")) {userString.remove("we_chat");}
        if(userString.has("password")) {userString.remove("password");}
        Set<Map.Entry<String, JsonElement>> set = userString.entrySet();
        for(Map.Entry entry:set){
            list.add(new UserField(entry.getKey().toString(),((JsonElement)entry.getValue()).getAsString()));
        }
        return list;
    }

}
