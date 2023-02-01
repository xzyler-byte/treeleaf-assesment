package com.xzyler.microservices.securityservice.service.security;

import com.xzyler.microservices.securityservice.dto.security.HelperDto;
import com.xzyler.microservices.securityservice.dto.security.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserService {

    @Transactional
    void createUser(UserDto userDto) throws Exception;

    @Transactional
    void updateUser(UserDto userDto);

    void deleteUser(Integer user_id) throws Exception;

    Map<String, Object> fetchUser(Integer user_Id) throws Exception;

    List<Map<String, Object>> fetchUsers() throws Exception;

    void resetReqPassword(HelperDto helperDto) throws Exception;

//    void approve(String email) throws Exception;

    void changePassword(HelperDto helperDto) throws Exception;

    void resetPasswordProcess(HelperDto helperDto) throws Exception;

    Integer changeStatus(Integer id, Integer status) throws Exception;

    List<Map<String, Object>> getUsersListByRole(Integer roleId) throws Exception;

    Map<String, Object> getUserProfile(Integer userId) throws Exception;
}
