package org.hsgt.core.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String introduction;
    private String email;
    private String phone;
    private List<String> roles;
}
