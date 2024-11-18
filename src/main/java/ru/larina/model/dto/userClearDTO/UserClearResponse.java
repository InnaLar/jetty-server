package ru.larina.model.dto.userClearDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserClearResponse {
    private Integer userId;
}
