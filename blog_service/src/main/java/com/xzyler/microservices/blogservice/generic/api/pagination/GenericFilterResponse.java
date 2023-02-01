package com.xzyler.microservices.blogservice.generic.api.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GenericFilterResponse {
    private Long totalElements;
    private int totalPages;
    private List<?> content;
}
