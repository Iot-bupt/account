package cn.edu.bupt.dao.User;

import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserField;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CZX on 2018/4/9.
 */
public interface UserService {

    User findUserById(Integer userId);

    List<User> findUserByTenantId(Integer TenantId);

    User findUserByEmail(String email);

    Integer saveUser(User user);

    void updateUser(User user);

    void deleteUser(Integer userId);

    List<User> findTenantAdmins(Integer page, Integer size, Integer tenant_id);

    void deleteTenantAdmins(Integer tenantId);

    List<User> findCustomerUsers(Integer page, Integer size,Integer tenant_id,Integer customer_id);

    void deleteCustomerUsers(Integer tenantId,Integer customerId);

    Integer findTenantAdminsPageNum(Integer size, Integer tenant_id);

    Integer findCustomerUsersPageNum(Integer size,Integer tenant_id,Integer customer_id);

    Integer addNewField(Integer tenant_id,String name,String desc);

    void updateAFieldDesc(Integer id,String desc);

    void updateAFieldValue(Integer tenant_id,Integer user_id,String name,String value);

    void deleteAField(Integer id);

    List<UserField> findFields(Integer tenant_id);

}
