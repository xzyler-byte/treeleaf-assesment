package com.xzyler.microservices.securityservice.configuration.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {

        final Map<String, Object> additionalInfo = new HashMap<String, Object>();
        additionalInfo.put("userId", ((AuthUserDetails) authentication.getPrincipal()).getUserId());
        ((DefaultOAuth2AccessToken) accessToken)
                .setAdditionalInformation(additionalInfo);
        return superEnhance(accessToken, authentication);
    }

    public OAuth2AccessToken superEnhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        accessToken = super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
        return accessToken;
    }
}
