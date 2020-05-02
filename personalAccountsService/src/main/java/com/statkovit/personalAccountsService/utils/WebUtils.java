package com.statkovit.personalAccountsService.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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

    private static final String DEVICE_ID_HEADER = "X-Device-Id";

    private WebUtils() {
    }

    public static Optional<String> getRequestIp() {
        HttpServletRequest currentRequest;

        try {
            currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }


        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = currentRequest.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                return Optional.of(ipList.split(",")[0]);
            }
        }

        return Optional.ofNullable(currentRequest.getRemoteAddr());
    }

    public static Optional<String> getDeviceId() {
        HttpServletRequest currentRequest;

        try {
            currentRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (IllegalStateException ex) {
            return Optional.empty();
        }

        return Optional.ofNullable(currentRequest.getHeader(DEVICE_ID_HEADER));
    }
}
