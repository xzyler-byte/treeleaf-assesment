package com.xzyler.microservices.securityservice.service.security.rolemenurightassignment.impl;

import com.xzyler.microservices.securityservice.repository.security.rolemenurightassignment.RoleMenuRightAssignmentDetailRepo;
import com.xzyler.microservices.securityservice.service.security.rolemenurightassignment.RoleMenuRightAssignmentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleMenuRightAssignmentDetailServiceImpl implements RoleMenuRightAssignmentDetailService {

    private final RoleMenuRightAssignmentDetailRepo roleMenuRightAssignmentDetailRepo;

    @Override
    public void delete(Integer id) {
        roleMenuRightAssignmentDetailRepo.deleteById(id);
    }
}
