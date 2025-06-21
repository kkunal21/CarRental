package com.carrental.repository;

import com.carrental.entity.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
