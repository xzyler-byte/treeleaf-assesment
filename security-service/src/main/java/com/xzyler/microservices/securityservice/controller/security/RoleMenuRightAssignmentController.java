package com.xzyler.microservices.securityservice.controller.security;

import com.xzyler.microservices.securityservice.controller.BaseController;
import com.xzyler.microservices.securityservice.dto.security.rolemenurightassignment.RoleMenuRightAssignmentMasterDto;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;
import com.xzyler.microservices.securityservice.service.security.rolemenurightassignment.RoleMenuRightAssignmentMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/role_right")
@RequiredArgsConstructor
public class RoleMenuRightAssignmentController extends BaseController {

    private final RoleMenuRightAssignmentMasterService roleMenuRightAssignmentMasterService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasPermission(#this.this.permissionName,'create')")
    public ResponseEntity<?> createRoleRight(@RequestBody @Valid RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMasterDto) throws Exception {
        RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterService.create(roleMenuRightAssignmentMasterDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.save", customMessageSource.get("role.menu.assignment")),
                roleMenuRightAssignmentMaster));
    }

    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasPermission(#this.this.permissionName,'update')")
    public ResponseEntity<?> updateRoleRight(@RequestBody @Valid  RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMasterDto) throws Exception {
        RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterService.update(roleMenuRightAssignmentMasterDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update", customMessageSource.get("role.menu.assignment")),
                roleMenuRightAssignmentMaster));
    }

    @DeleteMapping("/delete/{roleRightMasterId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'delete')")
    public ResponseEntity<?> deleteRoleRight(@PathVariable("roleRightMasterId") Integer roleRightMasterId) throws NotFoundException {
        roleMenuRightAssignmentMasterService.delete(roleRightMasterId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.delete", customMessageSource.get("role.menu.assignment")),
                null));
    }

    @GetMapping("/approve/{roleRightMasterId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> approveRoleRight(@PathVariable("roleRightMasterId") Integer roleRightMasterId) throws NotFoundException {
        roleMenuRightAssignmentMasterService.approve(roleRightMasterId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.approve", customMessageSource.get("role.menu.assignment")),
                null));
    }

    @GetMapping("/get/{roleRightMasterId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> fetchRoleRight(@PathVariable("roleRightMasterId") Integer roleRightMasterId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched", customMessageSource.get("role.menu.assignment")),
                roleMenuRightAssignmentMasterService.fetch(roleRightMasterId)));
    }

    @GetMapping("/fetch_by_status/{status}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> fetchRoleRights(@PathVariable("status") Integer status) throws NotFoundException {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list",
                customMessageSource.get("role.menu.assignment")), roleMenuRightAssignmentMasterService.fetchAllIndex(status)));
    }

    @GetMapping("/fetch")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> fetch() throws NotFoundException {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list", customMessageSource.get("role.menu.assignment")),
                roleMenuRightAssignmentMasterService.findAll()));
    }

    @GetMapping("get/role/{roleId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> getByRole(@PathVariable("roleId") Integer roleId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list", customMessageSource.get("role.menu.assignment")),
                roleMenuRightAssignmentMasterService.getByRoleId(roleId)));
    }

    @PutMapping("/toggle/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") Integer status) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update", customMessageSource.get("role.menu.assignment"))
                , roleMenuRightAssignmentMasterService.changeStatus(id, status)));
    }
}
