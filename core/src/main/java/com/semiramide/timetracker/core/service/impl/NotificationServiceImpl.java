package com.semiramide.timetracker.core.service.impl;

import com.semiramide.timetracker.core.service.NotificationService;
import jakarta.mail.internet.MimeMessage;

import java.util.Map;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Builder
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @SneakyThrows
    @Override
    public void sendMessageUsingThymeleafTemplate(
            String sender, String[] recipients, String subject, Map<String, Object> templateModel) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);
        sendEmail(sender, recipients, subject, htmlBody, true);
    }

    @Override
    public void sendEmail(
            String sender, String[] recipients, String subject, String body, boolean isHtmlBody) {
        log.info("Sending message. Sender: " + sender + "; Recipients: " + recipients);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            //TODO: Sender shouldn't be hardcoded
            helper.setFrom("semiramide.tt@semiramide.com");
            helper.setTo(recipients);
            helper.setSubject(subject);
            helper.setText(body, isHtmlBody);
            helper.addInline("attachment.png", new ClassPathResource("semiramide-logo.png"));
            javaMailSender.send(message);
            log.info("Message has been sent!");
        } catch (Exception ex) {
            log.error("Exception occurred during sending of mails", ex);
        }
    }
}
