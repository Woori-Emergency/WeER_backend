package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.util.XmlParsingUtils;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalApiService {
    @Value("${OPENAPI_SERVICE_KEY}")
    private String SERVICE_KEY;
    private final String BASE_URL = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HospitalRepository hospitalRepository;

    private final List<String> districts = DistrictConstants.DISTRICTS;

    // 애플리케이션 시작 시 한 번 실행되는 메서드
    @PostConstruct
    public void init() {
        System.out.println("애플리케이션 시작 시 병원 정보 가져오기 작업 실행");
        getHospitalInfoForAllDistricts();
    }

    @Transactional
    public void getHospitalInfoForAllDistricts() {
        int pageNo = 1;
        int numOfRows = 10;
        String stage1 = "서울특별시";
        for (String stage2 : districts) {
            getHospitalInfoAndSave(stage1, stage2, pageNo, numOfRows);
        }
    }

    public String getHospitalInfoAndSave(String stage1, String stage2, int pageNo, int numOfRows) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("Q0", stage1)
                .queryParam("Q1", stage2)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        // 요청 URI 출력
        System.out.println("요청 URI: " + uri);
        String xmlResponse = restTemplate.getForObject(uri, String.class);
        System.out.println("API 응답: " + xmlResponse);

        try {
            // XML 문자열을 Document 객체로 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
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
                String dutyAddr = XmlParsingUtils.getTextContentSafely(doc, "dutyAddr", i);
                String dutyEmcls = XmlParsingUtils.getTextContentSafely(doc, "dutyEmcls", i);
                String dutyEmclsName = XmlParsingUtils.getTextContentSafely(doc, "dutyEmclsName", i);
                String dutyName = XmlParsingUtils.getTextContentSafely(doc, "dutyName", i);
                String dutyTel1 = XmlParsingUtils.getTextContentSafely(doc, "dutyTel1", i);
                String dutyTel3 = XmlParsingUtils.getTextContentSafely(doc, "dutyTel3", i);
                String hpid = XmlParsingUtils.getTextContentSafely(doc, "hpid", i);
                String latitude = XmlParsingUtils.getTextContentSafely(doc, "wgs84Lat", i);
                String longitude = XmlParsingUtils.getTextContentSafely(doc, "wgs84Lon", i);

                // city와 state를 address에서 추출하는 로직
                String city = parseCityFromAddress(dutyAddr);
                String state = parseStateFromAddress(dutyAddr);

                Hospital hospital = Hospital.builder()
                        .hpid(hpid)
                        .name(dutyName)
                        .address(dutyAddr)
                        .city(city)
                        .state(state)
                        .tel(dutyTel1)
                        .erTel(dutyTel3)
                        .latitude(latitude != null ? Double.parseDouble(latitude) : null)
                        .longitude(longitude != null ? Double.parseDouble(longitude) : null)
                        .build();

                // hpid로 병원 정보를 저장하거나 업데이트하는 메서드 호출
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
