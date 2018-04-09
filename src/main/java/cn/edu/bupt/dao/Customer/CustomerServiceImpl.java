package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.dao.Tenant.TenantRepository;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Page<Customer> findCustomersByTenantId(Integer page, Integer size,Integer tenant_id){
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<Customer> customerPage = customerRepository.findAll(new Specification<Customer>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("tenant").as(Tenant.class),tenant_id);
                criteriaQuery.where(criteriaBuilder.and(predicate));
                return criteriaQuery.getRestriction();
            }
        },pageable);
        return customerPage;
    }

    @Override
    public Customer findCustomerById(Integer customerId){
        return customerRepository.findById(customerId).get();
    }

    @Override
    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public void deleteCustomersByTenantId(Integer tenantId){
        customerRepository.deleteAllByTenant(tenantRepository.findById(tenantId).get());
    }
}
