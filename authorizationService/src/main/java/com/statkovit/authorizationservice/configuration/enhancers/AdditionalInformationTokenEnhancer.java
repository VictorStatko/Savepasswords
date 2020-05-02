package com.statkovit.authorizationservice.configuration.enhancers;

import com.statkovit.authorizationservice.feign.IpStackRestClient;
import com.statkovit.authorizationservice.payload.IpStackApiResponseDto;
import com.statkovit.authorizationservice.utils.ObjectMapperUtils;
import com.statkovit.authorizationservice.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@SuppressWarnings("deprecation")
@RequiredArgsConstructor
@Log4j2
@Component
public class AdditionalInformationTokenEnhancer implements TokenEnhancer {

    public static final String CLIENT_IP_ADDRESS = "client_ip_address";
    public static final String CLIENT_DEVICE_ID = "client_device_id";
    public static final String SYSTEM_INFO = "system_info";
    public static final String GEO_INFO = "geo_info";
    public static final String CREATED_AT = "created_at";

    private final IpStackRestClient ipStackRestClient;
    private final ObjectMapperUtils objectMapperUtils;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();

        WebUtils.getClientIpFromRequest().ifPresent(ip -> {
                    additionalInfo.put(CLIENT_IP_ADDRESS, ip);

                    String geoInfo = fetchGeoInfo(ip);

                    if (StringUtils.isNotEmpty(geoInfo)) {
                        additionalInfo.put(GEO_INFO, geoInfo);
                    }
                }
        );
        WebUtils.getDeviceId().ifPresent(deviceId -> additionalInfo.put(CLIENT_DEVICE_ID, deviceId));
        WebUtils.getSystemInfo().ifPresent(systemInfo -> additionalInfo.put(SYSTEM_INFO, systemInfo));

        additionalInfo.put(CREATED_AT, Instant.now());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

    private String fetchGeoInfo(String ip) {
        try {
            IpStackApiResponseDto geoDto = WebUtils.isPrivateIpAddress(ip)
                    ? ipStackRestClient.getLocalIpInfo()
                    : ipStackRestClient.getIpInfo(ip);

            log.debug("Response from api.ipstack.com: {}", objectMapperUtils.safelyConvertObjectToString(geoDto));

            if (nonNull(geoDto.getError())) {
                return null;
            }

            return Stream.of(geoDto.getCountry_name(), geoDto.getCity())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
