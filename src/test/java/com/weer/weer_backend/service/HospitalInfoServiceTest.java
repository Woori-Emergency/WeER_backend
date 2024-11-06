package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.entity.AdmissionType;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.HospitalRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HospitalInfoServiceTest {

  @InjectMocks
  private HospitalInfoService hospitalInfoService;

  @Mock
  private HospitalRepository hospitalRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetRangeAllHospital() {
    Hospital hospital1 = Hospital.builder()
        .hospitalId(1L)
        .name("Hospital A")
        .latitude(37.7749)
        .longitude(-122.4194)
        .build();

    Hospital hospital2 = Hospital.builder()
        .hospitalId(2L)
        .name("Hospital B")
        .latitude(37.8044)
        .longitude(-122.2711)
        .build();

    List<Hospital> hospitals = Arrays.asList(hospital1, hospital2);

    AdmissionType admissionType1 = AdmissionType.builder()
        .availableBeds(5)
        .totalBeds(10)
        .build();

    when(hospitalRepository.findAll()).thenReturn(hospitals);
    List<HospitalRangeDto> result = hospitalInfoService.getRangeAllHospital(37.7749, -122.4194, 50);

    assertEquals(2, result.size());

    HospitalRangeDto hospitalRangeDto1 = result.get(0);
    assertEquals("Hospital A", hospitalRangeDto1.getHospitalName());
    assertEquals(37.7749, hospitalRangeDto1.getLatitude());
    assertEquals(-122.4194, hospitalRangeDto1.getLongitude());
    assertEquals(5, hospitalRangeDto1.getAvailableBeds());
    assertEquals(10, hospitalRangeDto1.getTotalBeds());

    HospitalRangeDto hospitalRangeDto2 = result.get(1);
    assertEquals("Hospital B", hospitalRangeDto2.getHospitalName());
    assertEquals(37.8044, hospitalRangeDto2.getLatitude());
    assertEquals(-122.2711, hospitalRangeDto2.getLongitude());
    assertEquals(5, hospitalRangeDto2.getAvailableBeds());
    assertEquals(10, hospitalRangeDto2.getTotalBeds());

    verify(hospitalRepository, times(1)).findAll();
  }

  @Test
  public void testGetRangeAllHospital_NoHospitalInRange() {
    Hospital hospital1 = Hospital.builder()
        .hospitalId(1L)
        .name("Hospital A")
        .latitude(40.7128)  // 뉴욕
        .longitude(-74.0060)
        .build();

    Hospital hospital2 = Hospital.builder()
        .hospitalId(2L)
        .name("Hospital B")
        .latitude(34.0522)  // LA
        .longitude(-118.2437)
        .build();

    List<Hospital> hospitals = Arrays.asList(hospital1, hospital2);

    // Mock 설정
    when(hospitalRepository.findAll()).thenReturn(hospitals);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> hospitalInfoService.getRangeAllHospital(37.7749, -122.4194, 50));
  }
}