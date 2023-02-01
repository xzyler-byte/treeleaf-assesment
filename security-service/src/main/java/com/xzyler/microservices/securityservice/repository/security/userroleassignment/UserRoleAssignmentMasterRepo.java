package com.xzyler.microservices.securityservice.repository.security.userroleassignment;

import com.xzyler.microservices.securityservice.entity.security.userroleassignment.UserRoleAssignmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRoleAssignmentMasterRepo extends JpaRepository<UserRoleAssignmentMaster, Integer> {

    @Query(value = "select u.id as \"id\",\n" +
            "       CAST(u.created_date AS DATE) as \"entryDate\",\n" +
            "       u.created_by as \"entryUserId\",\n" +
            "       u.status as \"status\",\n" +
            "       u.status_change_user_id as \"statusChangeUserId\",\n" +
            "       CAST (u.effect_date AS DATE) as \"effectDate\",\n" +
            "       CAST (u.effect_till_date AS DATE) as \"effectTillDate\",\n" +
            "       u.user_id as \"userId\",\n" +
            "       u3.id as \"roleId\",\n" +
            "       u3.role_name as \"roleName\",\n" +
            "from User_Role_Assignment_Master u\n" +
            "inner join User_Role_Assignment_Detail u2 on u.id = u2.user_role_assignment_master_id\n" +
            "inner join User_Role u3 on u2.role_id = u3.id\n" +
            " where u.id = ?1", nativeQuery = true)
    List<Map<String, Object>> findByIdNative(Integer userRoleAssignmentId);

    @Query(value = "select u2.role_id from User_Role_Assignment_Master u\n" +
            "inner join User_Role_Assignment_Detail u2 on u.id = u2.user_role_assignment_master_id\n" +
            "where u.user_id = ?1", nativeQuery = true)
    List<Integer> findRolesByUserId(Integer userId);

    @Query(value = "select u.id as \"id\", u.status as \"status\", u.user_id as \"userId\", u2.role_id as \"roleId\", u3.role_name as \"roleName\",\n" +
            "       CAST(u.effect_date AS DATE) as \"effectDate\", CAST(u.effect_till_date AS DATE) as \"effectTillDate\",\n" +
            "from User_Role_Assignment_Master u\n" +
            "inner join User_Role_Assignment_Detail u2 on u.id = u2.user_role_assignment_master_id\n" +
            "inner join User_Role u3 on u2.role_id = u3.id\n" +
            "where u.user_id = ?1", nativeQuery = true)
    List<Map<String,Object>> findByUserId(Integer userId);

    @Query(value = "select uora.id from User_Role_Assignment_Master uora\n" +
            "where uora.user_id = ?1 and uora.status=1 order by uora.created_date limit 1",nativeQuery = true)
    Integer findUserRoleByUserId(Integer userId);

    @Query(value = "select * from get_office_role_index()", nativeQuery = true)
    List<Map<String,Object>> findAllUserOfficeRole();

    @Query(nativeQuery = true,
            value = "select \n" +
                    "case when roledetail.role_id is null then false\n" +
                    "else true \n" +
                    "end as isAdmin \n" +
                    "from User_Role u\n" +
                    "           left join \n" +
                    "           (select u2.role_id from User_Role_Assignment_Master u\n" +
                    "            inner join User_Role_Assignment_Detail u2 on u.id = u2.user_role_assignment_master_id\n" +
                    "            where u.user_id = ?1) roledetail on u.id in (roledetail.role_id)\n" +
                    "            where u.role_name like '%Admin%'")
    Boolean checkUserHasAdminAccessOrNot(Integer userId);

    @Query(value = "select * from has_super_role(?1)", nativeQuery = true)
    Boolean hasSuperRole(Integer userId);
}
