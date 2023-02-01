package com.xzyler.microservices.securityservice.configuration.security;

import com.xzyler.microservices.securityservice.entity.security.User;
import com.xzyler.microservices.securityservice.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final HttpServletRequest httpServletRequest;

    @Override
    public AuthUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameOrEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username or Password."));
        String inputPassword = httpServletRequest.getParameter("password");
        if (!DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder().matches(inputPassword, user.getPassword()))
            throw new BadClientCredentialsException();
        return new AuthUserDetails(user);
    }
}
