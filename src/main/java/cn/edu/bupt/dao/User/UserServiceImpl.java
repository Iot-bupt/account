package cn.edu.bupt.dao.User;

import cn.edu.bupt.dao.Customer.CustomerRepository;
import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.Role.RoleService;
import cn.edu.bupt.dao.Tenant.TenantRepository;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.entity.Authority;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserCredentialsService userCredentialsService;

    @Autowired
    private RoleService roleService;

    @Override
    public User findUserById(Integer userId){
        log.trace("Executing findUserById [{}]", userId);
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findUserByTenantId(Integer TenantId) {
        log.trace("Executing findUserByTenantId [{}]", TenantId);
        return userRepository.findByTenantId(TenantId);
    }

    @Override
    public User findUserByEmail(String email){
        log.trace("Executing findUserByEmail [{}]", email);
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Integer saveUser(User user){
        log.trace("Executing saveUser [{}]", user);
        userValidator.validate(user);
        return userRepository.save(user);
    }

    @Override
    public void updateUser(User user){
        log.trace("Executing updateUser [{}]", user);
        userValidator.validate(user);
        userRepository.update(user);
    }

    @Override
    public void deleteUser(Integer userId){
        log.trace("Executing deleteUser [{}]", userId);
        roleService.deleteRoleUserRelationByUserId(userId);
        userCredentialsService.deleteUserCredentialsByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findTenantAdmins(Integer page, Integer size, Integer tenant_id){
        log.trace("Executing findTenantAdmins, tenantId [{}], size [{}], page [{}]", tenant_id, size, page);
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        List<User> userPage = userRepository.findTenantAdmins(page*size,size,tenant_id);
        return userPage;
    }

    @Override
    public Integer findTenantAdminsPageNum(Integer size, Integer tenant_id){
        Integer num = (userRepository.findTenantAdminsCount(tenant_id)+size-1)/size;
        return num;
    }

    @Override
    public List<User> findCustomerUsers(Integer page, Integer size,Integer tenant_id,Integer customer_id){
        log.trace("Executing findCustomerUsers, customerId [{}], size [{}], page [{}]", customer_id, size, page);
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        List<User> userPage = userRepository.findCustomerUsers(page*size,size,tenant_id,customer_id);
        return userPage;
    }

    @Override
    public Integer findCustomerUsersPageNum(Integer size,Integer tenant_id,Integer customer_id){
        log.trace("Executing findCustomerUsersPageNum, customerId [{}], size [{}]", customer_id, size);
        Integer num = (userRepository.findCustomerUsersCount(tenant_id,customer_id)+size-1)/size;
        return num;
    }

    @Override
    public void deleteCustomerUsers(Integer tenantId, Integer customerId){
        log.trace("Executing deleteCustomerUsers, customerId [{}]", customerId);
        List<User> users = userRepository.findCustomerUsers(0,100,tenantId,customerId);
        while(!users.isEmpty()) {
            for (User entity : users) {
                deleteUser(entity.getId());
            }
            users = userRepository.findCustomerUsers(0,100,tenantId,customerId);
        }
//        userRepository.deleteAllByCustomerId(customerId);
    }

    @Override
    public void deleteTenantAdmins(Integer tenantId){
        log.trace("Executing deleteTenantAdmins, tenantId [{}]", tenantId);
        List<User> users = userRepository.findTenantAdmins(0,100,tenantId);
        while(!users.isEmpty()) {
            for (User entity : users) {
                deleteUser(entity.getId());
            }
            users = userRepository.findTenantAdmins(0,100,tenantId);
        }
    }

    private DataValidator<User> userValidator =
            new DataValidator<User>() {
                @Override
                protected void validateDataImpl(User user) {
                    if (StringUtils.isEmpty(user.getEmail())) {
                        throw new DataValidationException("请指定用户邮箱！");
                    }
                    validateEmail(user.getEmail());
                    if (!StringUtils.isEmpty(user.getPhone())) {
                        validatePhone(user.getPhone());
                    }
                    Authority authority = user.getAuthority();
                    if (authority == null) {
                        throw new DataValidationException("User authority isn't defined!");
                    }

                    //用户的部门验证
                    Integer tenantId = user.getTenantId();
                    Integer customerId = user.getCustomerId();
                    switch (authority) {
                        case SYS_ADMIN:
                            if (!(tenantId==1) || !(customerId==1)){
                                throw new DataValidationException("System administrator can't be assigned neither to tenant nor to customer!");
                            }
                            break;
                        case TENANT_ADMIN:
                            if (tenantId==1) {
                                throw new DataValidationException("Tenant administrator should be assigned to tenant!");
                            } else if (!(customerId==1)) {
                                throw new DataValidationException("Tenant administrator can't be assigned to customer!");
                            }
                            break;
                        case CUSTOMER_USER:
                            if (tenantId==1 || customerId==1) {
                                throw new DataValidationException("Customer user should be assigned to customer!");
                            }
                            break;
                        default:
                            break;
                    }
                    if (!(customerId==1)) {
                        Customer customer = customerRepository.findById(user.getCustomerId());
                        if (!(customer.getTenantId()==tenantId)) {
                            throw new DataValidationException("User can't be assigned to customer from different tenant!");
                        }
                    }
                }
            };
}
