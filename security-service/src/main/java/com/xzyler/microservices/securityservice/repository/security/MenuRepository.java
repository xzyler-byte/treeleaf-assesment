package com.xzyler.microservices.securityservice.repository.security;

import com.xzyler.microservices.securityservice.entity.security.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query(value = "select id,\n" +
            "       icon_class as \"iconClass\",\n" +
            "       is_hidden as \"isHidden\",\n" +
            "       master_menu_id as \"masterMenuId\",\n" +
            "       menu_code as \"menuCode\",\n" +
            "       menu_name as \"menuName\",\n" +
            "       menu_name_local as \"menuNameLocal\",\n" +
            "       order_id as \"orderId\",\n" +
            "       url,\n" +
            "       created_date as \"entryDate\",\n" +
            "       created_by as \"entryUserId\",\n" +
            "       status,\n" +
            "       last_modified_date as \"statusChangeDate\",\n" +
            "       status_change_user_id as \"statusChangeUserId\"\n" +
            "       from Menu",nativeQuery = true)
    List<Map<String, Object>> findAllMenu();

    @Query(value = "select id,\n" +
            "       icon_class as \"iconClass\",\n" +
            "       is_hidden as \"isHidden\",\n" +
            "       master_menu_id as \"masterMenuId\",\n" +
            "       menu_code as \"menuCode\",\n" +
            "       menu_name as \"menuName\",\n" +
            "       menu_name_local as \"menuNameLocal\",\n" +
            "       order_id as \"orderId\",\n" +
            "       url,\n" +
            "       created_date as \"entryDate\",\n" +
            "       created_by as \"entryUserId\",\n" +
            "       status,\n" +
            "       last_modified_date as \"statusChangeDate\",\n" +
            "       status_change_user_id as \"statusChangeUserId\"\n" +
            "       from menu where code = ?1", nativeQuery = true)
    Map<String,Object> findByMenuCode(String menuCode);

    @Query(nativeQuery = true,
            value = "select * from get_menus_by_user(?1)")
    List<Map<String, Object>> findByUserId(Integer userId);


    @Query(nativeQuery = true,
            value = "select * from get_all_menus()")
    List<Map<String, Object>> getAllMenus();
}
