package com.xzyler.microservices.blogservice.generic.converters;

import java.text.ParseException;
import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto) throws Exception;

    E toEntity(D dto, E entity) throws Exception;

    D toDto(E entity) throws ParseException;

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
