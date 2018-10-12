package cn.edu.bupt.dao.Role;

import cn.edu.bupt.entity.Role;

import java.util.List;

/**
 * Created by CZX on 2018/9/4.
 */
public interface RoleService {

    List<Role> findAllRoles();

    List<Role> findAllRolesByUserId(int user_id);

    List<Role> findExtraRolesByUserId(int user_id);

    List<Role> findNotOwnedExtraRolesByUserId(int user_id);

    Integer saveRole(Role role);

    void deleteRoleById(Integer id);

    void updateRole(Role role);

    Role findRoleById(Integer id);

    void saveRoleUserRelation(Integer role_id,Integer user_id);

    void deleteRoleUserRelation(Integer role_id,Integer user_id);

    void deleteRoleUserRelationByUserId(Integer user_id);

}
