package com.xzyler.microservices.blogservice.generic.api.pagination.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomFilter {

    private String field;
    private String searchValue;
}
