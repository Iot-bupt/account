package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.Tenant.TenantRepository;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<Customer> findCustomersByTenantId(Integer page, Integer size,Integer tenant_id){
        log.trace("Executing findCustomersByTenantId, tenantId [{}], size [{}], page[{}]", tenant_id, size, page);
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
        log.trace("Executing findCustomerById [{}]", customerId);
        return customerRepository.findById(customerId).get();
    }

    @Override
    public Customer saveCustomer(Customer customer){
        log.trace("Executing saveCustomer [{}]", customer);
        customerValidator.validate(customer);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer customerId){
        log.trace("Executing deleteCustomer [{}]", customerId);
        userService.deleteCustomerUsers(customerId);
        //TODO:unassignCustomerDevices
        customerRepository.deleteById(customerId);
    }

    @Override
    public void deleteCustomersByTenantId(Integer tenantId){
        log.trace("Executing deleteCustomersByTenantId, tenantId [{}]", tenantId);
        Tenant tenant = tenantRepository.findById(tenantId).get();
        List<Customer> customerList = customerRepository.findAllByTenant(tenant);
        for(Customer customer : customerList){
            deleteCustomer(customer.getId());
        }
    }

    private DataValidator<Customer> customerValidator =
            new DataValidator<Customer>() {

                @Override
                protected void validateCreate(Customer customer) {
                    customerRepository.findCustomerByTenantAndTitle(customer.getTenant(), customer.getTitle()).ifPresent(
                            c -> {
                                throw new DataValidationException("Customer with such title already exists!");
                            }
                    );
                }

                @Override
                protected void validateUpdate(Customer customer) {
                    customerRepository.findCustomerByTenantAndTitle(customer.getTenant(), customer.getTitle()).ifPresent(
                            c -> {
                                if (!c.getId().equals(customer.getId())) {
                                    throw new DataValidationException("Customer with such title already exists!");
                                }
                            }
                    );
                }

                @Override
                protected void validateDataImpl(Customer customer) {
                    if (StringUtils.isEmpty(customer.getTitle())) {
                        throw new DataValidationException("Customer title should be specified!");
                    }
                    if (!StringUtils.isEmpty(customer.getEmail())) {
                        validateEmail(customer.getEmail());
                    }
                    if (customer.getTenant() == null) {
                        throw new DataValidationException("Customer should be assigned to tenant!");
                    }
                }
            };

}
