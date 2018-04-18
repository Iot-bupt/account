package cn.edu.bupt.controller;

import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.entity.Customer;
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
 * Created by zyf on 2018/4/11.
 */
@RestController
@RequestMapping(path= "/api/v1/account")
public class CustomerController extends BaseController{

    @Autowired
    private CustomerService customerService;

    public static final String CUSTOMER_ID = "customerId";
    public static final String CUSTOMER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING = "Customer ID should be specified when updating!";

//    @PreAuthorize("hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/customer",params = {"customerId"}, method = RequestMethod.GET)
    @ResponseBody
    public String getCustomerById(@RequestParam Integer customerId) throws IOTException {
        checkParameter(CUSTOMER_ID, customerId);
        try {
            return checkCustomerId(customerId).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
//    eg. {"email":"1@qq.com","title":"testCustomer","additional_info":"","phone":"1111","address":"address"}
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    @ResponseBody
    public String createCustomer(@RequestBody String customerInfo) throws IOTException {
        JsonObject customerString = new JsonParser().parse(customerInfo).getAsJsonObject();
        Customer customer = Json2Customer(customerString);
        try {
            customer.setTenant(getCurrentUser().getTenant());
            return checkNotNull(customerService.saveCustomer(customer)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
//    eg.{"id":"7","email":"2@qq.com","title":"testCustomer","additional_info":"","phone":"1111","address":"address"}
    @RequestMapping(value = "/customer", method = RequestMethod.PUT)
    @ResponseBody
    public String updateCustomer(@RequestBody String customerInfo) throws IOTException {
        JsonObject customerString = new JsonParser().parse(customerInfo).getAsJsonObject();
        if(customerString.get("id").getAsString().equals("")) {
            throw new IOTException(CUSTOMER_ID_SHOULD_BE_SPECIFIED_WHEN_UPDATING,
                    IOTErrorCode.BAD_REQUEST_PARAMS);
        }
        Customer customer = customerService.findCustomerById(customerString.get("id").getAsInt());
        customer.setEmail(customerString.get("email").getAsString());
        customer.setTitle(customerString.get("title").getAsString());
        customer.setAdditional_info(customerString.get("additional_info").getAsString());
        customer.setPhone(customerString.get("phone").getAsString());
        customer.setAddress(customerString.get("address").getAsString());
        try {
            customer.setTenant(getCurrentUser().getTenant());
            return checkNotNull(customerService.saveCustomer(customer)).toString();
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @Transactional
    @RequestMapping(value = "/customer",params = {"customerId"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCustomer(@RequestParam Integer customerId) throws IOTException {
        checkParameter(CUSTOMER_ID, customerId);
        try {
            checkCustomerId(customerId);
            customerService.deleteCustomer(customerId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

//    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/customers", params = {  "limit","page"  }, method = RequestMethod.GET)
    @ResponseBody
    public String getCustomers(@RequestParam int limit,
                               @RequestParam int page) throws IOTException {
        try {
            Integer tenantId = getCurrentUser().getTenant().getId();
            return checkNotNull(customerService.findCustomersByTenantId(page,limit,tenantId)).getContent().toString();
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
