package com.statkovit.authorizationservice.configuration;

import com.statkovit.authorizationservice.configuration.filters.ClientParametersFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final DefaultTokenServices defaultTokenServices;

    public static final String[] PERMIT_ALL_ROUTES = {
            "/api/v1/auth/accounts",
            "/api/v1/auth/accounts/client-encryption-salt",
            "/oauth/token"
    };

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(PERMIT_ALL_ROUTES)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().addFilterBefore(customResourceServerFilter(), AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Bean
    public ClientParametersFilter customResourceServerFilter() {
        return new ClientParametersFilter(defaultTokenServices, new OAuth2AuthenticationEntryPoint());
    }
}
