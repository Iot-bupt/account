package cn.edu.bupt.controller;

import cn.edu.bupt.Security.HttpUtil;
import cn.edu.bupt.dao.Tenant.TenantService;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zyf on 2018/4/17.
 */
@RestController
@RequestMapping("/api/v1/account")

public class TenantController extends BaseController{

    @Autowired
    private TenantService tenantService;

    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING = "Tenant ID should be specified when updating!";

    @ApiOperation(value = "根据TenantId获取Tenant")
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getTenantById')")
    @RequestMapping(value = "/tenant",params = {"tenantId"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTenantById(@RequestParam Integer  tenantId) throws IOTException {
        checkParameter("tenantId", tenantId);
        try {
            checkTenantId(tenantId);
            return checkNotNull(tenantService.findTenantById(tenantId)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    eg. {"email":"1@qq.com","title":"testTenant","additional_info":"","phone":"1111","address":"address"}
    @ApiOperation(value = "创建Tenant")
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'createTenant')")
    @RequestMapping(value = "/tenant", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String createTenant(@RequestBody String tenantInfo) throws IOTException {
        JsonObject tenantString = new JsonParser().parse(tenantInfo).getAsJsonObject();
        Tenant tenant = Json2Tenant(tenantString);
        try {
            tenantService.saveTenant(tenant);
            return tenant.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
//    eg.{"id":"7","email":"2@qq.com","title":"testTenant","additional_info":"","phone":"1111","address":"address"}
    @ApiOperation(value = "更新Tenant信息")
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'updateTenant')")
    @RequestMapping(value = "/tenant", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateTenant(@RequestBody String tenantInfo) throws IOTException {
        JsonObject tenantString = new JsonParser().parse(tenantInfo).getAsJsonObject();
        if(tenantString.get("id").getAsString().equals("")) {
            throw new IOTException(TENANT_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING,
                    IOTErrorCode.BAD_REQUEST_PARAMS);
        }
        Tenant tenant = Json2Tenant(tenantString);
        tenant.setId(tenantString.get("id").getAsInt());
        try {
            tenantService.updateTenant(tenant);
            return tenant.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }
    @ApiOperation(value = "根据TenantId删除Tenant")
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'deleteTenant')")
    @Transactional
    @RequestMapping(value = "/tenant",params = {"tenantId"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTenant(@RequestParam Integer tenantId) throws IOTException {
        checkParameter(TENANT_ID, tenantId);
        try {
            tenantService.deleteTenant(tenantId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "获取所有Tenant信息")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getTenants')")
    @RequestMapping(value = "/tenants", params = {  "limit","page"  }, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTenants(@RequestParam int limit,
                             @RequestParam int page) throws IOTException {
        try {
           // Integer tenantId = getCurrentUser().getTenant().getId();
            return checkNotNull(tenantService.findTenants(page,limit)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "获取所有Tenant信息")
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'getTenants')")
    @RequestMapping(value = "/tenantsPages", params = {  "limit","page"  }, method = RequestMethod.GET)
    @ResponseBody
    public Integer getTenantsPages(@RequestParam int limit) throws IOTException {
        try {
            // Integer tenantId = getCurrentUser().getTenant().getId();
            return tenantService.findTenantsPageNum(limit);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "修改某一个Tenant的suspended信息")
    @PreAuthorize("#oauth2.hasScope('all') OR hasPermission(null ,'updateSuspended')")
    @RequestMapping(value = "/updateSuspendedStatus", params = {  "suspended","id"  }, method = RequestMethod.PUT)
    @ResponseBody
    public void updateSuspended(@RequestParam Boolean suspended,Integer id) throws IOTException {
        try {
            // Integer tenantId = getCurrentUser().getTenant().getId();
            tenantService.updateSuspendedStatusById(suspended,id);
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    private Tenant Json2Tenant(JsonObject tenantString){
        Tenant tenant = new Tenant();
        tenant.setAddress(tenantString.get("address").getAsString());
        tenant.setAdditional_info(tenantString.get("additional_info").getAsString());
        tenant.setEmail(tenantString.get("email").getAsString());
        tenant.setPhone(tenantString.get("phone").getAsString());
        tenant.setTitle(tenantString.get("title").getAsString());
        return tenant;
    }
}

