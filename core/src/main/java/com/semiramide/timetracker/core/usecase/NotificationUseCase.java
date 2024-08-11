package com.semiramide.timetracker.core.usecase;

import com.semiramide.timetracker.core.entity.Employee;
import java.util.List;

public interface NotificationUseCase {

  void sendLeaveRequestMail(Employee employee, List<Employee> recipients);

  void sendLeaveDetailsUpdatedMail(Employee employee, List<Employee> recipients);

  void sendLeaveResponseMail(Employee superior, Employee recipient);
}
