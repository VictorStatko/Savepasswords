package com.statkovit.authorizationservice.configuration;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.services.impl.CustomAuthClientDetailsService;
import com.statkovit.authorizationservice.services.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import java.util.List;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private static final String TOKEN_ROUTE = ServerConstants.API_ROUTE + "auth/token";
    private static final String CHECK_TOKEN_ROUTE = TOKEN_ROUTE + "/check";

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthClientDetailsService authClientDetailsService;
    private final PasswordEncoder encoder;
    private final TokenStore tokenStore;
    private final WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator;

    //Constructor qualifier in lombok is not working for me https://github.com/rzwitserloot/lombok/issues/745
    @Autowired
    @Qualifier("springSecurityFilterChain")
    private Filter springSecurityFilterChain;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .withClientDetails(authClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(exceptionTranslator)
                .reuseRefreshTokens(false)
                .pathMapping("/oauth/token", TOKEN_ROUTE)
                .pathMapping("/oauth/check_token", CHECK_TOKEN_ROUTE);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(encoder)
                .allowFormAuthenticationForClients();
    }

    //FUCK IT https://www.gitmemory.com/issue/spring-projects/spring-security-oauth/1664/490335489
    @PostConstruct
    public void updateClientCredentialsTokenEndpointFilterAuthenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionTranslator(exceptionTranslator);
        authenticationEntryPoint.setTypeName("Form");

        FilterChainProxy filterChainProxy = (FilterChainProxy) springSecurityFilterChain;
        List<SecurityFilterChain> list = filterChainProxy.getFilterChains();
        list.stream()
                .flatMap(chain -> chain.getFilters().stream())
                .forEach(filter -> {
                    if (filter instanceof ClientCredentialsTokenEndpointFilter) {
                        ((ClientCredentialsTokenEndpointFilter) filter).setAuthenticationEntryPoint(authenticationEntryPoint);
                    }
                });
    }

}
