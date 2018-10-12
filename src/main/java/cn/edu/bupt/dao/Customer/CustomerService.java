package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.exception.IOTException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CZX on 2018/4/9.
 */
public interface CustomerService {

    List<Customer> findCustomersByTenantId(Integer page, Integer size, Integer tenant_id);

    Customer findCustomerById(Integer customerId);

    void saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(Integer tenantId,Integer customerId) throws IOTException;

    void deleteCustomersByTenantId(Integer tenantId) throws IOTException;

    String findCustomerName(Integer tenantId,Integer customerId);

    Integer findCustomersByTenantIdPageNum(Integer size,Integer tenant_id);
}
