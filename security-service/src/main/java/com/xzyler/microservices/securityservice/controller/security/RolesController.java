package com.xzyler.microservices.securityservice.controller.security;

import com.xzyler.microservices.securityservice.controller.BaseController;
import com.xzyler.microservices.securityservice.dto.security.RoleDto;
import com.xzyler.microservices.securityservice.service.security.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/role")
@RequiredArgsConstructor
public class RolesController extends BaseController {

    private final RoleService roleService;

    @GetMapping("get_all")
    public ResponseEntity<?> getRole() throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list", customMessageSource.get("role")),
                roleService.fetchRoles()));
    }


    @GetMapping("get/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable("roleId") Integer roleId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched", customMessageSource.get("role")),
                roleService.fetchRole(roleId)));
    }


    @PostMapping("/save")
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleDto roleDto, BindingResult bindingResult) throws Exception {
        validateRequestBody(bindingResult);
        roleService.createRole(roleDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.save",
                customMessageSource.get("role")), null));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRole(@ModelAttribute @Valid RoleDto roleDto, BindingResult bindingResult) {
        validateRequestBody(bindingResult);
        roleService.updateRole(roleDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update",
                customMessageSource.get("role")), null));
    }

    @DeleteMapping("delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") Integer roleId) throws Exception {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.delete",
                customMessageSource.get("role")), null));
    }

    @PutMapping("/toggle/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") Integer status) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.status",
                customMessageSource.get("role")), roleService.changeStatus(id, status)));
    }
}
