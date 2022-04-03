

package com.chat.im.im_oauth2server.config.oauth2;

import com.chat.im.im_oauth2server.config.error.MssWebResponseExceptionTranslator;
import com.chat.im.im_oauth2server.util.DigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * 授权服务配置
 * @author: AubreyChen in 2022/3/6
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    /**
     * 认证管理器
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyJwtTokenEnhancer jwtTokenEnhancer;

    /**
     * 自定义储存策略
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenEnhancer());
    }

    /**
     * 定义令牌端点上的安全性约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()");
                .checkTokenAccess("permitAll()");
    }

    /**
     * 用于定义客户端详细信息服务的配置程序。可以初始化客户端详细信息;
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // clients.withClientDetails(clientDetails());
        clients.inMemory().withClient("android").scopes("read").secret(new BCryptPasswordEncoder().encode(DigestUtil.encrypt("android")))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token").and().withClient("webapp")
                .scopes("read").authorizedGrantTypes("implicit").and().withClient("browser")
                .authorizedGrantTypes("refresh_token", "password").scopes("read");
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new MssWebResponseExceptionTranslator();
    }

    /**
     * 定义授权和令牌端点以及令牌服务
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tec = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(accessTokenEnhancer());
        delegates.add(jwtTokenEnhancer);
        tec.setTokenEnhancers(delegates);
        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(accessTokenEnhancer())
                .tokenEnhancer(tec)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(webResponseExceptionTranslator())
                .tokenServices(tokenService())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌管理服务相关配置，以及令牌信息的增强
     */
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        //支持刷新令牌
        service.setSupportRefreshToken(true);
        //令牌存储策略
        service.setTokenStore(tokenStore());

        //令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenEnhancer());
        tokenEnhancerChain.setTokenEnhancers(delegates);
        service.setTokenEnhancer(tokenEnhancerChain);

        // 令牌默认有效期2小时
        service.setAccessTokenValiditySeconds(5 * 60);
        // 刷新令牌默认有效期3天
        service.setRefreshTokenValiditySeconds(7200);
        return service;
    }

    /**
     * <p>
     * 注意，自定义TokenServices的时候，需要设置@Primary，否则报错，
     * </p>
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(accessTokenEnhancer());
        // tokenServices.setClientDetailsService(clientDetails());
        // token有效期自定义设置，默认12小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24 * 7);
        // tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        // refresh_token默认30天
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24 * 7);
        // tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return tokenServices;
    }

    /**
     * 定义jwt的生成方式
     */
    @Bean
    public JwtAccessTokenConverter accessTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 非对称加密，但jwt长度过长
        converter.setKeyPair(keyPair());
        // 对称加密
//         converter.setSigningKey("admin123");
        return converter;
    }

    @Bean
    public KeyPair keyPair() {
        return new KeyStoreKeyFactory(new ClassPathResource("spring-jwt.jks"), "admin123456".toCharArray())
                .getKeyPair("spring-jwt");
    }
}
