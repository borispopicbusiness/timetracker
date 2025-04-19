package com.semiramide.timetracker.core.service;

import java.util.Map;

public interface NotificationService {
    void sendMessageUsingThymeleafTemplate(
            String sender, String[] recipients, String subject, Map<String, Object> templateModel);

    void sendEmail(
            String sender, String[] recipients, String subject, String body, boolean isHtmlBody);
}
