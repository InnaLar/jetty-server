package ru.larina.model.dto.userReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTotalWorkByPeriodRs {
    private Long userId;
    private Duration totalWork;
}
