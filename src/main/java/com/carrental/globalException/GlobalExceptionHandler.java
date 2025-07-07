package com.carrental.globalException;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;


@Singleton
public class GlobalExceptionHandler {

    @Singleton
    public static class EmptyUserHandler implements ExceptionHandler<EmptyUsernameOrPasswordException , HttpResponse<?>> {
        @Override
        public HttpResponse<?> handle(HttpRequest request, EmptyUsernameOrPasswordException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.badRequest(error);
        }
    }

    @Singleton
    public static class UserExistsHandler implements ExceptionHandler<UserAlreadyExistsException , HttpResponse<?>>{
        @Override
        public HttpResponse<?> handle(HttpRequest request, UserAlreadyExistsException exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.badRequest(error);
        }
    }
    @Singleton
    public static class CannotReturnCarHandler implements ExceptionHandler<CannotReturnCarWithoutBooking , HttpResponse<?>>{

        @Override
        public HttpResponse<?> handle(HttpRequest request, CannotReturnCarWithoutBooking exception) {
            JsonError error = new JsonError(exception.getMessage());
            return HttpResponse.badRequest(error);
        }
    }


    }

