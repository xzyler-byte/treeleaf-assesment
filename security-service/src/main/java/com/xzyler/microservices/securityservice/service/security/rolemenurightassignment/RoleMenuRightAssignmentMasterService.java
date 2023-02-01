package com.xzyler.microservices.securityservice.service.security.rolemenurightassignment;

import com.xzyler.microservices.securityservice.dto.security.rolemenurightassignment.RoleMenuRightAssignmentMasterDto;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface RoleMenuRightAssignmentMasterService {
    @Transactional
    RoleMenuRightAssignmentMasterDto create(RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMasterDto) throws Exception;

    @Transactional
    RoleMenuRightAssignmentMasterDto update(RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMasterDto) throws Exception;

    void delete(Integer roleMenuRightMasterId) throws NotFoundException;

    void approve(Integer roleMenuRightMasterId) throws NotFoundException;

    Object fetch(Integer roleMenuRightMasterId) throws Exception;

    Object findAll();

    Object fetchAllIndex(Integer status);

    Object getByRoleId(Integer roleId) throws Exception;

    Integer changeStatus(Integer id, Integer status) throws Exception;
}
