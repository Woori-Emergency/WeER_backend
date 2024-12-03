package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonApiService {

    private final SystemMetricsAutoConfiguration systemMetricsAutoConfiguration;
    @Value("${OPENAPI_SERVICE_KEY}")
    private String SERVICE_KEY;

    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher eventPublisher;

        private Map<String, String> districtApiResponses = new HashMap<>();

    @Autowired
    public CommonApiService(RestTemplate restTemplate, ApplicationEventPublisher eventPublisher, SystemMetricsAutoConfiguration systemMetricsAutoConfiguration) {
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
        this.systemMetricsAutoConfiguration = systemMetricsAutoConfiguration;
    }

    //@Scheduled(fixedRate = 3600000) // 60분마다 호출
    public void updateApiDataForAllDistricts() {
        log.info("서울특별시 모든 구에 대한 외부 API 데이터 갱신 중...");
        for (String district : DistrictConstants.DISTRICTS) {
            String apiResponse = fetchApiData("서울특별시", district, 1, 10);
            districtApiResponses.put(district, apiResponse);
        }
        // 데이터 갱신 완료 후 이벤트 발행
        eventPublisher.publishEvent(new DataUpdateCompleteEvent("서울특별시 모든 구의 데이터 갱신 완료"));

        log.info("서울특별시 모든 구의 데이터 갱신 완료");
    }

    private String fetchApiData(String stage1, String stage2, int pageNo, int numOfRows) {
        String baseURL = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire";
        URI uri = UriComponentsBuilder.fromHttpUrl(baseURL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("STAGE1", stage1)
                .queryParam("STAGE2", stage2)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        log.info("지역: " + stage2);
        log.info("지역의 API 요청 URI: " + uri + " | 지역: " + stage2);
        return restTemplate.getForObject(uri, String.class);
    }

    public String getCachedApiResponseForDistrict(String district) {
        return districtApiResponses.get(district);
    }
}
