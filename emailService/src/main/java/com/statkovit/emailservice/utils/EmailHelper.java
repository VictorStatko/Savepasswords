package com.statkovit.emailservice.utils;

import com.statkovit.emailservice.properties.CustomProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component
@RequiredArgsConstructor
@Log4j2
public class EmailHelper {

    private static final String IMAGE_RESOURCE = "/images/%s";
    private static final String IMAGE_CONTENT_TYPE = "image/png";

    private final SesClient sesClient;
    private final CustomProperties customProperties;

    public boolean sendEmail(String subject, String recipient, String html, String... imageNames) {
        try {
            Session session = Session.getDefaultInstance(new Properties());

            MimeMessage message = new MimeMessage(session);

            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setSubject(subject);
            helper.setFrom(customProperties.getEmail().getFrom());
            helper.setTo(recipient);

            helper.setText(html, true);

            for (String imageName : imageNames) {
                setImageResource(helper, imageName);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);

            ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());

            byte[] messageBytesArray = new byte[buffer.remaining()];
            buffer.get(messageBytesArray);

            SdkBytes data = SdkBytes.fromByteArray(messageBytesArray);

            RawMessage rawMessage = RawMessage.builder()
                    .data(data)
                    .build();

            SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder()
                    .rawMessage(rawMessage)
                    .build();

            sesClient.sendRawEmail(rawEmailRequest);
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
