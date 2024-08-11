package com.semiramide.timetracker.core.usecase.impl;

import com.semiramide.timetracker.core.entity.Employee;
import com.semiramide.timetracker.core.service.NotificationService;
import com.semiramide.timetracker.core.usecase.NotificationUseCase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;

@Builder
public class NotificationUseCaseImpl implements NotificationUseCase {

  private final NotificationService notificationService;

  private final String SUBJECT = "Leave request";

  private final String UPDATE = "Leave request update";

  @Value("${tt.base-frontend-url}")
  private final String platformRedirectionUrl;

  @Override
  public void sendLeaveRequestMail(Employee employee, List<Employee> superiors) {
    String[] recipients = superiors.stream().map(Employee::getEmail).toArray(String[]::new);
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("employeeName", employee.getFirstName() + " " + employee.getLastName());
    templateModel.put("platformRedirectionUrl", platformRedirectionUrl + "/leaves");
    templateModel.put("senderName", employee.getFirstName());
    notificationService.sendMessageUsingThymeleafTemplate(
        employee.getEmail(), recipients, SUBJECT, templateModel);
  }

  @Override
  public void sendLeaveDetailsUpdatedMail(Employee employee, List<Employee> superiors) {
    String[] recipients = superiors.stream().map(Employee::getEmail).toArray(String[]::new);
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("employeeName", employee.getFirstName() + " " + employee.getLastName());
    templateModel.put("platformRedirectionUrl", platformRedirectionUrl + "/leaves");
    templateModel.put("senderName", employee.getFirstName());
    notificationService.sendMessageUsingThymeleafTemplate(
            employee.getEmail(), recipients, UPDATE, templateModel);
  }


  @Override
  public void sendLeaveResponseMail(Employee superior, Employee recipient) {
    String[] recipients = new String[] {recipient.getEmail()};
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("employeeName", superior.getFirstName() + " " + superior.getLastName());
    templateModel.put("platformRedirectionUrl", platformRedirectionUrl + "/leaves");
    templateModel.put("senderName", superior.getFirstName());
    notificationService.sendMessageUsingThymeleafTemplate(
        superior.getEmail(), recipients, "Leave response", templateModel);
  }
}
