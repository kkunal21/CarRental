package com.carrental.repository;

import com.carrental.entity.Booking;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

}
