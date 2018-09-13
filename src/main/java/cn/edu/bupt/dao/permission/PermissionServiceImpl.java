package cn.edu.bupt.dao.permission;

import cn.edu.bupt.entity.Permission;
import cn.edu.bupt.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by CZX on 2018/9/3.
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService{


    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> findAllPermissionsByRoleId(int role_id) {
        return permissionRepository.findAllByRoleId(role_id);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(int role_id) {
        return permissionRepository.findAllIDsByRoleId(role_id);
    }

    @Override
    public Set<String> findPermissionNamesByRoleId(int role_id) {
        return permissionRepository.findAllNamesByRoleId(role_id);
//        List<Integer> ids = findPermissionIdByRoleId(role_id);
//        List<String> permissions = new ArrayList<>();
//        for(Integer id : ids){
//            permissions.add(permissionRepository.findById(id).getName());
//        }
//        return permissions;
    }

    @Override
    public void saveRolePermissionRelation(int role_id, int permission_id) {
        permissionRepository.saveRolePermissionRelation(role_id,permission_id);
    }

    @Override
    public void deleteARelation(int role_id, int permission_id) {
        permissionRepository.deleteARelation(role_id,permission_id);
    }

    @Override
    public Set<Permission> findAllByUserId(int user_id) {
        return permissionRepository.findAllByUserId(user_id);
    }
}
