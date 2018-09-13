package cn.edu.bupt.Security;

import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.Role.RoleService;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.dao.permission.PermissionService;
import cn.edu.bupt.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by CZX on 2018/4/12.
 */
@Component
@Slf4j
public class CustomerAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final UserCredentialsService userCredentialsService;
    private final PermissionService permissionService;
    private final RoleService roleService;


    @Autowired
    public CustomerAuthenticationProvider(final UserService userService, final UserCredentialsService userCredentialsService,final PermissionService permissionService,RoleService roleService) {
        this.userService = userService;
        this.userCredentialsService = userCredentialsService;
        this.permissionService = permissionService;
        this.encoder = new BCryptPasswordEncoder();
        this.roleService = roleService;
    }

    //TODO:循环引用问题。在cn.edu.bupt.config.SecurityConfiguration中要注入CustomerAuthenticationProvider，而CustomerAuthenticationProvider要注入BCryptPasswordEncoder,BCryptPasswordEncoder又在cn.edu.bupt.config.SecurityConfiguration中。
//    @Autowired
//    public CustomerAuthenticationProvider(final UserService userService, final UserCredentialsService userCredentialsService,final BCryptPasswordEncoder encoder) {
//        this.userService = userService;
//        this.userCredentialsService = userCredentialsService;
//        this.encoder = encoder;
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        log.trace("Executing authenticate");
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        return authenticateByUsernameAndPassword(username, password);
    }

    private Authentication authenticateByUsernameAndPassword(String username, String password) {
        User user = userService.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserCredentials userCredentials = userCredentialsService.findUserCredentialsByUserId(user.getId());
        if (userCredentials == null) {
            throw new UsernameNotFoundException("User credentials not found");
        }

        if (!encoder.matches(password, userCredentials.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        if (user.getAuthority() == null) throw new InsufficientAuthenticationException("User has no authority assigned");

        Set<Permission> permissions = permissionService.findAllByUserId(user.getId());
        Set<String> permissionNames = new HashSet<>();
        for(Permission permission:permissions){
            permissionNames.add(permission.getName());
        }
//        List<Role> extra_roles = roleService.findAllRolesByUserId(user.getId());
//        for(Role role:extra_roles){
//            Set<String> extra_permissions = permissionService.findPermissionNamesByRoleId(role.getId());
//            permissions.addAll(extra_permissions);
//        }
        SecurityUser securityUser = new SecurityUser(user.getId(),user.getName(),user.getCustomerId(),user.getTenantId(),user.getAuthority(),permissionNames);

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
