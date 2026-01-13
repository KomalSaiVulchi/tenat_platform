package com.property.tenantplatform.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.property.tenantplatform.model.Booking;
import com.property.tenantplatform.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId "
      + "AND b.status IN (:activeStatuses) "
      + "AND (b.startDate <= :endDate AND b.endDate >= :startDate)")
  List<Booking> findOverlappingBookings(@Param("propertyId") Long propertyId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("activeStatuses") List<BookingStatus> activeStatuses);
}
