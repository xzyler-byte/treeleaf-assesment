package com.xzyler.microservices.securityservice.service.security.impl;

import com.xzyler.microservices.securityservice.dto.security.RoleDto;
import com.xzyler.microservices.securityservice.entity.security.Role;
import com.xzyler.microservices.securityservice.enums.Status;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;
import com.xzyler.microservices.securityservice.repository.security.RoleRepository;
import com.xzyler.microservices.securityservice.service.security.RoleService;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import com.xzyler.microservices.securityservice.util.NullAwareBeanUtilsBean;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final CustomMessageSource customMessageSource;

    @Override
    public List<Role> fetchRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    @Override
    public Object fetchRole(Integer roleId) {
        Object role = roleRepository.findById(roleId);
        return role;
    }

    @Override
    @Transactional
    public void createRole(RoleDto roleDto) {
        Role role = Role.builder()
                .roleName(roleDto.getRoleName())
                .build();
        roleRepository.saveAndFlush(role);
    }

    @Override
    @Transactional
    public void updateRole(RoleDto roleDto) {

        BeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
        try {
            Role roleDB = roleRepository.findById(roleDto.getId()).orElseThrow(() -> new Exception("Role Not Found."));
            beanUtilsBean.copyProperties(roleDB, Role.builder()
                    .roleName(roleDto.getRoleName())
                    .build());

            roleRepository.saveAndFlush(roleDB);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteRole(Integer roleId) throws Exception {
        Role roleDB = roleRepository.findById(roleId).orElseThrow(() -> new Exception("Role Not found"));
        roleDB.setStatus(Status.INACTIVE);
        roleRepository.save(roleDB);
    }

    @Override
    public Role get(int roleId) throws NotFoundException {
        return roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                customMessageSource.get("role"))));
    }

    @Override
    public Integer changeStatus(Integer id, Integer status) throws Exception {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("role"))));
        role.setStatus(Status.getByKey(status));
        roleRepository.save(role);
        return id;
    }


}
