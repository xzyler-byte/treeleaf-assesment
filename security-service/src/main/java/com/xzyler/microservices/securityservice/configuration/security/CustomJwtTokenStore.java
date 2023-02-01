package com.xzyler.microservices.securityservice.configuration.security;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/***
 * Custom JWT Token Store
 */
public class CustomJwtTokenStore extends JwtTokenStore {

    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        super(jwtTokenEnhancer);
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication oAuth2Authentication = super.readAuthentication(token);
        oAuth2Authentication.setDetails(token.getAdditionalInformation());
        return oAuth2Authentication;
    }
}
