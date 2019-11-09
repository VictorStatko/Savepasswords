package com.statkovit.gatewayservice.config;

import com.statkovit.gatewayservice.feign.AuthServiceRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.COOKIE;

@Configuration
@RequiredArgsConstructor
public class GlobalAuthFilterConfiguration {

    private final AuthServiceRestClient authServiceRestClient;

    private static final String COOKIE_SPLITTER = "; ";

    @Bean
    public GlobalFilter customFilter() {
        return new GlobalAuthFilter();
    }

    private class GlobalAuthFilter implements GlobalFilter {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest.Builder requestMutation = exchange.getRequest().mutate();
            Optional.ofNullable(exchange.getRequest().getHeaders().get(COOKIE))
                    .ifPresent(cookieHeaders -> {
                                List<String> cookiesList = cookieHeaders.stream()
                                        .flatMap(cookieHeader -> Arrays.stream(cookieHeader.split(COOKIE_SPLITTER)))
                                        .filter(cookie -> !cookie.startsWith(AUTHORIZATION + "="))
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(cookiesList)) {
                                    requestMutation.headers(httpHeaders -> httpHeaders.remove(COOKIE));
                                } else {
                                    requestMutation.header(COOKIE, new String[]{String.join(COOKIE_SPLITTER, cookiesList)});
                                }
                            }
                    );

            MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();

            if (cookies.containsKey(AUTHORIZATION)) {
                Optional.ofNullable(cookies.getFirst(AUTHORIZATION))
                        .map(HttpCookie::getValue)
                        .map(opaqueToken -> {
                            ResponseEntity<Void> exchangeTokensResponse = authServiceRestClient.exchangeAuthToken(opaqueToken);
                            return exchangeTokensResponse.getHeaders().getFirst(AUTHORIZATION);
                        })
                        .ifPresent(jwtToken -> requestMutation.header(AUTHORIZATION, new String[]{jwtToken}));
            }

            return chain.filter(exchange.mutate().request(requestMutation.build()).build());
        }

    }
}
