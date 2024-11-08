package com.weer.weer_backend.service;

import com.weer.weer_backend.entity.Emergency;
import com.weer.weer_backend.repository.EmergencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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

@Service
public class EmergencyApiService {

    @Value("${OPENAPI_SERVICE_KEY}")
    private String SERVICE_KEY;

    private final String BASE_URL = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmergencyRepository emergencyRepository;

    // 서울특별시의 모든 구 목록
    private final List<String> districts = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구",
            "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구",
            "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    /**
     * Calls the method to fetch and save emergency info every 60 minutes.
     */
    @Scheduled(fixedRate = 3600000) // 3600000 ms = 60 minutes
    public void scheduledEmergencyDataUpdate() {
        System.out.println("Scheduled task: Fetching emergency info for all districts.");
        getEmergencyInfoForAllDistricts();
    }

    /**
     * 서울특별시의 모든 구에 대해 응급실 정보를 가져와 저장
     */
    public String getEmergencyInfoForAllDistricts() {
        int pageNo = 1;
        int numOfRows = 10;
        String stage1 = "서울특별시";
        for (String stage2 : districts) {
            fetchAndSaveEmergencyInfo(stage1, stage2, pageNo, numOfRows);
        }
        return "서울특별시의 모든 구에 대한 응급실 데이터가 성공적으로 저장되었습니다.";
    }

    public String fetchAndSaveEmergencyInfo(String stage1, String stage2, int pageNo, int numOfRows) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("STAGE1", stage1)
                .queryParam("STAGE2", stage2)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        System.out.println("요청 URI: " + uri);

        String xmlResponse = restTemplate.getForObject(uri, String.class);
        System.out.println("API 응답: " + xmlResponse);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

            String resultCode = doc.getElementsByTagName("resultCode").item(0).getTextContent();
            if (!"00".equals(resultCode)) {
                String resultMsg = doc.getElementsByTagName("resultMsg").item(0).getTextContent();
                return "API 호출 실패: " + resultMsg;
            }

            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                String hpid = getTextContentSafely(doc, "hpid", i);
                Integer hvec = parseIntegerSafely(doc, "hvec", i);
                Integer hv27 = parseIntegerSafely(doc, "hv27", i);
                Integer hv29 = parseIntegerSafely(doc, "hv29", i);
                Integer hv30 = parseIntegerSafely(doc, "hv30", i);
                Integer hv28 = parseIntegerSafely(doc, "hv28", i);
                Integer hv15 = parseIntegerSafely(doc, "hv15", i);
                Integer hv16 = parseIntegerSafely(doc, "hv16", i);
                Integer hvs01 = parseIntegerSafely(doc, "hvs01", i);
                Integer hvs59 = parseIntegerSafely(doc, "hvs59", i);
                Integer hvs52 = parseIntegerSafely(doc, "hvs52", i);
                Integer hvs51 = parseIntegerSafely(doc, "hvs51", i);
                Integer hvs02 = parseIntegerSafely(doc, "hvs02", i);
                Integer hvs48 = parseIntegerSafely(doc, "hvs48", i);
                Integer hvs49 = parseIntegerSafely(doc, "hvs49", i);

                saveOrUpdateEmergency(hpid, hvec, hv27, hv29, hv30, hv28, hv15, hv16, hvs01, hvs59, hvs52, hvs51, hvs02, hvs48, hvs49);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "XML 파싱 오류 발생";
        }

        return "응급실 데이터가 성공적으로 저장되었습니다.";
    }

    private void saveOrUpdateEmergency(String hpid, Integer hvec, Integer hv27, Integer hv29, Integer hv30, Integer hv28, Integer hv15, Integer hv16,
                                       Integer hvs01, Integer hvs59, Integer hvs52, Integer hvs51, Integer hvs02, Integer hvs48, Integer hvs49) {

        Emergency emergency = emergencyRepository.findByHpid(hpid).orElse(new Emergency());

        if (emergency.getHpid() == null) {
            emergency.setHpid(hpid);
        }

        emergency.setHvec(hvec);
        emergency.setHv27(hv27);
        emergency.setHv29(hv29);
        emergency.setHv30(hv30);
        emergency.setHv28(hv28);
        emergency.setHv15(hv15);
        emergency.setHv16(hv16);

        emergency.setHvs01(hvs01);
        emergency.setHvs59(hvs59);
        emergency.setHvs52(hvs52);
        emergency.setHvs51(hvs51);
        emergency.setHvs02(hvs02);
        emergency.setHvs48(hvs48);
        emergency.setHvs49(hvs49);

        emergencyRepository.save(emergency);
    }

    private String getTextContentSafely(Document doc, String tagName, int index) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        return (nodeList != null && nodeList.item(index) != null) ? nodeList.item(index).getTextContent() : null;
    }

    private Integer parseIntegerSafely(Document doc, String tagName, int index) {
        String textContent = getTextContentSafely(doc, tagName, index);
        return textContent != null ? Integer.parseInt(textContent) : null;
    }
}
