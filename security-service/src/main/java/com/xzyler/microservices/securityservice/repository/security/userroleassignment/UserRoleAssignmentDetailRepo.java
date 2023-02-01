package com.xzyler.microservices.securityservice.repository.security.userroleassignment;

import com.xzyler.microservices.securityservice.entity.security.userroleassignment.UserRoleAssignmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleAssignmentDetailRepo extends JpaRepository<UserRoleAssignmentDetail, Integer> {
}
