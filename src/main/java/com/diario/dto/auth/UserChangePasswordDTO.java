package com.diario.dto.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDTO implements Serializable {
    private String oldPassword;
    private String password;
    private String confirmPassword;
}
