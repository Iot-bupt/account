package cn.edu.bupt.Security;

import cn.edu.bupt.Security.model.SecurityUser;
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

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
//        authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> info = new HashMap<>();
        info.put(USER_ID, securityUser.getId());
        info.put(TENANT_ID, securityUser.getTenant().getId());
        info.put(CUSTOMER_ID, securityUser.getCustomer().getId());
        info.put(AUTHORITY, securityUser.getAuthority());
        info.put(USER_NAME, securityUser.getName());


        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);

        OAuth2AccessToken enhancedToken = super.enhance(customAccessToken, authentication);
        return enhancedToken;
    }
}
