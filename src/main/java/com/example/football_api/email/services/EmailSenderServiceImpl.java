package com.example.football_api.email.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSenderServiceImpl.class);
    private final JavaMailSender mailSender;
    //@Value(value = "${spring.mail.username}")
    private final static String emailFrom = "football_api13@gmail.com";
    @Override
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom(emailFrom);
            mailSender.send(mimeMessage);
            System.out.println("mail wyslany:  " + to);
        }catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
