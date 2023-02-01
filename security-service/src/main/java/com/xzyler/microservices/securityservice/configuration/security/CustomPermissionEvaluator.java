package com.xzyler.microservices.securityservice.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom Permission Evaluator
 */
@Configuration
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final TokenStore tokenStore;
    //Super Admin Authority Name
    private final String superAdminAuthorityName = "SuperAdmin";
    private final HttpServletRequest httpServletRequest;

    public CustomPermissionEvaluator(TokenStore tokenStore, HttpServletRequest httpServletRequest) {
        this.tokenStore = tokenStore;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean hasPermission(
            Authentication auth, Object controllerName, Object permission) {
//        return true;
        if ((auth == null) || (controllerName == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, controllerName.toString(), permission.toString());
    }

    @Override
    public boolean hasPermission(
            Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, targetType.toUpperCase(),
                permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String controllerName, String permission) {
        System.out.println(auth);
        // If User Is Super Administrator
        if (isSuperAdministrator(auth)) return true;
        // Hash of requested permissions
        Set<String> permissions = new HashSet<>(Arrays.asList(permission.split(",")));
        // If permissions Contains "create || update || editapproved"
        if (permissions.contains("create") || permissions.contains("update") || permissions.contains("editapproved")) {
            permissions.add("create");
            permissions.add("update");
            permissions.add("editapproved");
        }

        // For Readonly Permissions query == view
        if (permissions.contains("query") || permissions.contains("view")) {
            permissions.add("query");
            permissions.add("view");
        }

        // Check Permission is Allowed in Granted Authorities
        for (String p : permissions) {
            //As Authority Are Assigned as ControllerName_PermissionName so
            String authority = controllerName + "_" + p;
            boolean hasAccess = auth.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(authority));
            if (hasAccess) return true;
            //Else Loop Will be continued
        }

        return false;
    }

    /**
     * Check User is Super Administrator or Not
     *
     * @param auth
     * @return
     */
    private boolean isSuperAdministrator(Authentication auth) {
        //return true;
        return auth.getAuthorities().stream().anyMatch(ga -> ga.getAuthority().equals(superAdminAuthorityName));
    }
}
