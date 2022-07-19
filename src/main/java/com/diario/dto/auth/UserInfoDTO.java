package com.diario.dto.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO implements Serializable {
    private String email;
    private String imageUrl;
    private String name;
}
