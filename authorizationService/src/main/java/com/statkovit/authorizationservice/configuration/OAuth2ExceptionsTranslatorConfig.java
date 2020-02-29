package com.statkovit.authorizationservice.configuration;

import com.statkovit.authorizationservice.exceptions.CustomOAuth2Exception;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

@SuppressWarnings("deprecation")
/**
 * Translates OAuth2Exception response into common application exception response like ErrorDto.
 */
@Configuration
public class OAuth2ExceptionsTranslatorConfig {

    @Bean
    public WebResponseExceptionTranslator<OAuth2Exception> oauth2ResponseExceptionTranslator() {
        return exception -> {
            if (exception instanceof OAuth2Exception) {
                OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
                return ResponseEntity
                        .status(oAuth2Exception.getHttpErrorCode())
                        .body(new CustomOAuth2Exception(oAuth2Exception));
            } else if (exception instanceof AuthenticationException) {
                AuthenticationException authenticationException = (AuthenticationException) exception;
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new CustomOAuth2Exception(new UnauthorizedClientException(authenticationException.getMessage())));
            }

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CustomOAuth2Exception("OAuth Error"));
        };
    }
}
