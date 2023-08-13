package org.hsgt.entities.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String introduction;
    private String email;
    private String phone;
    private String[] roles;
}
