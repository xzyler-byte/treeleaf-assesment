package com.xzyler.microservices.securityservice.controller.security;

import com.xzyler.microservices.securityservice.controller.BaseController;
import com.xzyler.microservices.securityservice.dto.security.userroleassignment.UserRoleAssignmentMasterDto;
import com.xzyler.microservices.securityservice.service.security.userroleassignment.UserRoleAssignmentMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user_role_assignment")
@RequiredArgsConstructor
public class UserRoleAssignmentController extends BaseController {

    private final UserRoleAssignmentMasterService userRoleAssignmentMasterService;

    @PostMapping("/create")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'create')")
    public ResponseEntity<?> createUserSectionAssignment(@Valid @RequestBody UserRoleAssignmentMasterDto userRoleAssignmentMasterDto) throws Exception {
        try {
            UserRoleAssignmentMasterDto userRoleAssignmentMaster = userRoleAssignmentMasterService.create(userRoleAssignmentMasterDto);
            return ResponseEntity.ok(successResponse(customMessageSource.get("success.save",
                    customMessageSource.get("user.section.assignment")), userRoleAssignmentMaster));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/edit")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'update')")
    public ResponseEntity<?> editUserSectionAssignment(@Valid @RequestBody UserRoleAssignmentMasterDto userRoleAssignmentMasterDto) throws Exception {
        UserRoleAssignmentMasterDto userRoleAssignmentMaster =  userRoleAssignmentMasterService.edit(userRoleAssignmentMasterDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update",
                customMessageSource.get("user.section.assignment")), userRoleAssignmentMaster));
    }

    @DeleteMapping("/delete/{userRoleAssignmentId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'delete')")
    public ResponseEntity<?> deleteUserSectionAssignment(@PathVariable("userRoleAssignmentId") Integer userRoleAssignmentId) throws Exception {
        userRoleAssignmentMasterService.delete(userRoleAssignmentId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.deleted", customMessageSource.get("user.section.assignment")), null));

    }

    @GetMapping("/get/{userRoleAssignmentId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
   public ResponseEntity<?> getUserSectionAssignment(@PathVariable("userRoleAssignmentId") Integer userRoleAssignmentId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched",customMessageSource.get("user.section.assignment")),
                userRoleAssignmentMasterService.get(userRoleAssignmentId)));
    }

    @GetMapping("/get/user/{userId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> getUserRoleAssignmentbyUserId(@PathVariable("userId") Integer userId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched", customMessageSource.get("user.section.assignment")),
                userRoleAssignmentMasterService.getByUser(userId)));
    }

    /*@GetMapping("/get/user/{userId}")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> getByUser(@PathVariable("userId") Integer userId, @PathVariable("sectionId") Integer sectionId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list",
                customMessageSource.get("user.section.assignment")), userRoleAssignmentMasterService.getByUserId(userId)));
    }*/

    @GetMapping("/fetch")
//    @PreAuthorize("hasPermission(#this.this.permissionName,'query')")
    public ResponseEntity<?> getUserSectionRoles() throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list",
                customMessageSource.get("user.section.assignment")), userRoleAssignmentMasterService.findAllUserRole()));
    }

    @PutMapping("/toggle/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") Integer status) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update",
                customMessageSource.get("user.section.assignment")), userRoleAssignmentMasterService.changeStatus(id, status)));
    }
}
