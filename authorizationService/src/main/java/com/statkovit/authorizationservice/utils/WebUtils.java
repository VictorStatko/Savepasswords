package com.statkovit.authorizationservice.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Log4j2
public final class WebUtils {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private static final String X_HTTP_CLIENT_IP_HEADER = "X-Http-Client-Ip";
    private static final String DEVICE_ID_HEADER = "X-Device-Id";
    private static final String SYSTEM_INFO_HEADER = "X-System-Info";
    private static final String UNKNOWN = "unknown";


    private WebUtils() {
    }

    public static Optional<String> getClientIpFromRequest() {
        HttpServletRequest currentRequest;

        try {
            currentRequest = getCurrentRequest();
        } catch (IllegalStateException ex) {
            log.error(ex.getMessage(), ex);
            return Optional.empty();
        }

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = currentRequest.getHeader(header);
            if (nonNull(ipList) && ipList.length() > 0 && !UNKNOWN.equalsIgnoreCase(ipList)) {
                return Optional.of(ipList.split(",")[0]);
            }
        }

        return Optional.ofNullable(currentRequest.getRemoteAddr());
    }

    public static Optional<String> getClientIpFromInternalHeader(HttpServletRequest currentRequest) {
        return Optional.ofNullable(currentRequest.getHeader(X_HTTP_CLIENT_IP_HEADER));
    }

    public static Optional<String> getDeviceId() {
        try {
            return Optional.ofNullable(getCurrentRequest().getHeader(DEVICE_ID_HEADER));
        } catch (IllegalStateException ex) {
            log.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    public static Optional<String> getSystemInfo() {
        try {
            return Optional.ofNullable(getCurrentRequest().getHeader(SYSTEM_INFO_HEADER));
        } catch (IllegalStateException ex) {
            log.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    public static Optional<String> getBearerTokenValue() {
        try {
            HttpServletRequest request = getCurrentRequest();
            return getBearerTokenValue(request);
        } catch (IllegalStateException ex) {
            log.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    public static Optional<String> getBearerTokenValue(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            log.debug("Authorization header is not provided.");
            return Optional.empty();
        }

        try {
            return Optional.of(authorization.split("Bearer ")[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public static boolean isPrivateIpAddress(String ip) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ip);

        return address.isSiteLocalAddress();
    }

    private static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
