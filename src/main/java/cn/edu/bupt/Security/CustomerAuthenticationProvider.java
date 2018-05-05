package cn.edu.bupt.Security;

import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by CZX on 2018/4/12.
 */
@Component
public class CustomerAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final UserCredentialsService userCredentialsService;


    @Autowired
    public CustomerAuthenticationProvider(final UserService userService, final UserCredentialsService userCredentialsService) {
        this.userService = userService;
        this.userCredentialsService = userCredentialsService;
        this.encoder = new BCryptPasswordEncoder();
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
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        return authenticateByUsernameAndPassword(username, password);
    }

    private Authentication authenticateByUsernameAndPassword(String username, String password) {
        User user = userService.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserCredentials userCredentials = userCredentialsService.findUserCredentialsByUserId(user.getId()).get();
        if (userCredentials == null) {
            throw new UsernameNotFoundException("User credentials not found");
        }

        if (!encoder.matches(password, userCredentials.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        if (user.getAuthority() == null) throw new InsufficientAuthenticationException("User has no authority assigned");

        SecurityUser securityUser = new SecurityUser(user.getId(),user.getName(),user.getCustomer().getId(),user.getTenant().getId(),user.getAuthority());

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
