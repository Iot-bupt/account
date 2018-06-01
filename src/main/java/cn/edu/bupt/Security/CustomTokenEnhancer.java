package cn.edu.bupt.Security;

import cn.edu.bupt.Security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CZX on 2018/4/26.
 */
public class CustomTokenEnhancer extends JwtAccessTokenConverter implements Serializable{

    private static final String USER_NAME = "user_name";
    private static final String USER_ID = "user_id";
    private static final String TENANT_ID = "tenant_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String AUTHORITY = "authority";
    private static final String ISSUER = "issuer";
    private static final String PERMISSIONS = "permissions";

    @Value("${security.jwt.tokenIssuer}")
    private String tokenIssuer;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        if(!accessToken.getScope().contains("all")){
            SecurityUser securityUser = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
    //        authentication.getUserAuthentication().getPrincipal();
            Map<String, Object> info = new HashMap<>();
            info.put(USER_ID, securityUser.getId());
            info.put(TENANT_ID, securityUser.getTenantId());
            info.put(CUSTOMER_ID, securityUser.getCustomerId());
            info.put(AUTHORITY, securityUser.getAuthority());
            info.put(USER_NAME, securityUser.getUserPrincipal());
            info.put(ISSUER, tokenIssuer);
            info.put(PERMISSIONS, securityUser.getPermissions());
            customAccessToken.setAdditionalInformation(info);
        }
        OAuth2AccessToken enhancedToken = super.enhance(customAccessToken, authentication);
        return enhancedToken;
    }
}
