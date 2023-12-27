package org.hsgt.core.domain;

import lombok.Data;

@Data
public class SystemConfigure {
    String variable;
    String value;
    String setTime;
    String setBy;
    String note;
}
