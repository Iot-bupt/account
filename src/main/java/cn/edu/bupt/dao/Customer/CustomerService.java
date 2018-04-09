package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.entity.Customer;
import org.springframework.data.domain.Page;

/**
 * Created by CZX on 2018/4/9.
 */
public interface CustomerService {

    Page<Customer> findCustomersByTenantId(Integer page, Integer size,Integer tenant_id);

    Customer findCustomerById(Integer customerId);

    Customer saveCustomer(Customer customer);

    void deleteCustomer(Integer customerId);

    void deleteCustomersByTenantId(Integer tenantId);
}
