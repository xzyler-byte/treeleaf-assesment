package com.xzyler.microservices.securityservice.repository.security.rolemenurightassignment;

import com.xzyler.microservices.securityservice.entity.security.rolerightassignment.RoleMenuRightAssignmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMenuRightAssignmentDetailRepo extends JpaRepository<RoleMenuRightAssignmentDetail, Integer> {
}
