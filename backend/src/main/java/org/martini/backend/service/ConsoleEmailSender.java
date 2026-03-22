package org.martini.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.martini.backend.model.EmailDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev"})
public class ConsoleEmailSender implements EmailSender {
    @Override
    public void sendMail(EmailDto emailDto) {
        log.info("Following mail is going to be send: {}", emailDto.toString());
    }
}
