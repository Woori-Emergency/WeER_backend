package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Equipment;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.EquipmentRepository;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentApiService {

    private final CommonApiService commonApiService;
    private final EquipmentRepository equipmentRepository;
    private final HospitalRepository hospitalRepository;


    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        log.info("장비 데이터 이벤트 수신: " + event.getMessage());
        getEquipmentInfoForAllDistricts();
    }

    @Transactional
    public String getEquipmentInfoForAllDistricts() {
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
                        log.error("API 호출 실패: " + resultMsg);
                    } else {

                        NodeList items = doc.getElementsByTagName("item");
                        for (int i = 0; i < items.getLength(); i++) {
                            Node item = items.item(i);  // 현재 <item> 노드를 기준으로 파싱

                            String hpid = XmlParsingUtils.getTextContentSafely(item, "hpid");
                            Boolean hvventiAYN = XmlParsingUtils.parseBooleanSafely(item, "hvventiayn");
                            Boolean hvventisoAYN = XmlParsingUtils.parseBooleanSafely(item, "hvventisoayn");
                            Boolean hvinCUAYN = XmlParsingUtils.parseBooleanSafely(item, "hvincuayn");
                            Boolean hvcrrTAYN = XmlParsingUtils.parseBooleanSafely(item, "hvcrrtayn");
                            Boolean hvecmoAYN = XmlParsingUtils.parseBooleanSafely(item, "hvecmoayn");
                            Boolean hvhypoAYN = XmlParsingUtils.parseBooleanSafely(item, "hvhypoayn");
                            Boolean hvoxyAYN = XmlParsingUtils.parseBooleanSafely(item, "hvoxyayn");
                            Boolean hvctAYN = XmlParsingUtils.parseBooleanSafely(item, "hvctayn");
                            Boolean hvmriAYN = XmlParsingUtils.parseBooleanSafely(item, "hvmriayn");
                            Boolean hvangioAYN = XmlParsingUtils.parseBooleanSafely(item, "hvangioayn");

                            Integer hvs30 = XmlParsingUtils.parseIntegerSafely(item, "hvs30");
                            Integer hvs31 = XmlParsingUtils.parseIntegerSafely(item, "hvs31");
                            Integer hvs32 = XmlParsingUtils.parseIntegerSafely(item, "hvs32");
                            Integer hvs33 = XmlParsingUtils.parseIntegerSafely(item, "hvs33");
                            Integer hvs34 = XmlParsingUtils.parseIntegerSafely(item, "hvs34");
                            Integer hvs35 = XmlParsingUtils.parseIntegerSafely(item, "hvs35");
                            Integer hvs37 = XmlParsingUtils.parseIntegerSafely(item, "hvs37");
                            Integer hvs27 = XmlParsingUtils.parseIntegerSafely(item, "hvs27");
                            Integer hvs28 = XmlParsingUtils.parseIntegerSafely(item, "hvs28");
                            Integer hvs29 = XmlParsingUtils.parseIntegerSafely(item, "hvs29");

                            saveOrUpdateEquipment(hpid, hvventiAYN, hvventisoAYN, hvinCUAYN, hvcrrTAYN, hvecmoAYN, hvhypoAYN,
                                    hvoxyAYN, hvctAYN, hvmriAYN, hvangioAYN, hvs30, hvs31, hvs32, hvs33,
                                    hvs34, hvs35, hvs37, hvs27, hvs28, hvs29);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("XML 파싱 오류 발생");
                }
            }
        }
        return "서울특별시의 모든 구에 대한 장비 데이터가 성공적으로 저장되었습니다.";
    }

    private void saveOrUpdateEquipment(String hpid, Boolean hvventiAYN, Boolean hvventisoAYN, Boolean hvinCUAYN, Boolean hvcrrTAYN,
                                       Boolean hvecmoAYN, Boolean hvhypoAYN, Boolean hvoxyAYN, Boolean hvctAYN, Boolean hvmriAYN,
                                       Boolean hvangioAYN, Integer hvs30, Integer hvs31, Integer hvs32, Integer hvs33, Integer hvs34,
                                       Integer hvs35, Integer hvs37, Integer hvs27, Integer hvs28, Integer hvs29) {
        // Optional<Hospital>을 통해 hpid로 Hospital 엔티티 검색
        Optional<Hospital> hospitalOptional = hospitalRepository.findByHpid(hpid);

        // Equipment 엔티티 생성 또는 기존 엔티티 로드
        Equipment equipment = equipmentRepository.findByHpid(hpid).orElse(new Equipment());

        if (equipment.getHpid() == null) {
            equipment.setHpid(hpid);
        }

        // Equipment 필드 설정
        equipment.setHvventiAYN(hvventiAYN);
        equipment.setHvventisoAYN(hvventisoAYN);
        equipment.setHvinCUAYN(hvinCUAYN);
        equipment.setHvcrrTAYN(hvcrrTAYN);
        equipment.setHvecmoAYN(hvecmoAYN);
        equipment.setHvhypoAYN(hvhypoAYN);
        equipment.setHvoxyAYN(hvoxyAYN);
        equipment.setHvctAYN(hvctAYN);
        equipment.setHvmriAYN(hvmriAYN);
        equipment.setHvangioAYN(hvangioAYN);
        equipment.setHvs30(hvs30);
        equipment.setHvs31(hvs31);
        equipment.setHvs32(hvs32);
        equipment.setHvs33(hvs33);
        equipment.setHvs34(hvs34);
        equipment.setHvs35(hvs35);
        equipment.setHvs37(hvs37);
        equipment.setHvs27(hvs27);
        equipment.setHvs28(hvs28);
        equipment.setHvs29(hvs29);

        // Equipment 저장
        equipment = equipmentRepository.save(equipment);

        // Hospital이 존재할 경우 외래 키 관계 설정 후 저장
        if (hospitalOptional.isPresent()) {
            Hospital hospital = hospitalOptional.get();
            hospital.setEquipmentId(equipment);  // Hospital에 Equipment 외래키 설정
            hospitalRepository.save(hospital);   // Hospital 저장
        } else {
            log.error("해당 hpid(" + hpid + ")에 대한 Hospital을 찾을 수 없습니다.");
        }
    }

}
