package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Created by CZX on 2018/4/8.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer>,JpaSpecificationExecutor<Customer> {

//    void deleteAllByTenant(Tenant tenant);

    Optional<Customer> findCustomerByTenantAndTitle(Tenant tenant, String title);

    List<Customer> findAllByTenant(Tenant tenant);
}