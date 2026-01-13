package com.property.tenantplatform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.property.tenantplatform.dto.BookingDecisionRequest;
import com.property.tenantplatform.dto.BookingRequest;
import com.property.tenantplatform.exception.InvalidInputException;
import com.property.tenantplatform.exception.ResourceNotFoundException;
import com.property.tenantplatform.model.Booking;
import com.property.tenantplatform.model.BookingStatus;
import com.property.tenantplatform.model.Property;
import com.property.tenantplatform.model.Tenant;
import com.property.tenantplatform.repository.BookingRepository;
import com.property.tenantplatform.repository.PropertyRepository;

@Service
public class BookingService {

  private final BookingRepository bookingRepository;
  private final PropertyRepository propertyRepository;
  private final TenantService tenantService;

  public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository,
                        TenantService tenantService) {
    this.bookingRepository = bookingRepository;
    this.propertyRepository = propertyRepository;
    this.tenantService = tenantService;
  }

  @Transactional
  public Booking create(Long userId, BookingRequest request) {
    Tenant tenant = tenantService.findByUserId(userId);
    Property property = propertyRepository.findById(request.getPropertyId())
        .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

    if (!tenant.getProperty().getId().equals(property.getId())) {
      throw new InvalidInputException("Tenant can only book assigned property");
    }

    if (request.getEndDate().isBefore(request.getStartDate())) {
      throw new InvalidInputException("End date must be after start date");
    }

    List<Booking> overlaps = bookingRepository.findOverlappingBookings(property.getId(),
        request.getStartDate(), request.getEndDate(),
        List.of(BookingStatus.PENDING, BookingStatus.APPROVED));
    if (!overlaps.isEmpty()) {
      throw new InvalidInputException("Property already booked for selected dates");
    }

    Booking booking = new Booking();
    booking.setTenant(tenant);
    booking.setProperty(property);
    booking.setStartDate(request.getStartDate());
    booking.setEndDate(request.getEndDate());
    booking.setStatus(BookingStatus.PENDING);
    return bookingRepository.save(booking);
  }

  @Transactional
  public Booking decide(Long bookingId, BookingDecisionRequest request) {
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    if (booking.getStatus() == BookingStatus.REJECTED || booking.getStatus() == BookingStatus.APPROVED) {
      throw new InvalidInputException("Booking already processed");
    }
    booking.setStatus(request.getStatus());
    return bookingRepository.save(booking);
  }

  public List<Booking> findAll() {
    return bookingRepository.findAll();
  }

  public List<Booking> findForTenant(Long userId) {
    Tenant tenant = tenantService.findByUserId(userId);
    return tenant.getId() == null ? List.of() : bookingRepository.findAll().stream()
        .filter(b -> b.getTenant().getId().equals(tenant.getId()))
        .toList();
  }
}
