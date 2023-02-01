package com.xzyler.microservices.blogservice.generic.controllers;

import com.xzyler.microservices.blogservice.generic.api.BaseEntity;
import com.xzyler.microservices.blogservice.generic.api.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;


public abstract class GenericCrudController<T extends BaseEntity, ID extends Serializable> extends GenericCrudBaseController<T, ID> {

    @Autowired
    private GenericService<T, ID> genericService;

    @PreAuthorize("hasPermission(#this.this.permissionName,'create')")
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody T entity) {
        T t = genericService.create(entity);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get("crud.create", customMessageSource.get(moduleName)),
                        t.getId())
        );
    }

    @PreAuthorize("hasPermission(#this.this.permissionName,'update')")
    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody T entity) {
        genericService.update(entity);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get("crud.update", customMessageSource.get(moduleName)),
                        null)
        );
    }

}
