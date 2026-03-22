package org.martini.backend.service;

import org.martini.backend.model.EmailDto;

public interface EmailSender {
    void sendMail(EmailDto emailDto);
}
