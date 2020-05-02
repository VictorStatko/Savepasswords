package com.statkovit.personalAccountsService.configuration.tokens;

import com.statkovit.personalAccountsService.utils.WebUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Log4j2
public class CustomUserInfoTokenServices implements ResourceServerTokenServices {

    private static final String X_HTTP_CLIENT_IP_HEADER = "X-Http-Client-Ip";
    private static final String X_DEVICE_ID_HEADER = "X-Device-Id";

    private final String userInfoEndpointUrl;

    private final String clientId;

    private final AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

    private final PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

    public CustomUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);

        if (map.containsKey("error")) {
            log.debug("Userinfo returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }

        return extractAuthentication(map);
    }

    private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        Object principal = getPrincipal(map);
        List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);

        OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
                null, null, null, null);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        token.setDetails(map);

        return new OAuth2Authentication(request, token);
    }

    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = this.principalExtractor.extractPrincipal(map);
        return (principal == null ? "unknown" : principal);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    @SuppressWarnings({"unchecked"})
    private Map<String, Object> getMap(String path, String accessToken) {
        log.debug("Getting user info from: " + path);
        try {
            BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
            resource.setClientId(this.clientId);
            OAuth2RestOperations restTemplate = new OAuth2RestTemplate(resource);

            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;
                token.setTokenType(tokenType);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }

            HttpHeaders headers = new HttpHeaders();

            WebUtils.getRequestIp().ifPresent(ip -> headers.set(X_HTTP_CLIENT_IP_HEADER, ip));
            WebUtils.getDeviceId().ifPresent(id -> headers.set(X_DEVICE_ID_HEADER, id));

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(path, HttpMethod.GET, entity, Map.class).getBody();
        } catch (Exception ex) {
            log.error("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
            return Collections.singletonMap("error", "Could not fetch user details");
        }
    }

}