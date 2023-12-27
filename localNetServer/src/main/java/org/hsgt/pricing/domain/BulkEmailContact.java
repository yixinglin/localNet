package org.hsgt.pricing.domain;

import lombok.Data;

@Data
public class BulkEmailContact {
    long id;
    String email;
    String firstName;
    String lastName;
    Boolean subscribed;
    String sentAt; // Date time
    String unsubscribedLink;
}
