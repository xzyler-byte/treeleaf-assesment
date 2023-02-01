package com.xzyler.microservices.securityservice.service.security.userroleassignment.impl;

import com.xzyler.microservices.securityservice.repository.security.userroleassignment.UserRoleAssignmentDetailRepo;
import com.xzyler.microservices.securityservice.service.security.userroleassignment.UserRoleAssignmentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleAssignmentDetailServiceImpl implements UserRoleAssignmentDetailService {

    private final UserRoleAssignmentDetailRepo userRoleAssignmentDetailRepo;

    @Override
    public void delete(Integer id) {
        userRoleAssignmentDetailRepo.deleteById(id);
    }
}
