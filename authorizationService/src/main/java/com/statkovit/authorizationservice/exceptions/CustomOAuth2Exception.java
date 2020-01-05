package com.statkovit.authorizationservice.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.statkovit.authorizationservice.exceptions.serializers.CustomOAuth2ExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.Objects;

@JsonSerialize(using = CustomOAuth2ExceptionSerializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {
    private String error;
    private String errorDescription;
    private Integer httpErrorCode;

    public CustomOAuth2Exception(String msg) {
        super(msg);
        this.errorDescription = msg;
    }

    public CustomOAuth2Exception(OAuth2Exception e) {
        super(e.getMessage());
        this.error = e.getOAuth2ErrorCode();
        this.errorDescription = e.getMessage();
        this.httpErrorCode = e.getHttpErrorCode();
    }

    @Override
    public String getOAuth2ErrorCode() {
        if (Objects.nonNull(error)) {
            return error;
        }
        return super.getOAuth2ErrorCode();
    }

    @Override
    public String getMessage() {
        if (Objects.nonNull(errorDescription)) {
            return errorDescription;
        }
        return super.getMessage();
    }

    @Override
    public int getHttpErrorCode() {
        if (Objects.nonNull(httpErrorCode)) {
            return httpErrorCode;
        }
        return super.getHttpErrorCode();
    }
}
