package com.statkovit.emailservice.utils;

import com.statkovit.emailservice.properties.SpringProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Log4j2
public class EmailHelper {

    private static final String IMAGE_RESOURCE = "/images/%s";
    private static final String IMAGE_CONTENT_TYPE = "image/png";

    private final JavaMailSender emailSender;
    private final SpringProperties springProperties;

    public boolean sendEmail(String subject, String recipient, String html, String... imageNames) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setSubject(subject);
            helper.setTo(recipient);
            helper.setFrom(springProperties.getMail().getUsername());
            helper.setText(html, true);

            for (String imageName : imageNames) {
                setImageResource(helper, imageName);
            }

            emailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }

    private void setImageResource(MimeMessageHelper helper, String imageName) throws IOException, MessagingException {
        final InputStreamSource imageSource = new ByteArrayResource(
                IOUtils.toByteArray(
                        getClass().getResourceAsStream(String.format(IMAGE_RESOURCE, imageName))
                )
        );

        helper.addInline(imageName, imageSource, IMAGE_CONTENT_TYPE);
    }
}
