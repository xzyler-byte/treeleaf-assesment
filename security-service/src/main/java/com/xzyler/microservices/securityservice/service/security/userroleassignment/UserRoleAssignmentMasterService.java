package com.xzyler.microservices.securityservice.service.security.userroleassignment;

import com.xzyler.microservices.securityservice.dto.security.userroleassignment.UserRoleAssignmentMasterDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleAssignmentMasterService {

    @Transactional
    UserRoleAssignmentMasterDto create(UserRoleAssignmentMasterDto userRoleAssignmentMasterDto) throws Exception;

    @Transactional
    UserRoleAssignmentMasterDto edit(UserRoleAssignmentMasterDto userRoleAssignmentMasterDto) throws Exception;

    void delete(Integer user_office_roleId) throws Exception;

    Object get(Integer userOfficeAssignmentId) throws Exception;

    List<Integer> getRolesByUserId(Integer userId);

    Object getByUserId(Integer userid) throws Exception;

    Object getByUser(Integer userId) throws Exception;

    Object findAllUserRole();

    Boolean checkUserHasAdminRoleOrNot(Integer userId);

    Boolean hasSuperRole(Integer userId);

    Integer changeStatus(Integer id, Integer status) throws Exception;
}
