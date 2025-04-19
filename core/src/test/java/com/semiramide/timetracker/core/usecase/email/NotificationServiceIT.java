package com.semiramide.timetracker.core.usecase.email;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.semiramide.timetracker.core.service.NotificationService;
import com.semiramide.timetracker.core.usecase.ServiceConfig;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServiceConfig.class})
public class NotificationServiceIT {
    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(GreenMailConfiguration.aConfig().withUser("user123", "password123"))
                    .withPerMethodLifecycle(false);

    @Autowired
    private NotificationService notificationService;

    @Test
    void shouldSendEmail() throws MessagingException, IOException {
        notificationService.sendEmail(
                "user1@test.com",
                new String[]{"user2@test.com", "user3@test.com"},
                "subject",
                "body",
                false);

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(2, receivedMessages.length);
        assertEquals("subject", receivedMessages[0].getSubject());
        assertEquals("user3@test.com", receivedMessages[0].getAllRecipients()[1].toString());
    }
}
