package com.carrental.security;

import com.carrental.entity.User;
import com.carrental.repository.UserRepository;
import com.carrental.service.BcryptPasswordService;
import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.Optional;

@Singleton
public class AuthenticationProviderUserPassword<B> implements HttpRequestAuthenticationProvider<B> {

    private final UserRepository userRepository;
    private final BcryptPasswordService passwordEncoder;

    @Inject
    public AuthenticationProviderUserPassword(UserRepository userRepository , BcryptPasswordService passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {

        String username = authRequest.getIdentity();
        String secret = authRequest.getSecret();

        Optional<User> optionalUser = userRepository.findByUserName(username);

        if(optionalUser.isEmpty()){
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
        User user = optionalUser.get();
        boolean valid  = passwordEncoder.matches(secret , user.getPassword());

        if(!valid){
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }

        return AuthenticationResponse.success(authRequest.getIdentity() , Collections.singleton(user.getRole()));

    }

}
