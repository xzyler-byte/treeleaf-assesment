package com.xzyler.microservices.securityservice.dto.generics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveToggle {
    private long id;
    private boolean status;
}
