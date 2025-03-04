package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Emergency;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.EmergencyRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.util.XmlParsingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmergencyApiService {
    private final CommonApiService commonApiService;
    private final EmergencyRepository emergencyRepository;
    private final HospitalRepository hospitalRepository;
    // 생성자를 통한 의존성 주입

    /**
     * DataUpdateCompleteEvent 발생 시 응급실 정보를 저장하는 메서드
     */
    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        log.info("응급실 데이터 이벤트 수신: " + event.getMessage());
        getEmergencyInfoForAllDistricts();
    }

    /**
     * 서울특별시의 모든 구에 대하여 실시간 정보 가져와 저장
     * @return
     */
    @Transactional
    public String getEmergencyInfoForAllDistricts() {
        for (String district : DistrictConstants.DISTRICTS) {
            String xmlResponse = commonApiService.getCachedApiResponseForDistrict(district);
            if (xmlResponse == null) {
                log.error(district + " 데이터가 아직 캐싱되지 않았습니다.");
            } else {

                try {
                    DocumentBuilder builder = XmlParsingUtils.createDocumentBuilder();
                    Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

                    String resultCode = doc.getElementsByTagName("resultCode").item(0).getTextContent();
                    if (!"00".equals(resultCode)) {
                        String resultMsg = doc.getElementsByTagName("resultMsg").item(0).getTextContent();
                        log.info("API 호출 실패: " + resultMsg);
                    } else {

                        NodeList items = doc.getElementsByTagName("item");
                        for (int i = 0; i < items.getLength(); i++) {
                            Node item = items.item(i);  // 현재 <item> 태그를 기준으로 파싱

                            String hpid = XmlParsingUtils.getTextContentSafely(item, "hpid");
                            Integer hvec = XmlParsingUtils.parseIntegerSafely(item, "hvec");
                            Integer hv27 = XmlParsingUtils.parseIntegerSafely(item, "hv27");
                            Integer hv29 = XmlParsingUtils.parseIntegerSafely(item, "hv29");
                            Integer hv30 = XmlParsingUtils.parseIntegerSafely(item, "hv30");
                            Integer hv28 = XmlParsingUtils.parseIntegerSafely(item, "hv28");
                            Integer hv15 = XmlParsingUtils.parseIntegerSafely(item, "hv15");
                            Integer hv16 = XmlParsingUtils.parseIntegerSafely(item, "hv16");
                            Integer hvs01 = XmlParsingUtils.parseIntegerSafely(item, "hvs01");
                            Integer hvs59 = XmlParsingUtils.parseIntegerSafely(item, "hvs59");
                            Integer hvs03 = XmlParsingUtils.parseIntegerSafely(item, "hvs03");
                            Integer hvs04 = XmlParsingUtils.parseIntegerSafely(item, "hvs04");
                            Integer hvs02 = XmlParsingUtils.parseIntegerSafely(item, "hvs02");
                            Integer hvs48 = XmlParsingUtils.parseIntegerSafely(item, "hvs48");
                            Integer hvs49 = XmlParsingUtils.parseIntegerSafely(item, "hvs49");

                            saveOrUpdateEmergency(hpid, hvec, hv27, hv29, hv30, hv28, hv15, hv16, hvs01, hvs59, hvs03, hvs04, hvs02, hvs48, hvs49);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("XML 파싱 오류 발생");
                }
            }
        }
        return "서울특별시의 모든 구에 대한 응급실 데이터가 성공적으로 저장되었습니다.";
    }

    private void saveOrUpdateEmergency(String hpid, Integer hvec, Integer hv27, Integer hv29, Integer hv30, Integer hv28, Integer hv15, Integer hv16,
                                       Integer hvs01, Integer hvs59, Integer hvs03, Integer hvs04, Integer hvs02, Integer hvs48, Integer hvs49) {
        Hospital hospital = hospitalRepository.findByHpid(hpid)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_HOSPITAL));

        Emergency updatedEmergency = Emergency.builder()
            .hpid(hpid)
            .hvec(hvec)
            .hv27(hv27)
            .hv29(hv29)
            .hv30(hv30)
            .hv28(hv28)
            .hv15(hv15)
            .hv16(hv16)
            .hvs01(hvs01)
            .hvs59(hvs59)
            .hvs03(hvs03)
            .hvs04(hvs04)
            .hvs02(hvs02)
            .hvs48(hvs48)
            .hvs49(hvs49).build();

        // Hospital과의 외래 키 관계 설정
        emergencyRepository.save(updatedEmergency);  // Emergency 저장
        hospital.setEmergencyId(updatedEmergency);    // Hospital 업데이트
        hospitalRepository.save(hospital);
    }

}
