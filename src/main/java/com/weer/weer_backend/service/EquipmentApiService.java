package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Equipment;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.EquipmentRepository;
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
public class EquipmentApiService {

    @Autowired
    private CommonApiService commonApiService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private final List<String> districts = DistrictConstants.DISTRICTS;

    @Autowired
    private HospitalRepository hospitalRepository;

    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        System.out.println("장비 데이터 이벤트 수신: " + event.getMessage());
        getEquipmentInfoForAllDistricts();
    }

    @Transactional
    public String getEquipmentInfoForAllDistricts() {
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
                    String hpid = XmlParsingUtils.getTextContentSafely(doc, "hpid", i);
                    Boolean hvventiAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvventiayn", i);
                    Boolean hvventisoAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvventisoayn", i);
                    Boolean hvinCUAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvincuayn", i);
                    Boolean hvcrrTAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvcrrtayn", i);
                    Boolean hvecmoAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvecmoayn", i);
                    Boolean hvhypoAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvhypoayn", i);
                    Boolean hvoxyAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvoxyayn", i);
                    Boolean hvctAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvctayn", i);
                    Boolean hvmriAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvmriayn", i);
                    Boolean hvangioAYN = XmlParsingUtils.parseBooleanSafely(doc, "hvangioayn", i);

                    Integer hvs30 = XmlParsingUtils.parseIntegerSafely(doc, "hvs30", i);
                    Integer hvs31 = XmlParsingUtils.parseIntegerSafely(doc, "hvs31", i);
                    Integer hvs32 = XmlParsingUtils.parseIntegerSafely(doc, "hvs32", i);
                    Integer hvs33 = XmlParsingUtils.parseIntegerSafely(doc, "hvs33", i);
                    Integer hvs34 = XmlParsingUtils.parseIntegerSafely(doc, "hvs34", i);
                    Integer hvs35 = XmlParsingUtils.parseIntegerSafely(doc, "hvs35", i);
                    Integer hvs37 = XmlParsingUtils.parseIntegerSafely(doc, "hvs37", i);
                    Integer hvs27 = XmlParsingUtils.parseIntegerSafely(doc, "hvs27", i);
                    Integer hvs28 = XmlParsingUtils.parseIntegerSafely(doc, "hvs28", i);
                    Integer hvs29 = XmlParsingUtils.parseIntegerSafely(doc, "hvs29", i);

                    saveOrUpdateEquipment(hpid, hvventiAYN, hvventisoAYN, hvinCUAYN, hvcrrTAYN, hvecmoAYN, hvhypoAYN,
                            hvoxyAYN, hvctAYN, hvmriAYN, hvangioAYN, hvs30, hvs31, hvs32, hvs33,
                            hvs34, hvs35, hvs37, hvs27, hvs28, hvs29);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("XML 파싱 오류 발생");
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
            System.out.println("해당 hpid(" + hpid + ")에 대한 Hospital을 찾을 수 없습니다.");
        }
    }

}
