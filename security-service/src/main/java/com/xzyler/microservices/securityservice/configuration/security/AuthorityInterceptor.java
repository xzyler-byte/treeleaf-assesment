package com.xzyler.microservices.securityservice.configuration.security;

import com.xzyler.microservices.securityservice.repository.security.MenuRepository;
import com.xzyler.microservices.securityservice.repository.security.userroleassignment.UserRoleAssignmentMasterRepo;
import com.xzyler.microservices.securityservice.util.UserDataConfig;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Authority Interceptor
 * It Sets the User Authorities Dynamically
 */
@Component
@Lazy
public class AuthorityInterceptor implements HandlerInterceptor {

    private final MenuRepository menuRepository;
    private final UserDataConfig userDataConfig;
    private final UserRoleAssignmentMasterRepo userRoleAssignmentMasterRepo;

    //Super Admin Authority Name
    private final String superAdminAuthorityName = "SuperAdmin";

    AuthorityInterceptor(@Lazy MenuRepository menuRepository,
                         @Lazy UserDataConfig userDataConfig, @Lazy UserRoleAssignmentMasterRepo userRoleAssignmentMasterRepo) {
        this.menuRepository = menuRepository;
        this.userDataConfig = userDataConfig;
        this.userRoleAssignmentMasterRepo = userRoleAssignmentMasterRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        if (auth.isAuthenticated()) {
            Integer userId = userDataConfig.getLoggedInUserId();
            if (userId != null) {
                // Check User Is Super Admin Or Not
                Boolean isSuperAdmin = userRoleAssignmentMasterRepo.hasSuperRole(userId);
                if (isSuperAdmin) {
                    authorities.add(new SimpleGrantedAuthority(superAdminAuthorityName));
                } else {
                    //Fetch Assigned Menus Access
                    List<Map<String, Object>> menus = menuRepository.findByUserId(userId);
                    menus.forEach(m -> {
                        if (m.get("controllerName") != null) {
                            //Check Roles Contain SuperUser Role or not
                            if (m.get("roleIds") != null) {
                                String roleIds = String.valueOf(String.valueOf(m.get("roleIds")));
                                List<String> rolesIdsList = Arrays.asList(roleIds.split(","));
                                // Check Contains Role Id 1 => SuperAdmin
                                if (rolesIdsList.contains("1")) {
                                    authorities.add(new SimpleGrantedAuthority(superAdminAuthorityName));
                                }
                            }
                            // Add Authorities in RUN TIME
                            if (Boolean.parseBoolean(String.valueOf(m.get("isCreate")))) {
                                authorities.add(new SimpleGrantedAuthority(m.get("controllerName") + "_" + "create"));
                            }
                            if (Boolean.parseBoolean(String.valueOf(m.get("isView")))) {
                                authorities.add(new SimpleGrantedAuthority(m.get("controllerName") + "_" + "query"));
                            }
                            if (Boolean.parseBoolean(String.valueOf(m.get("isUpdate")))) {
                                authorities.add(new SimpleGrantedAuthority(m.get("controllerName") + "_" + "update"));
                            }
                            if (Boolean.parseBoolean(String.valueOf(m.get("isDelete")))) {
                                authorities.add(new SimpleGrantedAuthority(m.get("controllerName") + "_" + "delete"));
                            }
                            if (Boolean.parseBoolean(String.valueOf(m.get("isApprove")))) {
                                authorities.add(new SimpleGrantedAuthority(m.get("controllerName") + "_" + "approve"));
                            }
                        }
                    });
                }
                //Custom Auth Provider
                CustomAuthProvider refinedAuth = new CustomAuthProvider(authorities, auth);
                refinedAuth.setDetails(auth.getDetails());
                SecurityContextHolder.getContext().setAuthentication(refinedAuth);
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
