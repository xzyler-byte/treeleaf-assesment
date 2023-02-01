package com.xzyler.microservices.securityservice.configuration.security;

import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AuthUserDetails implements UserDetails {
    private Integer userId;
    private String username;
    private String password;
    private boolean status;
    private List<GrantedAuthority> authorities;
    //private Collection<Role> roles;

    public AuthUserDetails(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus().equals(Status.ACTIVE);
        //this.roles = user.getRoles();
        this.authorities = getGrantedAuthorities(user);
    }

    /**
     * Prepares Users Granted Authorities List
     * It fetches the user assigned roles and authorities , and then prepares user assigned authorities
     *
     * @param user
     * @return
     */
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        //Get User Roles
        //Collection<Role> userRoles = user.getRoles();
        // Get User Authorities
        //Collection<Authority> userAuthorities = user.getAuthorities();
        //Set for Authorities Names
        Set<String> authorities = new HashSet<>();
        // Authorities from Roles
//        userRoles.forEach(userRole -> {
//            Collection<Authority> userRoleAuthorities = userRole.getAuthorities();
//            userRoleAuthorities.forEach(userRoleAuthority -> authorities.add(userRoleAuthority.getName()));
//        });
//        // Authorities from Authorities
//        userAuthorities.forEach(userAuthority -> authorities.add(userAuthority.getName()));

        // Create Granted Authority Instance and Add SimpleGrantedAuthority
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        authorities.forEach(authority -> {
            System.out.println(authority);
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        });
        // Return Granted Authority
        return grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

    /**
     * Returns User Roles
     *
     * @return
     */
    //public Collection<Role> getRoles() {
    //return roles;
    //}

    /**
     * Checks Current User has Particular Role or not
     *
     * @param roleName
     * @return
     */
    public boolean hasRole(String roleName) {
        return true;
        //return this.roles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    /**
     * Checks Current User has Particular Role or not
     *
     * @param roleId
     * @return
     */
    public boolean hasRole(Long roleId) {
        return true;
        //return this.roles.stream().anyMatch(role -> role.getId() == roleId);
    }

    /**
     * Check Current User has Particular Authority or not
     *
     * @param authorityName
     * @return
     */
    public boolean hasAuthority(String authorityName) {
        Set<String> userAuthorities = AuthorityUtils.authorityListToSet(authorities);
        return userAuthorities.stream().anyMatch(authority -> authority.equalsIgnoreCase(authorityName));
    }
}
