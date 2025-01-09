package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.MapInfoResponseDto;
import com.weer.weer_backend.dto.RouteDto;
import com.weer.weer_backend.dto.RouteResponseDto;
import com.weer.weer_backend.dto.SummaryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MapServiceTest {

  @Mock
  private MapInterface mapInterface;

  @InjectMocks
  private MapService mapService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(mapService, "apiKey", "testApiKey");
  }



  @Test
  void testGetMapInfo_Success() {
    // given
    double originLat = 37.5665;
    double originLon = 126.9780;
    double destLat = 35.1796;
    double destLon = 129.0756;
    String origin = originLon + "," + originLat;
    String dest = destLon + "," + destLat;

    SummaryDto summaryDto = SummaryDto.builder()
        .distance(1000)
        .duration(600)
        .build();
    RouteDto routeDto = RouteDto.builder()
        .summary(summaryDto)
        .build();
    LinkedList<RouteDto> routes = new LinkedList<>();
    routes.add(routeDto);

    RouteResponseDto routeResponseDto = RouteResponseDto.builder()
        .routes(routes)
        .build();

    given(mapInterface.getMapInfo(origin, dest, "testApiKey")).willReturn(routeResponseDto);

    // when
    CompletableFuture<MapInfoResponseDto> result = mapService.getMapInfo(originLat, originLon, destLat, destLon);

    // then
    assertNotNull(result);
    assertFalse(result.isCompletedExceptionally());
    assertDoesNotThrow(() -> {
      MapInfoResponseDto mapInfo = result.join();
      assertNotNull(mapInfo);
      assertEquals(1000, mapInfo.getDistance());
      assertEquals(600, mapInfo.getDuration());
    });
    verify(mapInterface, times(1)).getMapInfo(origin, dest, "testApiKey");
  }

  @Test
  void testGetMapInfo_NoSummary() {
    // given
    double originLat = 37.5665;
    double originLon = 126.9780;
    double destLat = 35.1796;
    double destLon = 129.0756;
    String origin = originLon + "," + originLat;
    String dest = destLon + "," + destLat;

    RouteDto routeDto = RouteDto.builder()
        .summary(null)
        .build();
    LinkedList<RouteDto> routes = new LinkedList<>();
    routes.add(routeDto);

    RouteResponseDto routeResponseDto = RouteResponseDto.builder()
        .routes(routes)
        .build();

    given(mapInterface.getMapInfo(origin, dest, "testApiKey")).willReturn(routeResponseDto);

    // when

    // then
    assertEquals(null, mapService.getMapInfo(originLat, originLon, destLat, destLon));

  }
}
