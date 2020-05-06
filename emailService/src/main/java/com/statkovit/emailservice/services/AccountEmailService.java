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
        final Context context = new Context();
        context.setVariable("frontendUrl", customProperties.getFrontend().getUrl());
        context.setVariable("verificationCode", dto.getVerificationCode());

        final String htmlContent = springTemplateEngine.process("registrationConfirmation.html", context);

        boolean result = emailHelper.sendEmail("Registration confirmation", dto.getEmail(), htmlContent);

        if (result) {
            log.debug("Successfully sent account verification email for {}.", dto.getEmail());
        } else {
            log.warn("Failed sent account verification email for {}.", dto.getEmail());
        }
    }
}
