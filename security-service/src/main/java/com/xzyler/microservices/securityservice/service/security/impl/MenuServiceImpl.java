package com.xzyler.microservices.securityservice.service.security.impl;

import com.xzyler.microservices.securityservice.dto.security.MenuDto;
import com.xzyler.microservices.securityservice.entity.security.Menu;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;
import com.xzyler.microservices.securityservice.repository.security.MenuRepository;
import com.xzyler.microservices.securityservice.service.security.MenuService;
import com.xzyler.microservices.securityservice.service.security.userroleassignment.UserRoleAssignmentMasterService;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import com.xzyler.microservices.securityservice.util.NullAwareBeanUtilsBean;
import com.xzyler.microservices.securityservice.util.UserDataConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final CustomMessageSource customMessageSource;
    private final UserDataConfig userDataConfig;
    private final UserRoleAssignmentMasterService userRoleAssignmentMasterService;

    @Override
    public Menu getById(Integer menuId) throws Exception {
        return menuRepository.findById(menuId).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                customMessageSource.get("menu"))));
    }

    @Override
    public void create(MenuDto menuDto) {
        Menu menu = Menu.builder()
                .masterMenuId(menuDto.getMasterMenuId())
                .menuCode(menuDto.getMenuCode())
                .iconClass(menuDto.getMenuIcon())
                .menuName(menuDto.getMenuName())
                .order(menuDto.getOrder())
                .url(menuDto.getUrl())
                .isHidden(menuDto.getIsHidden())
                .build();

        menuRepository.save(menu);
    }

    @Override
    public List<Map<String, Object>> fetch() {

        return menuRepository.findAllMenu();
    }

    @Override
    public void update(MenuDto menuDto) throws Exception {
        Menu menuDB = menuRepository.findById(menuDto.getId()).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                customMessageSource.get("menu"))));
        Menu menu = Menu.builder()
                .masterMenuId(menuDto.getMasterMenuId())
                .menuCode(menuDto.getMenuCode())
                .iconClass(menuDto.getMenuIcon())
                .menuName(menuDto.getMenuName())
                .order(menuDto.getOrder())
                .url(menuDto.getUrl())
                .isHidden(menuDto.getIsHidden())
                .build();

        try {
            BeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            beanUtilsBean.copyProperties(menuDB, menu);

            menuRepository.save(menuDB);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public Object get(Integer menuId) throws NotFoundException {
        return menuRepository.findById(menuId).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                customMessageSource.get("menu"))));
    }

    @Override
    public void delete(Integer menuId) throws NotFoundException {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                customMessageSource.get("menu"))));
        menuRepository.save(menu);
    }

    @Override
    public Object getByApplicationCode(String menuCode) throws Exception {
        Map<String, Object> menu = menuRepository.findByMenuCode(menuCode);

        if (menu.isEmpty()) {
            throw new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("menu")));
        }

        return menu;
    }

    @Override
    public Object getByUserId() {
        try {
            //Get User Id
            Integer userId = userDataConfig.getLoggedInUserId();
            List<Map<String, Object>> menus = new ArrayList<>();

            //Check Admin User Access
            if (userRoleAssignmentMasterService.hasSuperRole(userId)) {
                menus = menuRepository.getAllMenus();
            } else {
                menus = menuRepository.findByUserId(userId);
            }

            List<Map<String, Object>> returnMenus = new ArrayList<>();
            List<Map<String, Object>> parents = new ArrayList<>();
            if (menus.size() > 0) {
                menus.forEach(mnu -> {
                    Map<String, Object> menu = new ObjectMapper().convertValue(mnu, Map.class);
                    if (menu.containsKey("masterMenuId") && (menu.get("masterMenuId") == null || menu.get("masterMenuId").equals(0))) {
                        parents.add(menu);
                    }

                });
                returnMenus = getMenuChildrens(menus, parents);
            }
            return returnMenus;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getMenuChildrens(List<Map<String, Object>> menus, List<Map<String, Object>> parents) {
        //Loop Over Parents
        parents.forEach(parent -> {
            // Empty Children Array
            List<Map<String, Object>> childrens = new ArrayList<>();
            /*Map<String, Map<String, Object>> childrens = new HashMap<>();
            List<String> indexNames = new ArrayList<>();*/
            //Check ParentId over menus
            menus.forEach(mnu -> {
                Map<String, Object> menu = new ObjectMapper().convertValue(mnu, Map.class);
                Integer menuId = (Integer) parent.get("menuId");
                Integer masterId = ((Integer) menu.get("masterMenuId"));
                if (menuId.equals(masterId)) {
                    childrens.add(menu);
                }
            });
            parent.put("children", childrens);
            //Recursive Call
            getMenuChildrens(menus, childrens);
        });

        //Return Parents
        return parents;
    }

}
