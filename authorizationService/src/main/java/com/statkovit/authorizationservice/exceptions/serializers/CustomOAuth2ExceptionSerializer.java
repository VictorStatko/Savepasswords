package com.statkovit.authorizationservice.exceptions.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.statkolibraries.utils.DateTimeUtils;
import com.statkovit.authorizationservice.exceptions.CustomOAuth2Exception;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class CustomOAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

    public CustomOAuth2ExceptionSerializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(CustomOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("timestamp", DateTimeUtils.dateToISO8601Format(new Date()));
        jsonGenerator.writeNumberField("status", e.getHttpErrorCode());
        jsonGenerator.writeObjectField("message", "global.authError");
        jsonGenerator.writeStringField("error", e.getOAuth2ErrorCode());
        jsonGenerator.writeStringField("error_description", e.getMessage());

        if (e.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : e.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jsonGenerator.writeStringField(key, add);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
