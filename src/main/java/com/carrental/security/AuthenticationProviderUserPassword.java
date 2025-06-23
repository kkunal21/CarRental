package com.carrental.security;

import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticationProviderUserPassword<B> implements HttpRequestAuthenticationProvider<B> {
    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {

        String username = authRequest.getIdentity();
        String secret = authRequest.getSecret();

        if (!username.equals("admin") || !secret.equals("admin@123")) {
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
        return AuthenticationResponse.success(authRequest.getIdentity());
    }

}
