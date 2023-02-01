package com.xzyler.microservices.securityservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDataConfig {

    private final TokenStore tokenStore;
    private final CustomMessageSource customMessageSource;

    public Integer getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) return null;
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> details = tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        if(details != null)
            return (Integer) details.get("userId");
        return null;
    }

    public Map<String,Object> getLoggedInUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken) return null;
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> details = tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        if(details != null){
            Map<String,Object> loggedInUserInfo = new HashMap<>();
            loggedInUserInfo.put("userId",details.get("userId"));
            return loggedInUserInfo ;
        }
        return null;
    }

    /**
     * Get User Info from token By Key
     * @param authentication
     * @param key
     * @return
     */
    public <T> T  getLoggedInUserInfoByKey(Authentication authentication, String key) {
        if(authentication instanceof AnonymousAuthenticationToken) return null;
        OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> details = tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        return (T) details.get(key);
    }
}
