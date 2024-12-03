package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.util.XmlParsingUtils;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalApiService {
    @Value("${OPENAPI_SERVICE_KEY}")
    private String SERVICE_KEY;
    private long start;

    private final RestTemplate restTemplate;
    private final HospitalRepository hospitalRepository;

    // 애플리케이션 시작 시 한 번 실행되는 메서드
    @PostConstruct
    public void init() {
        log.info("애플리케이션 시작 시 병원 정보 가져오기 작업 실행");
        start = System.currentTimeMillis();
        getHospitalInfoForAllDistricts();
    }

    @Transactional
    public void getHospitalInfoForAllDistricts() {
        int pageNo = 1;
        int numOfRows = 10;
        String stage1 = "서울특별시";
        for (String stage2 : DistrictConstants.DISTRICTS) {
            getHospitalInfoAndSave(stage1, stage2, pageNo, numOfRows);
        }
        long end = System.currentTimeMillis();
        long duration = end - start;
        log.info("duration: " + duration);
    }
    
    public String getHospitalInfoAndSave(String stage1, String stage2, int pageNo, int numOfRows) {
        String baseURL = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire";
        URI uri = UriComponentsBuilder.fromHttpUrl(baseURL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("Q0", stage1)
                .queryParam("Q1", stage2)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        // 요청 URI 출력
        String xmlResponse = restTemplate.getForObject(uri, String.class);

        try {
            // XML 문자열을 Document 객체로 파싱
            DocumentBuilder builder = XmlParsingUtils.createDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

            // 응답 코드 확인
            String resultCode = doc.getElementsByTagName("resultCode").item(0).getTextContent();
            if (!"00".equals(resultCode)) {
                String resultMsg = doc.getElementsByTagName("resultMsg").item(0).getTextContent();
                return "API 호출 실패: " + resultMsg;
            }

            // item 노드 리스트 가져오기
            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Node itemNode = items.item(i);  // 각 <item> 노드
                String dutyAddr = XmlParsingUtils.getTextContentSafely(itemNode, "dutyAddr");
                String dutyName = XmlParsingUtils.getTextContentSafely(itemNode, "dutyName");
                String dutyTel1 = XmlParsingUtils.getTextContentSafely(itemNode, "dutyTel1");
                String dutyTel3 = XmlParsingUtils.getTextContentSafely(itemNode, "dutyTel3");
                String hpid = XmlParsingUtils.getTextContentSafely(itemNode, "hpid");
                String latitude = XmlParsingUtils.getTextContentSafely(itemNode, "wgs84Lat");
                String longitude = XmlParsingUtils.getTextContentSafely(itemNode, "wgs84Lon");

                // city와 state를 address에서 추출하는 로직
                String city = parseCityFromAddress(dutyAddr);
                String state = parseStateFromAddress(dutyAddr);

                if (city == null || state == null) {
                    log.info("[WARN] Unable to parse city/state from address: " + dutyAddr);
                }

                // 병원 데이터 저장 또는 업데이트
                saveOrUpdateHospital(hpid, dutyName, dutyAddr, city, state, dutyTel1, dutyTel3, latitude, longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "XML 파싱 오류 발생";
        }

        return "서울특별시 데이터가 저장되었습니다.";
    }

    // hpid가 존재하면 업데이트, 존재하지 않으면 삽입
    private void saveOrUpdateHospital(String hpid, String name, String address, String city, String state, String tel, String erTel, String latitude, String longitude) {
        Optional<Hospital> existingHospital = hospitalRepository.findByHpid(hpid);

        Hospital hospital = existingHospital.orElseGet(Hospital::new);
        hospital.setHpid(hpid);
        hospital.setName(name);
        hospital.setAddress(address);
        hospital.setCity(city);
        hospital.setState(state);
        hospital.setTel(tel);
        hospital.setErTel(erTel);
        hospital.setLatitude(latitude != null ? Double.parseDouble(latitude) : null);
        hospital.setLongitude(longitude != null ? Double.parseDouble(longitude) : null);

        hospitalRepository.save(hospital);
    }

    // 주소에서 시/도를 추출하는 메서드
    private String parseCityFromAddress(String address) {
        if (address != null && address.length() > 0) {
            String[] parts = address.split(" ");
            return parts[0]; // 예: "서울특별시"
        }
        return null;
    }

    // 주소에서 구를 추출하는 메서드
    private String parseStateFromAddress(String address) {
        if (address != null && address.length() > 0) {
            String[] parts = address.split(" ");
            if (parts.length > 1) {
                return parts[1]; // 예: "강남구"
            }
        }
        return null;
    }
}
