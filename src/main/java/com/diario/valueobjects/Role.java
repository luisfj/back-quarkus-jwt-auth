package com.diario.valueobjects;

import lombok.ToString;

@ToString
public enum Role {
    ADMIN(Names.ADMIN), USER(Names.USER);

    public class Names{
        public static final String ADMIN = "Admin";
        public static final String USER = "User";
    }

    String descricao;
    private Role(String descricao){this.descricao = descricao;}

    public String getDescricao(){
        return this.descricao;
    }

}
