package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by CZX on 2018/4/8.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer>,JpaSpecificationExecutor<Customer> {
    List<Customer> findCustomersByTenant(Tenant tenant);
}
