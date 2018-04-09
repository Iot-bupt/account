package cn.edu.bupt.dao.User;

import cn.edu.bupt.dao.Customer.CustomerRepository;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.entity.User;
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
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

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
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer userId){
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
}
