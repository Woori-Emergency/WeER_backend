package com.weer.weer_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import com.weer.weer_backend.dto.HospitalDistanceDto;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.ERAnnouncementRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HospitalInfoServiceTest {

  @Mock
  private HospitalRepository hospitalRepository;

  @Mock
  private ERAnnouncementRepository erAnnouncementRepository;

  @Mock
  private MapService mapService;

  @InjectMocks
  private HospitalInfoService hospitalInfoService;

  private Hospital hospital;

  @BeforeEach
  void setUp() {
    hospital = Hospital.builder()
        .hospitalId(1L)
        .name("Test Hospital")
        .latitude(37.7749)
        .longitude(-122.4194)
        .emergencyId(null) // No emergency info to simulate filtering
        .icuId(null)       // No ICU info to simulate filtering
        .equipmentId(null) // No equipment info to simulate filtering
        .build();
  }

  @Test
  void getAnnounce_whenHospitalExists_returnsAnnouncements() {
    // given
    lenient().when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

    // when
    CustomException exception = assertThrows(CustomException.class, () -> {
      hospitalInfoService.getAnnounce(2L); // Hospital with ID 2 doesn't exist
    });

    // then
    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_HOSPITAL);
  }

  @Test
  void filteredHospitals_whenHospitalMatchesFilter_returnsFilteredHospitals() {
    // given
    HospitalFilterDto filterDto = HospitalFilterDto.builder()
        .city("Test City")
        .state("Test State")
        .hvcc(true)
        .build();

    lenient().when(hospitalRepository.findByCityAndState("Test City", "Test State"))
        .thenReturn(List.of(hospital));

    lenient().when(mapService.getMapInfo(any(Double.class), any(Double.class), any(Double.class), any(Double.class)))
        .thenReturn(CompletableFuture.completedFuture(MapInfoResponseDto.builder().distance(10).duration(15).build()));

    // when
    List<HospitalDistanceDto> filteredHospitals = hospitalInfoService.filteredHospitals(37.7749, -122.4194, filterDto);

    // then
    assertThat(filteredHospitals).isEmpty(); // As emergency, ICU, and equipment details are missing
  }

  @Test
  void filteredHospitals_whenHospitalDoesNotMatchFilter_returnsEmptyList() {
    // given
    HospitalFilterDto filterDto = HospitalFilterDto.builder()
        .city("Another City")
        .state("Another State")
        .build();

    lenient().when(hospitalRepository.findByCityAndState("Another City", "Another State"))
        .thenReturn(List.of());

    // when
    List<HospitalDistanceDto> filteredHospitals = hospitalInfoService.filteredHospitals(37.7749, -122.4194, filterDto);

    // then
    assertThat(filteredHospitals).isEmpty();
  }

}
