package com.weer.weer_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDTO {

    private String name;
    private String tel;
    private String organization;

}
