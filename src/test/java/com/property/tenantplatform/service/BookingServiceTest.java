package com.property.tenantplatform.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.property.tenantplatform.dto.BookingRequest;
import com.property.tenantplatform.exception.InvalidInputException;
import com.property.tenantplatform.model.Booking;
import com.property.tenantplatform.model.BookingStatus;
import com.property.tenantplatform.model.Property;
import com.property.tenantplatform.model.Tenant;
import com.property.tenantplatform.repository.BookingRepository;
import com.property.tenantplatform.repository.PropertyRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceTest {

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private PropertyRepository propertyRepository;

  @Mock
  private TenantService tenantService;

  @InjectMocks
  private BookingService bookingService;

  private Tenant tenant;
  private Property property;

  @BeforeEach
  void setup() {
    property = new Property(1L, "Test", "Addr", "Owner");
    tenant = new Tenant();
    tenant.setId(10L);
    tenant.setProperty(property);
  }

  @Test
  void shouldRejectOverlappingBooking() {
    BookingRequest request = new BookingRequest();
    request.setPropertyId(1L);
    request.setStartDate(LocalDate.now());
    request.setEndDate(LocalDate.now().plusDays(2));

    when(tenantService.findByUserId(anyLong())).thenReturn(tenant);
    when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
    when(bookingRepository.findOverlappingBookings(eq(1L), any(LocalDate.class), any(LocalDate.class), any()))
      .thenReturn(List.of(new Booking(null, tenant, property, request.getStartDate(), request.getEndDate(),
        BookingStatus.APPROVED)));

    assertThatThrownBy(() -> bookingService.create(5L, request))
        .isInstanceOf(InvalidInputException.class)
        .hasMessageContaining("already booked");
  }
}
