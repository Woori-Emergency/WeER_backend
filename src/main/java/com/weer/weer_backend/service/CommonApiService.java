package com.weer.weer_backend.service;

import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonApiService {

    @Value("${OPENAPI_SERVICE_KEY}")
    private String SERVICE_KEY;

    private final String BASE_URL = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire";

    private final RestTemplate restTemplate;
    private final ApplicationEventPublisher eventPublisher;

    private final List<String> districts = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구",
            "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구",
            "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    private Map<String, String> districtApiResponses = new HashMap<>();

    @Autowired
    public CommonApiService(RestTemplate restTemplate, ApplicationEventPublisher eventPublisher) {
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 3600000) // 60분마다 호출
    public void updateApiDataForAllDistricts() {
        System.out.println("서울특별시 모든 구에 대한 외부 API 데이터 갱신 중...");
        for (String district : districts) {
            String apiResponse = fetchApiData("서울특별시", district, 1, 10);
            districtApiResponses.put(district, apiResponse);
        }
        // 데이터 갱신 완료 후 이벤트 발행
        eventPublisher.publishEvent(new DataUpdateCompleteEvent("서울특별시 모든 구의 데이터 갱신 완료"));
    }

    private String fetchApiData(String stage1, String stage2, int pageNo, int numOfRows) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("STAGE1", stage1)
                .queryParam("STAGE2", stage2)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        System.out.println("API 요청 URI: " + uri);
        return restTemplate.getForObject(uri, String.class);
    }

    public String getCachedApiResponseForDistrict(String district) {
        return districtApiResponses.get(district);
    }
}
