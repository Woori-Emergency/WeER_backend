package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Emergency;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.EmergencyRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.util.XmlParsingUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyApiService {

    @Autowired
    private CommonApiService commonApiService;

    @Autowired
    private EmergencyRepository emergencyRepository;

    private final List<String> districts = DistrictConstants.DISTRICTS;

    @Autowired
    private HospitalRepository hospitalRepository;

    /**
     * DataUpdateCompleteEvent 발생 시 응급실 정보를 저장하는 메서드
     */
    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        System.out.println("이벤트 수신: " + event.getMessage());
        getEmergencyInfoForAllDistricts();
    }

    /**
     * 서울특별시의 모든 구에 대하여 실시간 정보 가져와 저장
     * @return
     */
    @Transactional
    public String getEmergencyInfoForAllDistricts() {
        for (String district : districts) {
            String xmlResponse = commonApiService.getCachedApiResponseForDistrict(district);
            if (xmlResponse == null) {
                System.out.println(district + " 데이터가 아직 캐싱되지 않았습니다.");
                continue;
            }

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

                String resultCode = doc.getElementsByTagName("resultCode").item(0).getTextContent();
                if (!"00".equals(resultCode)) {
                    String resultMsg = doc.getElementsByTagName("resultMsg").item(0).getTextContent();
                    System.out.println("API 호출 실패: " + resultMsg);
                    continue;
                }

                NodeList items = doc.getElementsByTagName("item");
                for (int i = 0; i < items.getLength(); i++) {
                    String hpid =  XmlParsingUtils.getTextContentSafely(doc, "hpid", i);
                    Integer hvec = XmlParsingUtils.parseIntegerSafely(doc, "hvec", i);
                    Integer hv27 = XmlParsingUtils.parseIntegerSafely(doc, "hv27", i);
                    Integer hv29 = XmlParsingUtils.parseIntegerSafely(doc, "hv29", i);
                    Integer hv30 = XmlParsingUtils.parseIntegerSafely(doc, "hv30", i);
                    Integer hv28 = XmlParsingUtils.parseIntegerSafely(doc, "hv28", i);
                    Integer hv15 = XmlParsingUtils.parseIntegerSafely(doc, "hv15", i);
                    Integer hv16 = XmlParsingUtils.parseIntegerSafely(doc, "hv16", i);
                    Integer hvs01 = XmlParsingUtils.parseIntegerSafely(doc, "hvs01", i);
                    Integer hvs59 = XmlParsingUtils.parseIntegerSafely(doc, "hvs59", i);
                    Integer hvs52 = XmlParsingUtils.parseIntegerSafely(doc, "hvs52", i);
                    Integer hvs51 = XmlParsingUtils.parseIntegerSafely(doc, "hvs51", i);
                    Integer hvs02 = XmlParsingUtils.parseIntegerSafely(doc, "hvs02", i);
                    Integer hvs48 = XmlParsingUtils.parseIntegerSafely(doc, "hvs48", i);
                    Integer hvs49 = XmlParsingUtils.parseIntegerSafely(doc, "hvs49", i);
                    saveOrUpdateEmergency(hpid, hvec, hv27, hv29, hv30, hv28, hv15, hv16, hvs01, hvs59, hvs52, hvs51, hvs02, hvs48, hvs49);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("XML 파싱 오류 발생");
            }
        }
        return "서울특별시의 모든 구에 대한 응급실 데이터가 성공적으로 저장되었습니다.";
    }

    private void saveOrUpdateEmergency(String hpid, Integer hvec, Integer hv27, Integer hv29, Integer hv30, Integer hv28, Integer hv15, Integer hv16,
                                       Integer hvs01, Integer hvs59, Integer hvs52, Integer hvs51, Integer hvs02, Integer hvs48, Integer hvs49) {
        Optional<Hospital> hospitalOptional = hospitalRepository.findByHpid(hpid);

        if (hospitalOptional.isPresent()) {
            Hospital hospital = hospitalOptional.get();

            // hpid로 Emergency 정보 조회 또는 새로 생성
            Emergency emergency = emergencyRepository.findByHpid(hpid).orElse(new Emergency());

            if (emergency.getHpid() == null) {
                emergency.setHpid(hpid);
            }

            // 필드 설정
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

            // Hospital과의 외래 키 관계 설정
            hospital.setEmergencyId(emergency);
            emergencyRepository.save(emergency);  // Emergency 저장
            hospitalRepository.save(hospital);    // Hospital 업데이트
        } else {
            System.out.println("해당 hpid(" + hpid + ")를 가진 Hospital을 찾을 수 없습니다.");
        }
    }

}
