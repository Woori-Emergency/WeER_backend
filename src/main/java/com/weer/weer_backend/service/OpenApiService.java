package com.weer.weer_backend.service;

import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.HospitalRepository;
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
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class OpenApiService {
    @Value("${OPENAPI_SERVICE_KEY}")
    private String SERVICE_KEY;
    private final String BASE_URL = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HospitalRepository hospitalRepository;

    private final List<String> districts = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구",
            "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구",
            "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    public void getEmergencyInfoForAllDistricts(String stage1, int pageNo, int numOfRows) {
        for (String stage2 : districts) {
            getEmergencyInfoAndSave(stage1, stage2, pageNo, numOfRows);
        }
    }

    public String getEmergencyInfoAndSave(String stage1, String stage2, int pageNo, int numOfRows) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("Q0", stage1)
                .queryParam("Q1", stage2) // `stage2`를 그대로 전달
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode(StandardCharsets.UTF_8) // 한 번만 인코딩
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
                String dutyAddr = getTextContentSafely(doc, "dutyAddr", i);
                String dutyEmcls = getTextContentSafely(doc, "dutyEmcls", i);
                String dutyEmclsName = getTextContentSafely(doc, "dutyEmclsName", i);
                String dutyName = getTextContentSafely(doc, "dutyName", i);
                String dutyTel1 = getTextContentSafely(doc, "dutyTel1", i);
                String dutyTel3 = getTextContentSafely(doc, "dutyTel3", i);
                String hpid = getTextContentSafely(doc, "hpid", i);
                String latitude = getTextContentSafely(doc, "wgs84Lat", i);
                String longitude = getTextContentSafely(doc, "wgs84Lon", i);

                Hospital hospital = Hospital.builder()
                        .hpid(hpid)
                        .name(dutyName)
                        .address(dutyAddr)
                        .tel(dutyTel1)
                        .erTel(dutyTel3)
                        .latitude(latitude != null ? Double.parseDouble(latitude) : null)
                        .longitude(longitude != null ? Double.parseDouble(longitude) : null)
                        .build();

                // hpid로 병원 정보를 저장하거나 업데이트하는 메서드 호출
                saveOrUpdateHospital(hpid, dutyName, dutyAddr, dutyTel1, dutyTel3, latitude, longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "XML 파싱 오류 발생";
        }

        return "서울특별시 데이터가 저장되었습니다.";
    }

    //hpid가 존재하면 업데이트, 존재하지 않으면 삽입
    private void saveOrUpdateHospital(String hpid, String name, String address, String tel, String erTel, String latitude, String longitude) {
        Optional<Hospital> existingHospital = hospitalRepository.findByHpid(hpid);

        Hospital hospital = existingHospital.orElseGet(Hospital::new);
        hospital.setHpid(hpid);
        hospital.setName(name);
        hospital.setAddress(address);
        hospital.setTel(tel);
        hospital.setErTel(erTel);
        hospital.setLatitude(latitude != null ? Double.parseDouble(latitude) : null);
        hospital.setLongitude(longitude != null ? Double.parseDouble(longitude) : null);

        hospitalRepository.save(hospital);
    }

    // Helper method to get text content safely
    private String getTextContentSafely(Document doc, String tagName, int index) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        return (nodeList != null && nodeList.item(index) != null) ? nodeList.item(index).getTextContent() : null;
    }


}
