package com.diario.model;

import com.diario.valueobjects.Role;
import io.smallrye.common.constraint.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user_table")
@Getter
@Setter
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name=User.USER_SEQ, sequenceName = User.USER_SEQ, allocationSize = 1)
public class User implements Serializable {
    public static final String USER_SEQ = "USER_SEQ";

    public User(@NotNull String email, @NotNull String password, String name, String imageUrl, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imageUrl = imageUrl;
        this.roles = roles;
        if(this.roles == null || this.roles.isEmpty()){
            this.roles = Collections.singleton(Role.USER);
        }
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator=USER_SEQ)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotNull
    @Column
    protected String password;
    @Column
    protected String name;
    @Column
    protected String imageUrl;
    @Email
    @NotNull
    @Column(unique = true, length = 100)
    protected String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

}
