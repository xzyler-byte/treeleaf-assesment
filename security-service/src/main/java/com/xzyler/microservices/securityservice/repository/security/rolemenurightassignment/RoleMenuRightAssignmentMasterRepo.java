package com.xzyler.microservices.securityservice.repository.security.rolemenurightassignment;

import com.xzyler.microservices.securityservice.entity.security.rolerightassignment.RoleMenuRightAssignmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMenuRightAssignmentMasterRepo extends JpaRepository<RoleMenuRightAssignmentMaster, Integer> {

    @Query(value = "select u.id, \n" +
            "      CAST (u.created_date AS DATE) as entrydate,\n" +
            "      u.created_by as entryuserid,\n" +
            "      u.status as status,\n" +
            "      CAST (u.effect_date AS DATE) as effectdate,\n" +
            "      CAST (u.effect_till_date AS DATE) as effecttilldate,\n" +
            "      u2.is_update as isupdate,\n" +
            "      u2.is_query as isquery,\n" +
            "      u2.is_delete as isdelete,\n" +
            "      u2.is_create as iscreate,\n" +
            "      u2.is_approve as isapprove,\n" +
            "      u2.is_report as isreport,\n" +
            "      a.id as processId,\n" +
            "      a.menu_name as processName, \n" +
            "      a.menu_code as processCode, \n" +
            "      u3.id as roleId, \n" +
            "      u3.role_name as roleName,\n" +
            "from Role_Menu_Right_Assignment_Master u\n" +
            "            inner join Role_Menu_Right_Assignment_Detail u2 on u.id = u2.role_menu_right_assignment_master_id\n" +
            "            inner join Menu a on u2.menu_id = a.id\n" +
            "            inner join User_Role u3 on u.role_id = u3.id\n" +
            "            where u.id = ?1 order by u.created_date desc",nativeQuery = true)
    List<Map<String,Object>> findAllById(int id);

    @Query(value = "select uramarm.id as \"id\", u2.username as \"entryUserName\", uramarm.status,\n" +
            "       CAST (uramarm.created_date AS DATE) as \"entryDate\",\n" +
            "       CAST (uramarm.effect_till_date AS DATE) as \"effectTillDate\", CAST (uramarm.effect_date AS DATE) as \"effectDate\",\n" +
            "       u.id as \"roleId\", u.role_name as \"roleName\"\n" +
            "from Role_Menu_Right_Assignment_Master uramarm\n" +
            "             inner join User_Role u on uramarm.role_id = u.id\n" +
            "             join Users u2 on u2.id = uramarm.created_by \n" +
            "             where uramarm.status = ?1 order by uramarm.last_modified_date desc",nativeQuery = true)
    List<Map<String,Object>> findAllRoleRightsByStatus(Integer status);

    @Query(value = "select urrm.id from Role_Menu_Right_Assignment_Master urrm\n" +
            "            where urrm.role_id = ?1 and urrm.status = 1 order by urrm.last_created_date limit 1", nativeQuery = true)
    Integer getIdByRole(Integer roleId);
}
