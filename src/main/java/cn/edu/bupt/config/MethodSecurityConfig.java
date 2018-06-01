package cn.edu.bupt.config;

import cn.edu.bupt.Security.MyPermissionEvaluator;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * Created by CZX on 2018/5/10.
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration{

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        PermissionEvaluator permissionEvaluator = new MyPermissionEvaluator();
        OAuth2MethodSecurityExpressionHandler oAuth2MethodSecurityExpressionHandler = new OAuth2MethodSecurityExpressionHandler();
        oAuth2MethodSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        return oAuth2MethodSecurityExpressionHandler;
    }

}
