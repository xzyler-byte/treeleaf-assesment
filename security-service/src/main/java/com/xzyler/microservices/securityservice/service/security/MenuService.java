package com.xzyler.microservices.securityservice.service.security;

import com.xzyler.microservices.securityservice.dto.security.MenuDto;
import com.xzyler.microservices.securityservice.entity.security.Menu;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;

public interface MenuService {

    Menu getById(Integer menuId) throws Exception;

    void create(MenuDto menuDto);

    List<Map<String, Object>> fetch();

    void update(MenuDto menuDto) throws Exception;

    Object get(Integer menuId) throws NotFoundException;

    void delete(Integer menuId) throws NotFoundException;

    Object getByApplicationCode(String menuCode) throws Exception;

    Object getByUserId();

    List<Map<String, Object>> getMenuChildrens(List<Map<String, Object>> menus, List<Map<String, Object>> parents);
}
