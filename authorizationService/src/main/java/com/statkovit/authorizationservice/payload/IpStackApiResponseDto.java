package com.statkovit.authorizationservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpStackApiResponseDto {
    private ErrorDto error;
    private String ip;
    private String country_name;
    private String city;

    private static final class ErrorDto {
        private int code;
        private String type;
        private String info;
    }
}
