package ru.larina.model.dto.userDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPutResponse {
    private Long id;
    private String eml;
}
