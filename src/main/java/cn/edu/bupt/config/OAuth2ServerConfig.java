package cn.edu.bupt.config;


import cn.edu.bupt.Security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


@Configuration
public class OAuth2ServerConfig {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Autowired
        private CustomLogoutHandler customLogoutHandler;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
//            resources.stateless(true);
            CustomRemoteTokenServices resourceServerTokenServices = new CustomRemoteTokenServices();
            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            accessTokenConverter.setUserTokenConverter(new UserTokenConverter());
            resourceServerTokenServices.setAccessTokenConverter(accessTokenConverter);
            resources.tokenServices(resourceServerTokenServices);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    // Since we want the protected resources to be accessible in the UI as well we need
                    // session creation to be allowed (it's disabled by default in 2.0.6)
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requestMatchers().anyRequest()
                    .and()
                    .anonymous()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/v1/account/tenant").hasAuthority("TENANT_ADMIN")//access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")
                    .and().logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .addLogoutHandler(customLogoutHandler);

        }
    }


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;

        @Value("${security.jwt.tokenSigningKey}")
        private String signingKey;




        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            //配置两个客户端,一个用于password认证一个用于client认证
            clients.inMemory().withClient("client_1")
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret(passwordEncoder.encode("123456"))
                    .and().withClient("client_2")
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret(passwordEncoder.encode("123456"));
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .pathMapping("/oauth/token","/api/v1/auth/login")
                    .pathMapping("/oauth/check_token","/api/v1/auth/check_token")
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .tokenServices(authorizationServerTokenServices())
                    .accessTokenConverter(accessTokenConverter());
        }

        @Bean
        public InMemoryTokenStore tokenStore(){
            return new InMemoryTokenStore();
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            //允许表单认证
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
//            oauthServer.allowFormAuthenticationForClients();
        }

        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter converter = new CustomTokenEnhancer();
            converter.setSigningKey(signingKey);
            return converter;
        }

        @Bean
        public AuthorizationServerTokenServices authorizationServerTokenServices() {
            CustomAuthorizationTokenServices customTokenServices = new CustomAuthorizationTokenServices();
            customTokenServices.setTokenStore(tokenStore());
            customTokenServices.setSupportRefreshToken(true);
            customTokenServices.setReuseRefreshToken(true);
            customTokenServices.setTokenEnhancer(accessTokenConverter());
            return customTokenServices;
        }

    }

}
