package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Tenant.TenantService;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zyf on 2018/4/17.
 */
@RestController
@RequestMapping("/api")

public class TenantController extends BaseController{

    @Autowired
    private TenantService tenantService;

    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING = "Tenant ID should be specified when updating!";

  //  @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.GET)
    @ResponseBody
    public String getTenantById(@PathVariable(TENANT_ID) Integer  tenantId) throws IOTException {
        checkParameter("tenantId", tenantId);
        try {
            checkTenantId(tenantId);
            return checkNotNull(tenantService.findTenantById(tenantId)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }
//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
//    eg. {"email":"1@qq.com","title":"testTenant","additional_info":"","phone":"1111","address":"address"}
    @RequestMapping(value = "/tenant", method = RequestMethod.POST)
    @ResponseBody
    public String createTenant(@RequestBody String tenantInfo) throws IOTException {
        JsonObject tenantString = new JsonParser().parse(tenantInfo).getAsJsonObject();
        Tenant tenant = Json2Tenant(tenantString);
        try {
            return checkNotNull(tenantService.saveTenant(tenant)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
//    eg.{"id":"7","email":"2@qq.com","title":"testTenant","additional_info":"","phone":"1111","address":"address"}
    @RequestMapping(value = "/tenant", method = RequestMethod.PUT)
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
            return checkNotNull(tenantService.saveTenant(tenant)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @Transactional
    @RequestMapping(value = "/tenant/{tenantId}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTenant(@PathVariable(TENANT_ID) Integer tenantId) throws IOTException {
        checkParameter(TENANT_ID, tenantId);
        try {
            tenantService.deleteTenant(tenantId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @RequestMapping(value = "/tenants", params = {  "limit","page"  }, method = RequestMethod.GET)
    @ResponseBody
    public String getTenants(@RequestParam int limit,
                             @RequestParam int page) throws IOTException {
        try {
           // Integer tenantId = getCurrentUser().getTenant().getId();
            return checkNotNull(tenantService.findTenants(page,limit)).getContent().toString();
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

