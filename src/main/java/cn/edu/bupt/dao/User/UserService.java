package cn.edu.bupt.dao.User;

import cn.edu.bupt.entity.User;
import org.springframework.data.domain.Page;

/**
 * Created by CZX on 2018/4/9.
 */
public interface UserService {

    User findUserById(Integer userId);

    User findUserByEmail(String email);

    User saveUser(User user);

    void deleteUser(Integer userId);

    Page<User> findTenantAdmins(Integer page, Integer size,Integer tenant_id);

    void deleteTenantAdmins(Integer tenantId);

    Page<User> findCustomerUsers(Integer page, Integer size,Integer customer_id);

    void deleteCustomerUsers(Integer customerId);
}
