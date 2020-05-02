package com.statkovit.authorizationservice.configuration;

import com.statkovit.authorizationservice.configuration.enhancers.AdditionalInformationTokenEnhancer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
@Log4j2
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private static final String PERMIT_ALL = "permitAll()";

    private final UserDetailsService userDetailsService;
    private final ClientDetailsService authClientDetailsService;
    private final PasswordEncoder encoder;
    private final TokenStore tokenStore;
    private final WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator;
    private final AdditionalInformationTokenEnhancer tokenEnhancer;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(authClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(tokenServices())
                .exceptionTranslator(exceptionTranslator);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess(PERMIT_ALL)
                .passwordEncoder(encoder)
                .allowFormAuthenticationForClients();
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setClientDetailsService(authClientDetailsService);
        tokenServices.setTokenEnhancer(tokenEnhancer);
        return tokenServices;
    }
}
