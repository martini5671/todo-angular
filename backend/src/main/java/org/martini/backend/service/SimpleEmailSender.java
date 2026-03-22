package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.EmailDto;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Profile({"test", "prod"})
@Component
@RequiredArgsConstructor
public class SimpleEmailSender implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(EmailDto emailDto) {
        javaMailSender.send(mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(emailDto.to());
            mimeMessageHelper.setFrom(emailDto.from());
            mimeMessageHelper.setSubject(emailDto.subject());
            mimeMessageHelper.setText(emailDto.content(), false);
        });
    }
}
