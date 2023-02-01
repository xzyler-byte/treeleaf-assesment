package com.xzyler.microservices.securityservice.controller.security;

import com.xzyler.microservices.securityservice.controller.BaseController;
import com.xzyler.microservices.securityservice.dto.security.HelperDto;
import com.xzyler.microservices.securityservice.dto.security.UserDto;
import com.xzyler.microservices.securityservice.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/get_all")
    @PreAuthorize("{hasPermission(#this.this.permissionName,'get')}")
    public ResponseEntity<?> getUsers() throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched.list",
                customMessageSource.get("user")), userService.fetchUsers()));
    }


    @GetMapping("/get/{userId}")
    @PreAuthorize("{hasPermission(#this.this.permissionName,'get')}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Integer userId) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("fetched",
                customMessageSource.get("user")), userService.fetchUser(userId)));
    }


    @PostMapping("/save")
    @PreAuthorize("{hasPermission(#this.this.permissionName,'post')}")
    public ResponseEntity<?> createUser(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult) throws Exception {
        validateRequestBody(bindingResult);
        userService.createUser(userDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.save",
                customMessageSource.get("user")), null));
    }

    @PutMapping("/update")
    @PreAuthorize("{hasPermission(#this.this.permissionName,'put')}")
    public ResponseEntity<?> updateUser(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult) {
//        validateRequestBody(bindingResult);
        userService.updateUser(userDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update",
                customMessageSource.get("user")), null));
    }

    @DeleteMapping("delete/{userId}")
    @PreAuthorize("{hasPermission(#this.this.permissionName,'delete')}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) throws Exception {
        userService.deleteUser(userId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.delete",
                customMessageSource.get("user")), null));
    }

    /**
     * Function to change user password
     * @param helperDto
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/change_password")
    @PreAuthorize("{hasPermission(#this.this.permissionName,'put')}")
    public ResponseEntity<?> changePassword(@RequestBody HelperDto helperDto) throws Exception {
        userService.changePassword(helperDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.update",
                customMessageSource.get("password")), null));
    }

    @PostMapping("/accounts/password/reset/confirm")
    public ResponseEntity<?> resetPassword(@RequestBody HelperDto helperDto) throws Exception {
        userService.resetPasswordProcess(helperDto);
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.change",
                customMessageSource.get("password")),null));
    }

    @PutMapping("/toggle/{id}/{status}")
    public ResponseEntity<?> changeStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") Integer status) throws Exception {
        return ResponseEntity.ok(successResponse(customMessageSource.get("success.status",
                customMessageSource.get("user")), userService.changeStatus(id, status)));
    }

    @PostMapping("/reset_req")
    public ResponseEntity<?> resetReqPassword(@RequestBody HelperDto helperDto) throws Exception {
        try {
            userService.resetReqPassword(helperDto);
            return ResponseEntity.ok(successResponse(customMessageSource.get("mail.exists.send.mail"), null));
        } catch (Exception e) {
            return ResponseEntity.ok(errorResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/role/{roleId}")
    public ResponseEntity<?> getUsersByRole(@PathVariable("roleId") Integer roleId) throws Exception {
        return ResponseEntity.ok(successResponse("User Fetched Successfully.",
                userService.getUsersListByRole(roleId)));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfileData(@PathVariable("userId") Integer userId) throws Exception {
        return ResponseEntity.ok(successResponse("User Fetched Successfully.",
                userService.getUserProfile(userId)));
    }

}
