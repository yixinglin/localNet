package org.hsgt.entities.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private String username;
    private String password;
    private String nickName;
    private String email;
    private String phone;
    private String signature;
    private String profile;
}
