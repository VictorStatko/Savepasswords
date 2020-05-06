package com.statkovit.emailservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class ObjectMapperUtils {

    private final ObjectMapper objectMapper;

    public String safelyConvertObjectToString(Object object) {
        try {
            return objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }
}
