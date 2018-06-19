package cn.edu.bupt.config;


import cn.edu.bupt.Security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;


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
                    .logoutUrl("/api/v1/account/logout")
                    .clearAuthentication(true)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .addLogoutHandler(customLogoutHandler);

        }
    }


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private DataSource dataSource;
        @Autowired
        private RedisConnectionFactory connectionFactory;
        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;

        @Value("${security.jwt.tokenSigningKey}")
        private String signingKey;

        @Value("${oauth2.internal_client_id}")
        private String internal_client_id;

        @Value("${oauth2.internal_client_secret}")
        private String internal_client_secret;

        @Value("${oauth2.external_client_id}")
        private String external_client_id;

        @Value("${oauth2.external_client_secret}")
        private String external_client_secret;

        @Value("${oauth2.loginUrl}")
        private String loginUrl;

        @Value("${oauth2.checkUrl}")
        private String checkUrl;

        @Bean
        public JdbcClientDetailsService clientDetailsService(DataSource dataSource) {
            return new JdbcClientDetailsService(dataSource);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            //配置两个客户端,一个用于password认证一个用于client认证
//            clients.inMemory().withClient(internal_client_id)
//                    .authorizedGrantTypes("client_credentials", "refresh_token")
//                    .scopes("all")
//                    .authorities("client")
//                    .secret(passwordEncoder.encode(internal_client_secret))
//                    .and().withClient(external_client_id)
//                    .authorizedGrantTypes("password", "refresh_token")
//                    .scopes("select")
//                    .authorities("client")
//                    .secret(passwordEncoder.encode(external_client_secret));
            clients.withClientDetails(clientDetailsService(dataSource));
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .pathMapping("/oauth/token",loginUrl)
                    .pathMapping("/oauth/check_token",checkUrl)
                    .tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .tokenServices(authorizationServerTokenServices())
                    .accessTokenConverter(accessTokenConverter());
        }

//        @Bean
//        public JdbcTokenStore tokenStore(DataSource dataSource) {
//            return new JdbcTokenStore(dataSource);
//        }

        @Bean
        public InMemoryTokenStore tokenStore(){
            return new InMemoryTokenStore();
        }
//
//        @Bean
//        public TokenStore tokenStore() {
//            return new RedisTokenStore(connectionFactory);
//        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            //允许表单认证
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
            oauthServer.allowFormAuthenticationForClients();
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
            customTokenServices.setReuseRefreshToken(false);
            customTokenServices.setTokenEnhancer(accessTokenConverter());
            customTokenServices.setClientDetailsService(clientDetailsService(dataSource));
            return customTokenServices;
        }

    }

}
