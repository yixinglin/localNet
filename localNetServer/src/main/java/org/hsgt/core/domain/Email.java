package org.hsgt.core.domain;

import lombok.Data;

@Data
public class Email {
    String server;
    String transportType;
    String emailAddress;
    String username;
    String password;
    String subject;
    String body;
}
