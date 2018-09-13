package cn.edu.bupt.dao.permission;

import cn.edu.bupt.entity.Permission;
import cn.edu.bupt.entity.User;

import java.util.List;
import java.util.Set;

/**
 * Created by CZX on 2018/9/3.
 */
public interface PermissionService {

    List<Permission> findAllPermissions();

    List<Permission> findAllPermissionsByRoleId(int role_id);

    List<Integer> findPermissionIdsByRoleId(int role_id);

    Set<String> findPermissionNamesByRoleId(int role_id);

    void saveRolePermissionRelation(int role_id,int permission_id);

    void deleteARelation(int role_id,int permission_id);

    Set<Permission> findAllByUserId(int user_id);
}
