package com.diario.dto.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO implements Serializable {
    private String email;
    private String password;
    private String fullName;
    private String confirmPassword;
}
