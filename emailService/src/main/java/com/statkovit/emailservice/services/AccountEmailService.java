package com.statkovit.emailservice.services;

import com.statkovit.emailservice.payload.AccountVerificationRequestedDto;
import com.statkovit.emailservice.properties.CustomProperties;
import com.statkovit.emailservice.utils.EmailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountEmailService {

    private final EmailHelper emailHelper;
    private final SpringTemplateEngine springTemplateEngine;
    private final CustomProperties customProperties;

    public void sendAccountVerificationEmail(AccountVerificationRequestedDto dto) {
        String htmlTemplateLocation;
        String subject;
        //TODO replace text to messages and use separate files (messages.en and messages.ru)
        switch (dto.getLocale()) {
            case "en":
                htmlTemplateLocation = "en/registrationConfirmation.html";
                subject = "Registration confirmation";
                break;
            case "ru":
                htmlTemplateLocation = "ru/registrationConfirmation.html";
                subject = "Подтверждение регистрации";
                break;
            default:
                log.warn("Correct locale is not provided (Verification email request for {}). Fallback to default.", dto.getEmail());
                htmlTemplateLocation = "en/registrationConfirmation.html";
                subject = "Registration confirmation";
        }

        final Context context = new Context();

        context.setVariable("frontendUrl", customProperties.getFrontend().getUrl());
        context.setVariable("verificationCode", dto.getVerificationCode());

        final String htmlContent = springTemplateEngine.process(htmlTemplateLocation, context);

        boolean result = emailHelper.sendEmail(subject, dto.getEmail(), htmlContent);

        if (result) {
            log.debug("Successfully sent account verification email for {}.", dto.getEmail());
        } else {
            log.warn("Failed sent account verification email for {}.", dto.getEmail());
        }
    }
}
