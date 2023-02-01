package com.xzyler.microservices.securityservice.service.security;

import com.xzyler.microservices.securityservice.dto.security.RoleDto;
import com.xzyler.microservices.securityservice.entity.security.Role;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;

import java.util.List;

public interface RoleService {

    void createRole(RoleDto roleDto) throws Exception;

    void updateRole(RoleDto roleDto);

    void deleteRole(Integer roleId) throws Exception;

    Object fetchRole(Integer roleId) throws Exception;

    List<Role> fetchRoles() throws Exception;

    Role get(int roleId) throws NotFoundException;

    Integer changeStatus(Integer id, Integer status) throws Exception;
}
