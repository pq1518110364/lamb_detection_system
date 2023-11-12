package com.limen2023.lamb.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRoleDTO {
    private Long userId;
    private List<Long> roleIds;
}
