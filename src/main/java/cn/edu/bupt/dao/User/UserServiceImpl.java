package cn.edu.bupt.dao.User;

import cn.edu.bupt.dao.Customer.CustomerRepository;
import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.Tenant.TenantRepository;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.entity.User;
import org.apache.commons.lang3.StringUtils;
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
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;

    @Override
    public User findUserById(Integer userId){
        return userRepository.findById(userId).get();
    }

    @Override
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User saveUser(User user){
        userValidator.validate(user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer userId){
        userCredentialsService.deleteUserCredentialsByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public Page<User> findTenantAdmins(Integer page, Integer size, Integer tenant_id){
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<User> userPage = userRepository.findAll(new Specification<User>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate1 = criteriaBuilder.equal(root.get("tenant").as(Tenant.class),tenant_id);
                Predicate predicate2 = criteriaBuilder.equal(root.get("authority").as(String.class),"SYS_ADMIN");
                criteriaQuery.where(criteriaBuilder.and(predicate1,predicate2));
                return criteriaQuery.getRestriction();
            }
        },pageable);
        return userPage;
    }

    @Override
    public Page<User> findCustomerUsers(Integer page, Integer size,Integer customer_id){
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<User> userPage = userRepository.findAll(new Specification<User>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("customer").as(Customer.class),customer_id);
                criteriaQuery.where(criteriaBuilder.and(predicate));
                return criteriaQuery.getRestriction();
            }
        },pageable);
        return userPage;
    }

    @Override
    public void deleteCustomerUsers(Integer customerId){
        userRepository.deleteAllByCustomer(customerRepository.findById(customerId).get());
    }

    @Override
    public void deleteTenantAdmins(Integer tenantId){
        userRepository.deleteAllByTenantAndAuthority(tenantRepository.findById(tenantId).get(),"TENANT_ADMIN");
    }

    private DataValidator<User> userValidator =
            new DataValidator<User>() {
                @Override
                protected void validateDataImpl(User user) {
                    if (StringUtils.isEmpty(user.getEmail())) {
                        throw new DataValidationException("User email should be specified!");
                    }
                    validateEmail(user.getEmail());
                    String authority = user.getAuthority();
                    if (authority == null) {
                        throw new DataValidationException("User authority isn't defined!");
                    }

                    //用户的部门验证
                    Integer tenantId = user.getTenant().getId();
                    Integer customerId = user.getCustomer().getId();
                    switch (authority) {
                        case "SYS_ADMIN":
                            if (!(tenantId==null) || !(customerId==null)){
                                throw new DataValidationException("System administrator can't be assigned neither to tenant nor to customer!");
                            }
                            break;
                        case "TENANT_ADMIN":
                            if (tenantId==null) {
                                throw new DataValidationException("Tenant administrator should be assigned to tenant!");
                            } else if (!(customerId==null)) {
                                throw new DataValidationException("Tenant administrator can't be assigned to customer!");
                            }
                            break;
                        case "CUSTOMER_USER":
                            if (tenantId==null || customerId==null) {
                                throw new DataValidationException("Customer user should be assigned to customer!");
                            }
                            break;
                        default:
                            break;
                    }

                    User existentUserWithEmail = findUserByEmail(user.getEmail());
                    if (existentUserWithEmail != null && !isSameData(existentUserWithEmail, user)) {
                        throw new DataValidationException("User with email '" + user.getEmail() + "' "
                                + " already present in database!");
                    }
                    if (!(customerId==null)) {
                        Customer customer = customerRepository.findById(user.getCustomer().getId()).get();
                        if (!(customer.getTenant().getId()==tenantId)) {
                            throw new DataValidationException("User can't be assigned to customer from different tenant!");
                        }
                    }
                }
            };
}
