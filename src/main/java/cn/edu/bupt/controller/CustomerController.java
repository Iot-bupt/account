package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.Tenant.TenantService;
import cn.edu.bupt.entity.Customer;
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
 * Created by zyf on 2018/4/11.
 */
@RestController
@RequestMapping("/api/v1/account")
public class CustomerController extends BaseController{

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TenantService tenantService;

    public static final String CUSTOMER_ID = "customerId";
    public static final String CUSTOMER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING = "Customer ID should be specified when updating!";

    @ApiOperation(value = "根据CustomerId获取Customer")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer",params = {"customerId"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getCustomerById(@RequestParam Integer customerId) throws IOTException {
        checkParameter(CUSTOMER_ID, customerId);
        try {
            return checkCustomerId(customerId).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "创建Customer")
//    eg. {"email":"1@qq.com","title":"testCustomer","additional_info":"","phone":"1111","address":"address"}
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String createCustomer(@RequestBody String customerInfo) throws IOTException {
        JsonObject customerString = new JsonParser().parse(customerInfo).getAsJsonObject();
        Customer customer = Json2Customer(customerString);
        try {
            customer.setTenantId(getCurrentUser().getTenantId());
            customerService.saveCustomer(customer);
            return  customer.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "更新Customer信息")
//    eg.{"id":"7","email":"2@qq.com","title":"testCustomer","additional_info":"","phone":"1111","address":"address"}
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customer", method = RequestMethod.PUT, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateCustomer(@RequestBody String customerInfo) throws IOTException {
        JsonObject customerString = new JsonParser().parse(customerInfo).getAsJsonObject();
        if(customerString.get("id").getAsString().equals("")) {
            throw new IOTException(CUSTOMER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING,
                    IOTErrorCode.BAD_REQUEST_PARAMS);
        }
        Customer customer = new Customer();
        customer.setId(customerString.get("id").getAsInt());
        customer.setEmail(customerString.get("email").getAsString());
        customer.setTitle(customerString.get("title").getAsString());
        customer.setAdditional_info(customerString.get("additional_info").getAsString());
        customer.setPhone(customerString.get("phone").getAsString());
        customer.setAddress(customerString.get("address").getAsString());
        try {
            customer.setTenantId(getCurrentUser().getTenantId());
            customerService.updateCustomer(customer);
            return  customer.toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "根据CustomerId删除Customer")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @Transactional
    @RequestMapping(value = "/customer",params = {"customerId","tenantId"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCustomer(@RequestParam Integer customerId,@RequestParam Integer tenantId) throws IOTException {
        checkParameter(CUSTOMER_ID, customerId);
        try {
            checkCustomerId(customerId);
            customerService.deleteCustomer(tenantId,customerId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "获取某个Tenant下所有Customer")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customers", params = {  "limit","page"  }, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getCustomers(@RequestParam int limit,
                               @RequestParam int page) throws IOTException {
        try {
            Integer tenantId = getCurrentUser().getTenantId();
            return checkNotNull(customerService.findCustomersByTenantId(page,limit,tenantId)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "获取某个Tenant下所有Customer的页数")
//    @PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customersPages", params = {  "limit"  }, method = RequestMethod.GET)
    @ResponseBody
    public Integer getCustomersTotalPages(@RequestParam int limit) throws IOTException {
        try {
            Integer tenantId = getCurrentUser().getTenantId();
//            return checkNotNull(customerService.findCustomersByTenantId(0,limit,tenantId)).getTotalPages();
            return customerService.findCustomersByTenantIdPageNum(limit,tenantId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @ApiOperation(value = "根据CustomerId获取Customer的名字")
    @PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customerName",params = {"customerId"}, method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String findCustomerName(@RequestParam Integer customerId) throws IOTException {
        checkParameter(CUSTOMER_ID, customerId);
        try {
            return customerService.findCustomerName(getCurrentUser().getTenantId(),customerId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private Customer Json2Customer(JsonObject customerString){
        Customer customer = new Customer();
        customer.setEmail(customerString.get("email").getAsString());
        customer.setTitle(customerString.get("title").getAsString());
        customer.setAdditional_info(customerString.get("additional_info").getAsString());
        customer.setPhone(customerString.get("phone").getAsString());
//        customer.setTenant(tenantService.findTenantById(customerString.get("tenant_id").getAsInt()));
        customer.setAddress(customerString.get("address").getAsString());
        return customer;
    }
}
