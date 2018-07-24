package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.entity.Customer;
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

    void deleteCustomer(Integer customerId);

    void deleteCustomersByTenantId(Integer tenantId);

    String findCustomerName(Integer tenantId,Integer customerId);

    Integer findCustomersByTenantIdPageNum(Integer size,Integer tenant_id);
}
