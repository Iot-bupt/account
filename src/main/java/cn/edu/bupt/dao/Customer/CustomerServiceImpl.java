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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

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
    public List<Customer> findCustomersByTenantId(Integer page, Integer size,Integer tenant_id){
        log.trace("Executing findCustomersByTenantId, tenantId [{}], size [{}], page[{}]", tenant_id, size, page);
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
//        Page<Customer> customerPage = customerRepository.findAll(new Specification<Customer>() {
//            @Nullable
//            @Override
//            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                Predicate predicate = criteriaBuilder.equal(root.get("tenant").as(Tenant.class),tenant_id);
//                criteriaQuery.where(criteriaBuilder.and(predicate));
//                return criteriaQuery.getRestriction();
//            }
//        },pageable);
        Integer index = page * size;
        List<Customer> customers = customerRepository.findAllByTenantIdAndPage(index,size,tenant_id);
        return customers;
    }

    @Override
    public Integer findCustomersByTenantIdPageNum(Integer size,Integer tenant_id){
        log.trace("Executing findCustomersByTenantIdPageNum, tenantId [{}], size [{}]", tenant_id, size);
        Integer num = (customerRepository.findAllByTenantIdCount(tenant_id)+size-1)/size;
        return num;
    }

    @Override
    public Customer findCustomerById(Integer customerId){
        log.trace("Executing findCustomerById [{}]", customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    public void saveCustomer(Customer customer){
        log.trace("Executing saveCustomer [{}]", customer);
        customerValidator.validate(customer);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer){
        log.trace("Executing updateCustomer [{}]", customer);
        customerValidator.validate(customer);
        customerRepository.update(customer);
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
//        Tenant tenant = tenantRepository.findById(tenantId);
        List<Customer> customerList = customerRepository.findAllByTenantId(tenantId);
        for(Customer customer : customerList){
            deleteCustomer(customer.getId());
        }
    }

    @Override
    public String findCustomerName(Integer tenantId,Integer customerId){
        log.trace("Executing findCustomerName tenantId [{}],customerId [{}]", tenantId,customerId);
        return customerRepository.findNameByTenantAndCustomer(tenantId,customerId);
    }

    private DataValidator<Customer> customerValidator =
            new DataValidator<Customer>() {

                @Override
                protected void validateCreate(Customer customer) {
                    customerRepository.findCustomerByTenantIdAndTitle(customer.getTenantId(), customer.getTitle()).ifPresent(
                            c -> {
                                throw new DataValidationException("Customer with such title already exists!");
                            }
                    );
                }

                @Override
                protected void validateUpdate(Customer customer) {
                    customerRepository.findCustomerByTenantIdAndTitle(customer.getTenantId(), customer.getTitle()).ifPresent(
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
                    if (customer.getTenantId() == null) {
                        throw new DataValidationException("Customer should be assigned to tenant!");
                    }
                }
            };

}
