package ru.larina.model.dto.userReportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTimeRequest {
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime stopTime;
}
