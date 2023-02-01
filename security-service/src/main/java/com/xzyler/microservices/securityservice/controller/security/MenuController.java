package com.xzyler.microservices.securityservice.controller.security;

import com.xzyler.microservices.securityservice.controller.BaseController;
import com.xzyler.microservices.securityservice.dto.security.MenuDto;
import com.xzyler.microservices.securityservice.service.security.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController extends BaseController {

    private final MenuService menuService;

    @PostMapping("create")
    //@PreAuthorize("hasPermission(#this.this.permissionName,'create')")
    public ResponseEntity<?> createApplicationMenu(@RequestBody @Valid MenuDto menuDto){
        menuService.create(menuDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.save", customMessageSource.get("menu")), null));
    }

    @PostMapping("update")
    //@PreAuthorize("hasPermission(#this.this.permissionName,'update')")
    public ResponseEntity<?> updateApplicationMenu(@RequestBody MenuDto menuDto, BindingResult result) throws Exception {
        menuService.update(menuDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update", customMessageSource.get("menu")), null));
    }

    @GetMapping("fetch")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> fetchApplicationMenus(){
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list", customMessageSource.get("menu")), menuService.fetch()));
    }

    @GetMapping("/get/{menuId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> fetchApplicationMenus(@PathVariable("menuId") Integer menuId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched", customMessageSource.get("menu")), menuService.get(menuId)));
    }

    @DeleteMapping("delete/{menuId}")
    //@PreAuthorize("hasPermission(#this.this.permissionName,'delete')")
    public ResponseEntity<?> deleteApplicationMenus(@PathVariable("menuId") Integer menuId) throws Exception {
        menuService.delete(menuId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.delete", customMessageSource.get("menu")), null));

    }

    @GetMapping("/get_by_code/{code}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> getByApplicationCode(@PathVariable("code") String code) throws Exception {
        try {
            return ResponseEntity.ok(successResponse(customMessageSource.get("fetched", customMessageSource.get("menu")),
                    menuService.getByApplicationCode(code)));
        } catch (Exception e) {
            return new ResponseEntity(errorResponse(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/user")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> getByUser(){
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list", customMessageSource.get("menu")),
                menuService.getByUserId()));
    }

}
