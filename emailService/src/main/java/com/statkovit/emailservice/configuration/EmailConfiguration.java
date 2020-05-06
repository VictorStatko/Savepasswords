package com.statkovit.emailservice.configuration;

import com.statkovit.emailservice.properties.CustomProperties;
import com.statkovit.emailservice.properties.CustomProperties.Aws;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class EmailConfiguration {

    private final CustomProperties customProperties;

    @Bean
    public SesClient amazonSesClient() {
        return SesClient.builder()
                .credentialsProvider(getCredentialsProvider())
                .region(Region.EU_WEST_1)
                .build();
    }

    private AwsCredentials getCredentials() {
        final Aws aws = customProperties.getAws();
        return AwsBasicCredentials.create(aws.getAccessKey(), aws.getSecretKey());
    }

    private AwsCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(getCredentials());
    }
}
