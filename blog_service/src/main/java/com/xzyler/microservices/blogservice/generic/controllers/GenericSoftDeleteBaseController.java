package com.xzyler.microservices.blogservice.generic.controllers;

import com.xzyler.microservices.blogservice.dto.generics.ActiveToggle;
import com.xzyler.microservices.blogservice.generic.api.BaseEntity;
import com.xzyler.microservices.blogservice.generic.api.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;


public abstract class GenericSoftDeleteBaseController<T extends BaseEntity, ID extends Serializable> extends GenericCrudBaseController<T, ID> {

    @Autowired
    private GenericService<T, Long> genericService;

    @PreAuthorize("hasPermission(#this.this.permissionName,'update')")
    @PutMapping(value = "/toggle")
    public ResponseEntity<?> toggle(@RequestBody ActiveToggle data) {
        if (data.isStatus())
            genericService.activeById(data.getId());
        else
            genericService.deleteById(data.getId());
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(data.isStatus() ? "crud.active" : "crud.inactive", moduleName),
                        null)
        );
    }
}
