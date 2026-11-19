package org.lkcode.start_spring_security.event;

import org.lkcode.start_spring_security.model.User;
import org.lkcode.start_spring_security.service.EmailService;
import org.lkcode.start_spring_security.service.VerificationTokenService;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public RegistrationListener(VerificationTokenService verificationTokenService, EmailService emailService) {
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);;
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:8080/verify?token=" + token;
        String message = "Please click the following link to verify your email: " + confirmationUrl;
        emailService.sendEmail(recipientAddress, subject, message);
    }
}
