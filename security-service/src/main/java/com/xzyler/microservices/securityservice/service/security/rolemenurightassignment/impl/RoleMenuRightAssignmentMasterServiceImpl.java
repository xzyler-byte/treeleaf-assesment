package com.xzyler.microservices.securityservice.service.security.rolemenurightassignment.impl;

import com.xzyler.microservices.securityservice.dto.security.rolemenurightassignment.RoleMenuRightAssignmentDetailDto;
import com.xzyler.microservices.securityservice.dto.security.rolemenurightassignment.RoleMenuRightAssignmentMasterDto;
import com.xzyler.microservices.securityservice.entity.security.Menu;
import com.xzyler.microservices.securityservice.entity.security.Role;
import com.xzyler.microservices.securityservice.entity.security.rolerightassignment.RoleMenuRightAssignmentDetail;
import com.xzyler.microservices.securityservice.entity.security.rolerightassignment.RoleMenuRightAssignmentMaster;
import com.xzyler.microservices.securityservice.enums.Status;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;
import com.xzyler.microservices.securityservice.repository.security.rolemenurightassignment.RoleMenuRightAssignmentMasterRepo;
import com.xzyler.microservices.securityservice.service.security.MenuService;
import com.xzyler.microservices.securityservice.service.security.RoleService;
import com.xzyler.microservices.securityservice.service.security.rolemenurightassignment.RoleMenuRightAssignmentDetailService;
import com.xzyler.microservices.securityservice.service.security.rolemenurightassignment.RoleMenuRightAssignmentMasterService;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleMenuRightAssignmentMasterServiceImpl implements RoleMenuRightAssignmentMasterService {

    private final RoleMenuRightAssignmentMasterRepo roleMenuRightAssignmentMasterRepo;
    private final CustomMessageSource customMessageSource;
    private final RoleService roleService;
    private final MenuService menuService;
    private final RoleMenuRightAssignmentDetailService roleMenuRightAssignmentDetailService;
    SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional
    public RoleMenuRightAssignmentMasterDto create(RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMasterDto) throws Exception {
        try {
            Role role = roleService.get(roleMenuRightAssignmentMasterDto.getRoleId());
            Status status = Status.getByKey(roleMenuRightAssignmentMasterDto.getStatus());

            RoleMenuRightAssignmentMaster roleMenuRightAssignmentMaster = RoleMenuRightAssignmentMaster.builder()
                    .effectTillDate(roleMenuRightAssignmentMasterDto.getEffectTillDate())
                    .role(role)
                    .effectDate(date_formatter.parse(roleMenuRightAssignmentMasterDto.getEffectDate()))
                    .build();
            roleMenuRightAssignmentMaster.setStatus(status);

            List<RoleMenuRightAssignmentDetail> roleMenuRightAssignmentDetails = new ArrayList<>();
            for (RoleMenuRightAssignmentDetailDto roleMenuRightAssignmentDetailDto : roleMenuRightAssignmentMasterDto.getRight()) {
                Menu menu = menuService.getById(roleMenuRightAssignmentDetailDto.getMenuId());
                RoleMenuRightAssignmentDetail roleMenuRightAssignmentDetail = RoleMenuRightAssignmentDetail.builder()
                        .create(roleMenuRightAssignmentDetailDto.getIsCreate())
                        .delete(roleMenuRightAssignmentDetailDto.getIsDelete())
                        .view(roleMenuRightAssignmentDetailDto.getIsView())
                        .update(roleMenuRightAssignmentDetailDto.getIsUpdate())
                        .approve(roleMenuRightAssignmentDetailDto.getIsApprove())
                        .report(roleMenuRightAssignmentDetailDto.getIsReport())
                        .menu(menu)
                        .build();
                roleMenuRightAssignmentDetails.add(roleMenuRightAssignmentDetail);
            }
            roleMenuRightAssignmentMaster.setRoleMenuRightAssignmentDetailList(roleMenuRightAssignmentDetails);
            roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterRepo.save(roleMenuRightAssignmentMaster);
            return RoleMenuRightAssignmentMasterDto.builder()
                    .id(roleMenuRightAssignmentMaster.getId())
                    .status(roleMenuRightAssignmentMaster.getStatus().ordinal())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public RoleMenuRightAssignmentMasterDto update(RoleMenuRightAssignmentMasterDto roleMenuRightAssignmentMasterDto) throws Exception {
        try {
            Role role = roleService.get(roleMenuRightAssignmentMasterDto.getRoleId());
            RoleMenuRightAssignmentMaster roleMenuRightAssignmentMasterDB = roleMenuRightAssignmentMasterRepo.findById(roleMenuRightAssignmentMasterDto.getId())
                    .orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist", "Role Menu Right Assignment")));

            Status status = Status.getByKey(roleMenuRightAssignmentMasterDto.getStatus());

            List<RoleMenuRightAssignmentDetail> roleMenuRightAssignmentDetailDB = new ArrayList<>(roleMenuRightAssignmentMasterDB.getRoleMenuRightAssignmentDetailList());

            List<RoleMenuRightAssignmentDetail> roleMenuRightAssignmentDetails = new ArrayList<>();
            for (RoleMenuRightAssignmentDetailDto roleMenuRightAssignmentDetailDto : roleMenuRightAssignmentMasterDto.getRight()) {
                Menu menu = menuService.getById(roleMenuRightAssignmentDetailDto.getMenuId());
                RoleMenuRightAssignmentDetail roleMenuRightAssignmentDetail = RoleMenuRightAssignmentDetail.builder()
                        .create(roleMenuRightAssignmentDetailDto.getIsCreate())
                        .delete(roleMenuRightAssignmentDetailDto.getIsDelete())
                        .view(roleMenuRightAssignmentDetailDto.getIsView())
                        .update(roleMenuRightAssignmentDetailDto.getIsUpdate())
                        .approve(roleMenuRightAssignmentDetailDto.getIsApprove())
                        .report(roleMenuRightAssignmentDetailDto.getIsReport())
                        .menu(menu)
                        .roleMenuRightAssignmentMaster(roleMenuRightAssignmentMasterDB)
                        .build();
                roleMenuRightAssignmentDetails.add(roleMenuRightAssignmentDetail);
            }
            roleMenuRightAssignmentMasterDB.setRoleMenuRightAssignmentDetailList(roleMenuRightAssignmentDetails);
            roleMenuRightAssignmentMasterDB.setEffectTillDate(roleMenuRightAssignmentMasterDto.getEffectTillDate());
            roleMenuRightAssignmentMasterDB.setRole(role);
            roleMenuRightAssignmentMasterDB.setEffectDate(date_formatter.parse(roleMenuRightAssignmentMasterDto.getEffectDate()));

            roleMenuRightAssignmentMasterDB.setStatus(status);
            roleMenuRightAssignmentMasterDB = roleMenuRightAssignmentMasterRepo.save(roleMenuRightAssignmentMasterDB);
            roleMenuRightAssignmentDetailDB.forEach(u -> roleMenuRightAssignmentDetailService.delete(u.getId()));
            return RoleMenuRightAssignmentMasterDto.builder()
                    .id(roleMenuRightAssignmentMasterDB.getId())
                    .status(roleMenuRightAssignmentMasterDB.getStatus().ordinal())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Integer roleMenuRightMasterId) throws NotFoundException {
        RoleMenuRightAssignmentMaster roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterRepo.findById(roleMenuRightMasterId)
                .orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist", "Role Menu Right Assignment")));
        roleMenuRightAssignmentMaster.setStatus(Status.INACTIVE);
        roleMenuRightAssignmentMasterRepo.save(roleMenuRightAssignmentMaster);
    }

    @Override
    public void approve(Integer roleMenuRightMasterId) throws NotFoundException {
        RoleMenuRightAssignmentMaster roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterRepo.findById(roleMenuRightMasterId)
                .orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist", "Role Menu Right Assignment")));
        roleMenuRightAssignmentMaster.setStatus(Status.ACTIVE);
        roleMenuRightAssignmentMasterRepo.save(roleMenuRightAssignmentMaster);
    }

    @Override
    public Object fetch(Integer roleMenuRightMasterId) throws Exception {
        try {
            List<Map<String, Object>> roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterRepo.findAllById(roleMenuRightMasterId);
            return parse(roleMenuRightAssignmentMaster);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Object findAll() {
        return roleMenuRightAssignmentMasterRepo.findAll();
    }

    @Override
    public Object fetchAllIndex(Integer status) {
        return roleMenuRightAssignmentMasterRepo.findAllRoleRightsByStatus(status);
    }

    @Override
    public Object getByRoleId(Integer roleId) throws Exception {
        try {
            Integer roleRightID = roleMenuRightAssignmentMasterRepo.getIdByRole(roleId);
            if (roleRightID == null) {
                return null;
            }
            return fetch(roleRightID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


    Map<String, Object> parse(List<Map<String, Object>> userRoleAppMenuActionRightsMaster) {
        Map<String, Object> returnObject = new HashMap<>();
        List<Map<String, Object>> innerObject = new ArrayList<>();

        int index = 0;
        for (Map<String, Object> m : userRoleAppMenuActionRightsMaster) {
            if (index == 0) {
                returnObject.put("id", m.get("id"));
                returnObject.put("effectDate", m.get("effectdate"));
                returnObject.put("effectTillDate", m.get("effecttilldate"));
                returnObject.put("roleId", m.get("roleId"));
                returnObject.put("roleName", m.get("roleName"));
                returnObject.put("status", m.get("status"));
                Map<String, Object> mapObject = new HashMap<>();

                mapObject.put("processId", m.get("processid"));
                mapObject.put("processName", m.get("processname"));
                mapObject.put("processCode", m.get("processCode"));
                mapObject.put("iscreate", m.get("iscreate"));
                mapObject.put("isupdate", m.get("isupdate"));
                mapObject.put("isdelete", m.get("isdelete"));
                mapObject.put("isview", m.get("isquery"));
                mapObject.put("isapprove", m.get("isapprove"));
                mapObject.put("isreport", m.get("isreport"));
                innerObject.add(mapObject);

                index++;
            } else {
                Map<String, Object> mapObject = new HashMap<>();
                //mapObject.put("status", m.get("status"));
                mapObject.put("processId", m.get("processid"));
                mapObject.put("processName", m.get("processname"));
                mapObject.put("processCode", m.get("processCode"));
                mapObject.put("iscreate", m.get("iscreate"));
                mapObject.put("isupdate", m.get("isupdate"));
                mapObject.put("isdelete", m.get("isdelete"));
                mapObject.put("isview", m.get("isquery"));
                mapObject.put("isapprove", m.get("isapprove"));
                mapObject.put("isreport", m.get("isreport"));
                innerObject.add(mapObject);
            }

        }

        returnObject.put("grid", innerObject);
        return returnObject;
    }

    @Override
    public Integer changeStatus(Integer id, Integer status) throws Exception {
        RoleMenuRightAssignmentMaster roleMenuRightAssignmentMaster = roleMenuRightAssignmentMasterRepo.findById(id)
                .orElseThrow(() -> new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("role.menu.assignment"))));
        roleMenuRightAssignmentMaster.setStatus(Status.getByKey(status));
        roleMenuRightAssignmentMasterRepo.save(roleMenuRightAssignmentMaster);
        return id;
    }
}
