package ru.larina.model.dto.userReportDTO;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class UserTotalWorkByPeriodResponse {
    private Integer userId;
    private Duration totalWork;
}
