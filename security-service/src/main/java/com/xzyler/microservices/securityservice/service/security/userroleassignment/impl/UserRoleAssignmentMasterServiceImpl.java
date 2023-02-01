package com.xzyler.microservices.securityservice.service.security.userroleassignment.impl;

import com.xzyler.microservices.securityservice.dto.security.userroleassignment.UserRoleAssignmentDetailDto;
import com.xzyler.microservices.securityservice.dto.security.userroleassignment.UserRoleAssignmentMasterDto;
import com.xzyler.microservices.securityservice.entity.security.Role;
import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.entity.security.userroleassignment.UserRoleAssignmentDetail;
import com.xzyler.microservices.securityservice.entity.security.userroleassignment.UserRoleAssignmentMaster;
import com.xzyler.microservices.securityservice.enums.Status;
import com.xzyler.microservices.securityservice.exceptions.NotFoundException;
import com.xzyler.microservices.securityservice.repository.security.UserRepository;
import com.xzyler.microservices.securityservice.repository.security.userroleassignment.UserRoleAssignmentMasterRepo;
import com.xzyler.microservices.securityservice.service.security.RoleService;
import com.xzyler.microservices.securityservice.service.security.userroleassignment.UserRoleAssignmentDetailService;
import com.xzyler.microservices.securityservice.service.security.userroleassignment.UserRoleAssignmentMasterService;
import com.xzyler.microservices.securityservice.util.CustomMessageSource;
import com.xzyler.microservices.securityservice.util.NullAwareBeanUtilsBean;
import com.xzyler.microservices.securityservice.util.UserDataConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRoleAssignmentMasterServiceImpl implements UserRoleAssignmentMasterService {

    private final UserRepository userRepository;
    private final UserRoleAssignmentMasterRepo userRoleAssignmentMasterRepo;
    private final UserRoleAssignmentDetailService userRoleAssignmentDetailService;
    private final CustomMessageSource customMessageSource;
    private final RoleService roleService;
    private final UserDataConfig userDataConfig;
    SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional
    public UserRoleAssignmentMasterDto create(UserRoleAssignmentMasterDto userRoleAssignmentMasterDto) throws Exception {
        try {
            User user = userRepository.findById(userRoleAssignmentMasterDto.getUserId()).orElseThrow(() ->
                    new NotFoundException(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user"))));

            Status status = Status.getByKey(userRoleAssignmentMasterDto.getStatus());

            UserRoleAssignmentMaster userRoleAssignmentMaster = UserRoleAssignmentMaster.builder()
                    .userId(userRoleAssignmentMasterDto.getUserId())
                    .effectDate(userRoleAssignmentMasterDto.getEffectDate())
                    .build();
            if (userRoleAssignmentMasterDto.getEffectTillDate() != null ){
                userRoleAssignmentMaster.setEffectTillDate(date_formatter.parse(userRoleAssignmentMasterDto.getEffectTillDate()));
            }
            userRoleAssignmentMaster.setStatus(status);

            List<UserRoleAssignmentDetail> userRoleAssignmentDetails = new ArrayList<>();
            for (UserRoleAssignmentDetailDto userRoleAssignmentDetailDto : userRoleAssignmentMasterDto.getRight()) {
                Role role = roleService.get(userRoleAssignmentDetailDto.getRoleId());

                UserRoleAssignmentDetail userRoleAssignmentDetail = UserRoleAssignmentDetail.builder()
                        .role(role)
                        .build();

                userRoleAssignmentDetails.add(userRoleAssignmentDetail);
            }

            userRoleAssignmentMaster.setUserRoleAssignmentDetailList(userRoleAssignmentDetails);
            userRoleAssignmentMaster = userRoleAssignmentMasterRepo.save(userRoleAssignmentMaster);
            return UserRoleAssignmentMasterDto.builder()
                    .id(userRoleAssignmentMaster.getId())
                    .status(userRoleAssignmentMaster.getStatus().ordinal())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public UserRoleAssignmentMasterDto edit(UserRoleAssignmentMasterDto userRoleAssignmentMasterDto) throws Exception {
        try {
            UserRoleAssignmentMaster userRoleAssignmentMaster = userRoleAssignmentMasterRepo.findById(userRoleAssignmentMasterDto.getId())
                    .orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user.role.assignment"))));
            List<UserRoleAssignmentDetail> userRoleAssignmentDetailDB = new ArrayList<>(userRoleAssignmentMaster.getUserRoleAssignmentDetailList());

            User user = userRepository.findById(userRoleAssignmentMasterDto.getUserId()).orElseThrow(() ->
                    new NotFoundException(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user"))));

            Status status = Status.getByKey(userRoleAssignmentMasterDto.getStatus());

            List<UserRoleAssignmentDetail> userOfficeRoleAssignmentDetails = new ArrayList<>();
            for (UserRoleAssignmentDetailDto userRoleAssignmentDetailDto : userRoleAssignmentMasterDto.getRight()) {
                Role role = roleService.get(userRoleAssignmentDetailDto.getRoleId());

                UserRoleAssignmentDetail userRoleAssignmentDetail = UserRoleAssignmentDetail.builder()
                        .role(role)
                        .userRoleAssignmentMaster(userRoleAssignmentMaster)
                        .build();

                userOfficeRoleAssignmentDetails.add(userRoleAssignmentDetail);
            }
            userRoleAssignmentMaster.setUserRoleAssignmentDetailList(userOfficeRoleAssignmentDetails);

            BeanUtilsBean beanUtilsBean = new NullAwareBeanUtilsBean();
            userRoleAssignmentMaster.setUserId(userRoleAssignmentMasterDto.getUserId());
            userRoleAssignmentMaster.setEffectDate(userRoleAssignmentMasterDto.getEffectDate());
            if (userRoleAssignmentMasterDto.getEffectTillDate() != null ){
                userRoleAssignmentMaster.setEffectTillDate(date_formatter.parse(userRoleAssignmentMasterDto.getEffectTillDate()));
            }
            userRoleAssignmentMaster.setStatus(status);
            userRoleAssignmentMaster = userRoleAssignmentMasterRepo.save(userRoleAssignmentMaster);
            userRoleAssignmentDetailDB.forEach(urd -> userRoleAssignmentDetailService.delete(urd.getId()));
            return UserRoleAssignmentMasterDto.builder()
                    .id(userRoleAssignmentMaster.getId())
                    .status(userRoleAssignmentMaster.getStatus().ordinal())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Integer user_office_roleId) throws Exception {
        UserRoleAssignmentMaster userRoleAssignmentMaster = userRoleAssignmentMasterRepo.findById(user_office_roleId)
                .orElseThrow(() -> new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user.role.assignment"))));
        userRoleAssignmentMaster.setStatus(Status.INACTIVE);
        userRoleAssignmentMasterRepo.save(userRoleAssignmentMaster);
    }

    @Override
    public Object get(Integer userOfficeAssignmentId) throws Exception {
        List<Map<String,Object>> userOfficeAssignmentList = userRoleAssignmentMasterRepo.findByIdNative(userOfficeAssignmentId);
        return parse(userOfficeAssignmentList);
    }

    @Override
    public List<Integer> getRolesByUserId(Integer userId) {
        return userRoleAssignmentMasterRepo.findRolesByUserId(userId);
    }

    @Override
    public Object getByUserId(Integer userId) throws Exception {
        List<Map<String,Object>> userOfficeRoles = userRoleAssignmentMasterRepo.findByUserId(userId);
        return getUserNameFromUserIds(userOfficeRoles);
    }

    @Override
    public Object getByUser(Integer userId) throws Exception {
        Integer userOfficeRoleId = userRoleAssignmentMasterRepo.findUserRoleByUserId(userId);
        if (userOfficeRoleId == null){
            return null;
        };
        return get(userOfficeRoleId);
    }

    @Override
    public Object findAllUserRole() {
        List<Map<String,Object>> userOfficeRoles = new ArrayList<>();
        userOfficeRoles = userRoleAssignmentMasterRepo.findAllUserOfficeRole();
        return getUserNameFromUserIds(userOfficeRoles);
    }

    Map<String,Object> parse(List<Map<String,Object>> userOfficeRoleList) throws Exception {
        Map<String,Object> returnObject = new HashMap<>();
        List<Map<String,Object>> innerObject = new ArrayList<>();
        int index = 0;
        for (Map<String,Object> m : userOfficeRoleList){
            if (index == 0) {
                Integer userId = (Integer) m.get("userId");
                User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                        customMessageSource.get("User"))));
                String fullname = user.getFullName();
                returnObject.put("fullName",fullname);
                returnObject.put("userName", user.getUsername());
                returnObject.put("userId", userId);
                returnObject.put("effectDate", m.get("effectDate"));
                returnObject.put("effectTillDate", m.get("effectTillDate"));
                returnObject.put("status", m.get("status"));
                returnObject.put("id", m.get("id"));

                Map<String, Object> map = new HashMap<>();
                map.put("roleId", m.get("roleId"));
                map.put("roleName", m.get("roleName"));
                innerObject.add(map);
            }else {
                Map<String, Object> map = new HashMap<>();
                map.put("roleId", m.get("roleId"));
                map.put("roleName", m.get("roleName"));
                innerObject.add(map);
            }
        }
        returnObject.put("grid",innerObject);
        return returnObject;
    }

    private List<Map<String, Object>> getUserNameFromUserIds(List<Map<String, Object>> userOfficeRoles) {
        List<Map<String,Object>> returnObject = new ArrayList<>();
        userOfficeRoles.forEach( r -> {
            Map<String,Object> m = new HashMap<>(r);
            Integer userId = (Integer) r.get("userId");
            User user = null;
            try {
                user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(customMessageSource.get("error.doesn't.exist",
                        customMessageSource.get("User"))));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
            String userName = user.getUsername();
            m.put("userName", userName);
            returnObject.add(m);
        });
        return returnObject;
    }

    @Override
    public Boolean checkUserHasAdminRoleOrNot(Integer userId) {
        return userRoleAssignmentMasterRepo.checkUserHasAdminAccessOrNot(userId);
    }

    @Override
    public Boolean hasSuperRole(Integer userId) {
        return userRoleAssignmentMasterRepo.hasSuperRole(userId);
    }

    @Override
    public Integer changeStatus(Integer id, Integer status) throws Exception {
        UserRoleAssignmentMaster userRoleAssignmentMaster = userRoleAssignmentMasterRepo.findById(id)
                .orElseThrow(() -> new Exception(customMessageSource.get("error.doesn't.exist", customMessageSource.get("user.role.assignment"))));
        Status status1 = Status.getByKey(status);

        userRoleAssignmentMaster.setStatus(status1);
        userRoleAssignmentMasterRepo.save(userRoleAssignmentMaster);
        return id;
    }
}
