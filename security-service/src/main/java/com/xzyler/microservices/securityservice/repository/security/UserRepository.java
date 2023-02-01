package com.xzyler.microservices.securityservice.repository.security;

import com.xzyler.microservices.securityservice.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select u.* from Users u where u.username = ?1 OR u.Email = ?1 AND u.status = 1", nativeQuery = true)
    Optional<User> findByUserNameOrEmail(String userName);

    @Query(value = "select * from Users where username = ?1", nativeQuery = true)
    Optional<User> findByUserName(String userName);

    @Query(value = "select * from get_users_list_by_status(?1)", nativeQuery = true)
    Page<Map<String, Object>> findAllByStatus(int status, PageRequest size);

    @Query(value = "select * from get_users_list_by_status(?1)", nativeQuery = true)
    List<Map<String, Object>> findAllUsersByStatus(int status);

    @Query(value = "select u.id as id, u.full_name as \"fullName\",\n" +
            "       u.username as \"userName\", u.email as \"email\", u.status as \"status\",\n" +
            "                   cast(u.created_date as DATE) as \"entryDate\", a.image as \"image\",\n" +
            "                   u.address as \"address\",\n" +
            "                    u.mobile_no as \"mobileNo\", u.gender as \"gender\"\n" +
            "            from Users u\n" +
            "            left join Application_User_Image a on u.id = a.user_id and a.id =\n" +
            "        (select a1.id from Application_User_Image a1 where a1.user_id = u.id order by a1.id desc limit 1)\n" +
            "            order by u.last_modified_date desc", nativeQuery = true)
    List<Map<String, Object>> findAll(PageRequest size);

    @Query(value = "select u.id as id, u.full_name as fullName, u.full_name_np as fullNameNp,\n" +
            "       u.username as username, u.email as email, u.status as status,\n" +
            "                   cast(u.created_date as DATE) as entryDate,\n" +
            "       a.image as image, u.user_type as userType, u.internal_user_id as internalUserId, \n" +
            "                   u.address as address, u.mobile_no as mobileNo\n" +
            "            from Users u\n" +
            "            left join Application_User_Image a on u.id = a.user_id and a.id =\n" +
            "                (select a1.id from Application_User_Image a1 where a1.user_id = u.id order by a1.id desc limit 1)\n" +
            "            order by u.last_modified_date desc", nativeQuery = true)
    List<Map<String, Object>> findAllUsers();

    @Query(value = "select u.id as id, u.full_name as fullName, \n" +
            "       u.full_name_np as fullNameNp,\n" +
            "       u.username as username,\n" +
            "       u.email as email, u.status as status,\n" +
            "                   cast(u.created_date as DATE ) as entryDate, a.image as image, u.user_type as userType,\n" +
            "       u.internal_user_id as internalUserId,\n" +
            "                   u.address as address, u.mobile_no as mobileNo\n" +
            "            from Users u\n" +
            "            inner join Application_User_Image a on u.id = a.user_id and a.id = \n" +
            "                        (select a1.id from Application_User_Image a1 where a1.user_id = u.id order by a1.id desc limit 1)\n" +
            "            where u.id in ?1\n" +
            "            order by u.id desc", nativeQuery = true)
    List<Map<String, Object>> findAllUserIndex(List<Integer> userIds);

    @Query(value = "select u.* from Users u where u.email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "select u.* from Users u where u.username = ?1 AND u.Email = ?2 AND u.Status = 1", nativeQuery = true)
    Optional<User> findByUserNameAndEmail(String userName, String email);

    @Query(value = "select u.id as id, u.full_name as \"fullName\", u.full_name_np as \"fullNameNp\",\n" +
            "       u.username as \"userName\", u.email as email, u.status as status,\n" +
            "                  cast(u.created_date as DATE ) as \"entryDate\",\n" +
            "       a.image as image,\n" +
            "                  u.mobile_no as \"mobileNo\", u.address as address,\n" +
            "       u.created_by as \"entryUserId\", u.gender as gender\n" +
            "           from Users u\n" +
            "           left join Application_User_Image a on u.id = a.user_id\n" +
            "           where u.id = ?1 order by a.id desc limit 1", nativeQuery = true)
    Map<String, Object> findUserById(int id);

    @Query(nativeQuery = true,
            value = "select password_change_date from User_Password_Hist where user_id = ?1 order by password_change_date desc limit 1")
    String findLastPasswordChangeDate(Integer userId);

    @Query(nativeQuery = true,
            value = "select * from Users where id in (?1)")
    List<User> getUsersById(List<Integer> userIds);

    @Query(value = "select image from Application_User_Image where user_id=?1", nativeQuery = true)
    List<String> getUserImagesById(Integer id);

    @Query(value = "select image from Application_User_Image where user_id=?1 order by id desc limit 1", nativeQuery = true)
    String getLatestUserImageByUserId(Integer userId);

    @Query(nativeQuery = true,
            value = "select u.id as \"id\", u.full_name as \"fullName\", u.username as \"username\", u.email as \"email\", u.status as \"status\",\n" +
                    "       cast(u.created_date as DATE ) as \"entryDate\", a.image as \"image\",\n" +
                    "       u.mobile_no as \"mobileNo\", u.address as \"address\", u.created_by as \"entryUserId\"\n" +
                    "from Users u\n" +
                    "left join Application_User_Image a on u.id = a.user_id and a.id = (select a1.id from Application_User_Image a1 where a1.user_id = u.id order by a1.id desc limit 1)\n" +
                    "where full_name like ?1 or username like ?1 or email like ?1 or mobile_no like ?1")
    List<Map<String, Object>> getUsersListByFilter(String filterText);

    @Query(nativeQuery = true,
            value = "select\n" +
                    "case\n" +
                    "    when cast(current_date as Date) - cast(?1 as date) > 10 then true\n" +
                    "    else false\n" +
                    "end as passwordexpired")
    Boolean checkWhetherPasswordIsExpiredOrNot(String lastPasswordChangeDate);

    @Query(nativeQuery = true, value = "select distinct uram.user_id as \"userId\", u.full_name as \"fullName\" from User_Role_Assignment_Detail urad\n" +
            "inner join User_Role_Assignment_Master uram on urad.user_role_assignment_master_id = uram.id\n" +
            "inner join Users u on u.id = uram.user_id\n" +
            "where urad.role_id = ?2")
    List<Map<String, Object>> getListOfUsersByRole(Integer roleId);
}
