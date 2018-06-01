package cn.edu.bupt.Security;

import cn.edu.bupt.Security.model.SecurityUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by CZX on 2018/5/25.
 */
public class MyPermissionEvaluator implements PermissionEvaluator{

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object requiredPermission) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Collection<String> permissions = securityUser.getPermissions();
        for (String permission : permissions) {
            if (permission.equals(requiredPermission)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

//    public boolean hasPermission(Authentication authentication, Object requiredPermission) {
//        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
//        Collection<String> permissions = securityUser.getPermissions();
//        for (String permission : permissions) {
//            if (permission.equals(requiredPermission)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
