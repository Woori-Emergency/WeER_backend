package com.weer.weer_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class EmergencyService {

    private final String SERVICE_KEY = "PAu53vTJ00tnugDJvTjgIynHw7N7rOcUhFwgXXPSC4/Dw1A4/nhbWWtKnHdWM6tte5LRtUhrUAvUiSwREGKGAA==";  // 인코딩되지 않은 원본 서비스 키 사용
    private final String BASE_URL = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire";

    @Autowired
    private RestTemplate restTemplate;

    public String getEmergencyInfo(String stage1, String stage2) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)        // 서비스 키를 인코딩되지 않은 원본으로 사용
                .queryParam("Q0", stage1)                     // 시도 주소 (예: 서울특별시)
                .queryParam("Q1", stage2)                     // 시군구 주소 (예: 강남구)
                .queryParam("pageNo", 1)                      // 페이지 번호 추가
                .queryParam("numOfRows", 10)                  // 목록 건수 추가
                .encode(StandardCharsets.UTF_8)               // UTF-8로 인코딩
                .build()
                .toUri();

        // URI 출력
        System.out.println("요청 URI: " + uri);

        // API 호출 및 응답 수신
        String response = restTemplate.getForObject(uri, String.class);
        System.out.println("API 응답: " + response);  // 응답 데이터 출력
        return response;
    }
}
