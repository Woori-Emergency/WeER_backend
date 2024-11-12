package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weer.weer_backend.dto.HospitalDTO;
import com.weer.weer_backend.dto.HospitalFilterDto;
import com.weer.weer_backend.dto.HospitalRangeDto;
import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.entity.Emergency;
import com.weer.weer_backend.entity.Equipment;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.entity.Icu;
import com.weer.weer_backend.repository.HospitalRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HospitalInfoServiceTest {

  @Mock
  private HospitalRepository hospitalRepository;

  @Mock
  private MapService mapService;

  @InjectMocks
  private HospitalInfoService hospitalInfoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFilterHospital() {
    // Arrange
    HospitalFilterDto filterDto = HospitalFilterDto.builder()
        .city("CityA")
        .state("StateA")
        .build();

    Equipment equipment = Equipment.builder()
        .equipmentId(1L)
        .hvventiAYN(true)
        .build();
    Emergency emergency = Emergency.builder()
        .emergencyId(1L)
        .build();

    Icu icu = Icu.builder()
        .icuId(1L)
        .hvcc(1)
        .build();

    Hospital hospital = Hospital.builder()
        .city("CityA")
        .state("StateA")
        .equipmentId(equipment)
        .emergencyId(emergency)
        .icuId(icu)
        .build();

    when(hospitalRepository.findByCityAndState("CityA", "StateA"))
        .thenReturn(Collections.singletonList(hospital));

    // Act
    List<HospitalDTO> result = hospitalInfoService.filterHospital(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(hospitalRepository, times(1)).findByCityAndState("CityA", "StateA");
  }

  @Test
  void testGetHospitalDetail() {
    // Arrange
    Equipment equipment = Equipment.builder()
        .equipmentId(1L)
        .hvventiAYN(true)
        .build();
    Emergency emergency = Emergency.builder()
        .emergencyId(1L)
        .build();
    Icu icu = Icu.builder()
        .icuId(1L)
        .hvcc(1)
        .build();
    Hospital hospital = Hospital.builder()
        .hospitalId(1L)
        .equipmentId(equipment)
        .emergencyId(emergency)
        .icuId(icu)
        .build();

    when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

    // Act
    HospitalDTO result = hospitalInfoService.getHospitalDetail(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getHospitalId());
    verify(hospitalRepository, times(1)).findById(1L);
  }

  @Test
  void testGetRangeAllHospital() {
    // Arrange
    Double latitude = 40.7128;
    Double longitude = -74.0060;
    int range = 10;

    Emergency emergency = Emergency.builder()
        .emergencyId(1L)
        .hvec(1)
        .hvs01(8)
        .build();

    Equipment equipment = Equipment.builder()
        .equipmentId(1L)
        .build();

    Icu icu = Icu.builder()
        .icuId(1L)
        .build();

    Hospital hospital = Hospital.builder()
        .latitude(40.7127)
        .longitude(-74.0059)
        .name("HospitalA")
        .emergencyId(emergency)
        .equipmentId(equipment)
        .icuId(icu)
        .build();

    MapInfoResponseDto mapInfo = MapInfoResponseDto.builder()
        .distance(5)
        .duration(10)
        .build();

    when(hospitalRepository.findAll()).thenReturn(Collections.singletonList(hospital));
    when(mapService.getMapInfo(latitude, longitude, hospital.getLatitude(), hospital.getLongitude()))
        .thenReturn(mapInfo);

    // Act
    List<HospitalRangeDto> result = hospitalInfoService.getRangeAllHospital(latitude, longitude, range);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("HospitalA", result.get(0).getHospitalName());
    verify(hospitalRepository, times(1)).findAll();
    verify(mapService, times(1)).getMapInfo(latitude, longitude, hospital.getLatitude(), hospital.getLongitude());
  }

  @Test
  void testFilterHospitalWithEmergencyAndIcuAndEquipmentFilters() {
    // Arrange
    HospitalFilterDto filterDto = HospitalFilterDto.builder()
        .city("CityA")
        .state("StateA")
        .hvec(false)
        .hvcc(true)
        .hvventiAYN(true)
        .build();

    Emergency emergency = Emergency.builder()
        .emergencyId(1L)
        .hvec(1)
        .hvs01(8)
        .build();

    Equipment equipment = Equipment.builder()
        .equipmentId(1L)
        .hvventiAYN(true)
        .build();

    Icu icu = Icu.builder()
        .icuId(1L)
        .hvcc(1)
        .build();

    Hospital hospital = Hospital.builder()
        .city("CityA")
        .state("StateA")
        .equipmentId(equipment)
        .emergencyId(emergency)
        .icuId(icu)
        .build();

    when(hospitalRepository.findByCityAndState("CityA", "StateA"))
        .thenReturn(Collections.singletonList(hospital));

    // Act
    List<HospitalDTO> result = hospitalInfoService.filterHospital(filterDto);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.stream().allMatch(h -> h.getCity().equals("CityA")));
    verify(hospitalRepository, times(1)).findByCityAndState("CityA", "StateA");
  }
}
