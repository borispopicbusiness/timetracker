package com.semiramide.timetracker.adapters.api.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String newPassword;
}
