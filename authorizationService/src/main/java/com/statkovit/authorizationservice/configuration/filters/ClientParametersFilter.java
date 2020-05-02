package com.statkovit.authorizationservice.configuration.filters;

import com.statkovit.authorizationservice.configuration.OAuth2ResourceServerConfig;
import com.statkovit.authorizationservice.configuration.enhancers.AdditionalInformationTokenEnhancer;
import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.utils.WebUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Objects.isNull;

@SuppressWarnings("deprecation")
@Log4j2
public class ClientParametersFilter implements Filter {

    private final DefaultTokenServices defaultTokenServices;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public ClientParametersFilter(DefaultTokenServices defaultTokenServices, AuthenticationEntryPoint authenticationEntryPoint) {
        this.defaultTokenServices = defaultTokenServices;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        for (String route : OAuth2ResourceServerConfig.PERMIT_ALL_ROUTES) {
            if (route.equals(httpRequest.getRequestURI())) {
                httpRequest = removeAuthorizationHeader((HttpServletRequest) request);
                log.debug("Wrapped http request for skipping authorization header.");
                break;
            }
        }

        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> bearerToken = WebUtils.getBearerTokenValue(httpRequest);

        if (bearerToken.isEmpty()) {
            chain.doFilter(httpRequest, httpResponse);
            log.debug("Bearer token is not provided. Filter execution was skipped.");
            return;
        }

        OAuth2AccessToken accessToken = defaultTokenServices.readAccessToken(bearerToken.get());

        if (isNull(accessToken)) {
            log.debug("OAuth2AccessToken is not found. Filter execution was skipped.");
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();

        String clientIp = (String) additionalInformation.get(AdditionalInformationTokenEnhancer.CLIENT_IP_ADDRESS);
        String clientDeviceId = (String) additionalInformation.get(AdditionalInformationTokenEnhancer.CLIENT_DEVICE_ID);

        boolean validClientIp = isClientIpValid(clientIp, httpRequest);

        if (!validClientIp) {
            authenticationEntryPoint.commence(httpRequest, httpResponse, new BadCredentialsException("Usage of this token is not allowed for request ip."));
            return;
        }

        boolean validClientId = isClientDeviceIdValid(clientDeviceId);

        if (!validClientId) {
            authenticationEntryPoint.commence(httpRequest, httpResponse, new BadCredentialsException("Usage of this token is not allowed for request device id."));
            return;
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    private boolean isClientIpValid(String tokenClientIp, HttpServletRequest request) {
        Optional<String> requestClientIp = request.getRequestURI().startsWith(ServerConstants.INTERNAL_API_ROUTE) ?
                WebUtils.getClientIpFromInternalHeader(request)
                : WebUtils.getClientIpFromRequest();

        boolean validity = areOptionalValuesValid(Optional.ofNullable(tokenClientIp), requestClientIp);

        if (!validity) {
            log.error("Client ip validation failed. Expected: {}, Actual: {}.", tokenClientIp, requestClientIp.orElse(null));
        }

        return validity;
    }

    private boolean isClientDeviceIdValid(String tokenClientDeviceId) {
        Optional<String> requestClientDeviceId = WebUtils.getDeviceId();

        boolean validity = areOptionalValuesValid(Optional.ofNullable(tokenClientDeviceId), requestClientDeviceId);

        if (!validity) {
            log.error("Client device id validation failed. Expected: {}, Actual: {}.", tokenClientDeviceId, requestClientDeviceId.orElse(null));
        }

        return validity;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean areOptionalValuesValid(Optional<String> firstValueOptional, Optional<String> secondValueOptional) {
        if (firstValueOptional.isEmpty() && secondValueOptional.isPresent()) {
            return false;
        }

        if (firstValueOptional.isPresent() && secondValueOptional.isEmpty()) {
            return false;
        }

        if (firstValueOptional.isEmpty()) {
            return true;
        }

        return firstValueOptional.get().equals(secondValueOptional.get());
    }

    private HttpServletRequest removeAuthorizationHeader(HttpServletRequest request) {
        return new HttpServletRequestWrapper(request) {
            private Set<String> headerNameSet;

            @Override
            public String getHeader(String name) {
                if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                    return null;
                }
                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name)) {
                    return Collections.emptyEnumeration();
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                if (headerNameSet == null) {
                    headerNameSet = new HashSet<>();
                    Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
                    while (wrappedHeaderNames.hasMoreElements()) {
                        String headerName = wrappedHeaderNames.nextElement();
                        if (!HttpHeaders.AUTHORIZATION.equalsIgnoreCase(headerName)) {
                            headerNameSet.add(headerName);
                        }
                    }
                }
                return Collections.enumeration(headerNameSet);
            }
        };
    }
}
