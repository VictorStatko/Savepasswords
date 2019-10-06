package com.statkovit.gatewayservice.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Configuration
@Log4j2
public class RequestLoggingConfiguration {

    @Bean
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    private static class RequestLoggingFilter implements GlobalFilter {

        private static final String LOG_FORMAT = "Incoming request %s is routed to id: %s, uri: %s";
        private static final String UNKNOWN = "Unknown";

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            Set<URI> uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());

            String originalUri = (CollectionUtils.isEmpty(uris) ? UNKNOWN : uris.iterator().next().toString());

            URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);

            Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
            String routeId = route != null ? route.getId() : UNKNOWN;

            log.debug(String.format(LOG_FORMAT, originalUri, routeId, routeUri));
            return chain.filter(exchange);
        }
    }
}
