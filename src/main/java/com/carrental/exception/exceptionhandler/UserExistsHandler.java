package com.carrental.exception.exceptionhandler;

import com.carrental.exception.UserAlreadyExistsException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
public class UserExistsHandler implements ExceptionHandler<UserAlreadyExistsException, HttpResponse<?>> {
    @Override
    public HttpResponse<?> handle(HttpRequest request, UserAlreadyExistsException exception) {
        JsonError error = new JsonError(exception.getMessage());
        return HttpResponse.badRequest(error);
    }
}
