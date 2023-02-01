package com.xzyler.microservices.securityservice.configuration.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom Auth Provider
 */
public class CustomAuthProvider extends AbstractAuthenticationToken {
    private Authentication authentication;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomAuthProvider(Collection<? extends GrantedAuthority> authorities, Authentication authentication) {
        super(authorities);
        this.authentication = authentication;
        this.authorities = authorities;
    }

    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    public boolean isAuthenticated() {
        if (this.authentication == null && this.authentication.isAuthenticated()){
            return true;
        }
        return true;
    }
}
