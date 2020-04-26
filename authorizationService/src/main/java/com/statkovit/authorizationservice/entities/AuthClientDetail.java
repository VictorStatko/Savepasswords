package com.statkovit.authorizationservice.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "auth_client_detail")
@SequenceGenerator(name = "default_gen", sequenceName = "auth_client_detail_id_seq", allocationSize = 1)
@Getter
@Setter
public class AuthClientDetail implements ClientDetails {

    public static final int MAX_LENGTH__CLIENT_ID = 256;
    public static final int MAX_LENGTH__CLIENT_SECRET = 256;
    public static final int MAX_LENGTH__GRANT_TYPES = 256;
    public static final int MAX_LENGTH__SCOPES = 256;
    public static final int MAX_LENGTH__RESOURCES = 256;
    public static final int MAX_LENGTH__REDIRECT_URIS = 256;
    public static final int MAX_LENGTH__ADDITIONAL = 4096;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    @Column(name = "id")
    private String id;

    @Column(name = "client_id", nullable = false, unique = true, length = MAX_LENGTH__CLIENT_ID)
    private String clientId;

    @Column(name = "client_secret", length = MAX_LENGTH__CLIENT_SECRET)
    private String clientSecret;

    @Column(name = "authorized_grant_types", length = MAX_LENGTH__GRANT_TYPES)
    private String grantTypes;

    @Column(name = "scope", length = MAX_LENGTH__SCOPES)
    private String scopes;

    @Column(name = "resource_ids", length = MAX_LENGTH__RESOURCES)
    private String resources;

    @Column(name = "web_server_redirect_uri", length = MAX_LENGTH__REDIRECT_URIS)
    private String redirectUris;

    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    @Column(name = "additional_information", length = MAX_LENGTH__ADDITIONAL)
    private String additionalInformation;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resources != null ? new HashSet<>(Arrays.asList(resources.split(","))) : null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return false;
    }

    @Override
    public Set<String> getScope() {
        return scopes != null ? new HashSet<>(Arrays.asList(scopes.split(","))) : null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return grantTypes != null ? new HashSet<>(Arrays.asList(grantTypes.split(","))) : null;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return redirectUris != null ? new HashSet<>(Arrays.asList(redirectUris.split(","))) : null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValidity;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>(Collections.singletonMap("additional", additionalInformation));
    }
}
