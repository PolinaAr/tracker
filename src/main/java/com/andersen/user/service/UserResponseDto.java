package com.andersen.user.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String lastname;
    private String email;
}
