package ru.larina.model.dto.userReportDTO;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class UserTotalWorkByPeriodResponse {
    private Long userId;
    private Duration totalWork;
}
