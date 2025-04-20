package com.semiramide.timetracker.adapters.api.response;

import com.semiramide.timetracker.core.exception.TimetrackerException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;

    public static ErrorResponse getInstance(TimetrackerException exception) {
        return ErrorResponse.builder()
                .errorCode(exception.getCode())
                .errorMessage(exception.getMessage())
                .build();
    }

}
