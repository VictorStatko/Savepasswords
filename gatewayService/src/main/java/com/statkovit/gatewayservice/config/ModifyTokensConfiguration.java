package com.statkovit.gatewayservice.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.statkovit.gatewayservice.properties.CustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.COOKIE;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ModifyTokensConfiguration {

    private static final String COOKIE_SPLITTER = "; ";
    private static final String DEVICE = "Device";
    private static final String DEVICE_ID_HEADER = "X-Device-Id";

    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;

    @Bean
    public GlobalFilter customFilter() {
        return new GlobalAuthFilter();
    }

    private class GlobalAuthFilter implements GlobalFilter, Ordered {

        @Override
        public int getOrder() {
            return -2;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            ServerHttpRequestDecorator requestDecorator = processRequest(request);
            ServerHttpResponseDecorator responseDecorator = processResponse(request, response);

            return chain.filter(exchange.mutate().request(requestDecorator).response(responseDecorator).build());
        }
    }

    private ServerHttpRequestDecorator processRequest(ServerHttpRequest request) {
        HttpHeaders headers = new HttpHeaders();

        headers.putAll(request.getHeaders());

        Optional.ofNullable(headers.get(COOKIE)).ifPresent(cookies -> {
                    List<String> cookiesList = cookies.stream()
                            .flatMap(cookie -> Arrays.stream(cookie.split(COOKIE_SPLITTER)))
                            .filter(cookie -> !cookie.startsWith(AUTHORIZATION + "="))
                            .filter(cookie -> !cookie.startsWith(DEVICE + "="))
                            .collect(Collectors.toList());

                    if (cookiesList.isEmpty()) {
                        headers.remove(COOKIE);
                    } else {
                        headers.put(COOKIE, Collections.singletonList(String.join(COOKIE_SPLITTER, cookiesList)));
                    }
                }
        );

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        if (cookies.containsKey(AUTHORIZATION)) {
            Optional.ofNullable(cookies.getFirst(AUTHORIZATION))
                    .map(HttpCookie::getValue)
                    .ifPresent(token -> {
                        headers.put(AUTHORIZATION, Collections.singletonList("Bearer " + token));
                        log.debug("Successfully transferred access token from cookie to header. Access token {}.", replaceLastFour(token));
                    });
        }

        if (cookies.containsKey(DEVICE)) {
            Optional.ofNullable(cookies.getFirst(DEVICE))
                    .map(HttpCookie::getValue)
                    .ifPresent(device -> {
                        headers.put(DEVICE_ID_HEADER, Collections.singletonList(device));
                        log.debug("Successfully transferred device id from cookie to header. Device id {}.", device);
                    });
        }

        return new ServerHttpRequestDecorator(request) {
            @Override
            public HttpHeaders getHeaders() {
                return headers;
            }
        };
    }

    private ServerHttpResponseDecorator processResponse(ServerHttpRequest request, ServerHttpResponse response) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            @NonNull
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (!(body instanceof Flux)) {
                    return super.writeWith(body);
                }

                Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                if (isSuccessSignInRequest(request, response)) {
                    return super.writeWith(flux.buffer().map(dataBuffers -> {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        dataBuffers.forEach(buffer -> {
                            byte[] array = new byte[buffer.readableByteCount()];
                            buffer.read(array);
                            try {
                                outputStream.write(array);
                            } catch (IOException e) {
                                throw new RuntimeException(e.getMessage(), e);
                            }
                            DataBufferUtils.release(buffer);
                        });

                        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(
                                ByteBuffer.wrap(outputStream.toByteArray())
                        );

                        JsonNode fullResponseNode = readNode(charBuffer.toString());
                        JsonNode accessToken = fullResponseNode.get("access_token");
                        JsonNode expires = fullResponseNode.get("expires_in");
                        JsonNode deviceId = fullResponseNode.get("client_device_id");

                        if (isNodeContainValue(accessToken) && isNodeContainValue(expires)) {
                            String textToken = accessToken.asText();
                            response.addCookie(createCookie(AUTHORIZATION, textToken, expires.asLong()));
                            log.debug("Successfully added access token cookie. Token {}.", replaceLastFour(textToken));
                        } else {
                            log.error("Error during adding access token cookie. Access token present: {}, Expires present: {}",
                                    isNodeContainValue(accessToken), isNodeContainValue(expires)
                            );
                        }

                        if (isNodeContainValue(deviceId) && isNodeContainValue(expires)) {
                            String deviceIdText = deviceId.asText();
                            response.addCookie(createCookie(DEVICE, deviceIdText, expires.asLong()));
                            log.debug("Successfully added device cookie. Device {}.", deviceIdText);
                        } else {
                            log.error("Error during adding device cookie. Device present: {}, Expires present: {}",
                                    isNodeContainValue(deviceId), isNodeContainValue(expires)
                            );
                        }

                        return response.bufferFactory().wrap(fullResponseNode.toString().getBytes(StandardCharsets.UTF_8));
                    }));
                }

                if (isSuccessLogoutRequest(request, response)) {
                    HttpCookie oldToken = request.getCookies().getFirst(AUTHORIZATION);
                    HttpCookie oldDevice = request.getCookies().getFirst(DEVICE);

                    if (oldToken != null) {
                        response.addCookie(createCookie(AUTHORIZATION, "", 0L));
                        log.debug("Successfully added revoke token cookie. Old token {}.", replaceLastFour(oldToken.getValue()));
                    }

                    if (oldDevice != null) {
                        response.addCookie(createCookie(DEVICE, "", 0L));
                        log.debug("Successfully removed device cookie. Old device {}.", oldDevice);
                    }
                }

                return super.writeWith(body);

            }
        };
    }

    private boolean isSuccessSignInRequest(ServerHttpRequest request, ServerHttpResponse response) {
        return isSignInRequest(request)
                && response.getStatusCode() != null
                && response.getStatusCode().is2xxSuccessful();
    }

    private boolean isSignInRequest(ServerHttpRequest request) {
        return request.getPath().value().contains("auth/token");
    }

    private boolean isSuccessLogoutRequest(ServerHttpRequest request, ServerHttpResponse response) {
        return request.getPath().value().contains("auth/logout")
                && response.getStatusCode() != null
                && response.getStatusCode().is2xxSuccessful();
    }

    private ResponseCookie createCookie(String name, String value, Long maxAge) {
        return ResponseCookie.from(name, value)
                .path("/api")
                .httpOnly(true)
                .secure(customProperties.getCookie().isSecured())
                .maxAge(maxAge)
                .build();
    }

    private JsonNode readNode(String in) {
        try {
            return objectMapper.readTree(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String replaceLastFour(String s) {
        int length = s.length();
        if (length < 12) {
            return "***Can't show token because of small length.***";
        }

        return s.substring(0, length - 12) + "************";
    }

    public boolean isNodeContainValue(JsonNode node) {
        return node != null && !node.isMissingNode();
    }
}
