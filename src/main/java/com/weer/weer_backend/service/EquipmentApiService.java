package com.weer.weer_backend.service;

import com.weer.weer_backend.entity.Equipment;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.EquipmentRepository;
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

@Service
public class EquipmentApiService {

    @Autowired
    private CommonApiService commonApiService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private final List<String> districts = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구",
            "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구",
            "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        System.out.println("장비 데이터 이벤트 수신: " + event.getMessage());
        getEquipmentInfoForAllDistricts();
    }

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
                    String hpid = getTextContentSafely(doc, "hpid", i);
                    Boolean hvventiAYN = parseBooleanSafely(doc, "hvventiAYN", i);
                    Boolean hvventisoAYN = parseBooleanSafely(doc, "hvventisoAYN", i);
                    Boolean hvinCUAYN = parseBooleanSafely(doc, "hvinCUAYN", i);
                    Boolean hvcrrTAYN = parseBooleanSafely(doc, "hvcrrTAYN", i);
                    Boolean hvecmoAYN = parseBooleanSafely(doc, "hvecmoAYN", i);
                    Boolean hvhypoAYN = parseBooleanSafely(doc, "hvhypoAYN", i);
                    Boolean hvoxyAYN = parseBooleanSafely(doc, "hvoxyAYN", i);
                    Boolean hvctAYN = parseBooleanSafely(doc, "hvctAYN", i);
                    Boolean hvmriAYN = parseBooleanSafely(doc, "hvmriAYN", i);
                    Boolean hvangioAYN = parseBooleanSafely(doc, "hvangioAYN", i);

                    Integer hvs30 = parseIntegerSafely(doc, "hvs30", i);
                    Integer hvs31 = parseIntegerSafely(doc, "hvs31", i);
                    Integer hvs32 = parseIntegerSafely(doc, "hvs32", i);
                    Integer hvs33 = parseIntegerSafely(doc, "hvs33", i);
                    Integer hvs34 = parseIntegerSafely(doc, "hvs34", i);
                    Integer hvs35 = parseIntegerSafely(doc, "hvs35", i);
                    Integer hvs37 = parseIntegerSafely(doc, "hvs37", i);
                    Integer hvs27 = parseIntegerSafely(doc, "hvs27", i);
                    Integer hvs28 = parseIntegerSafely(doc, "hvs28", i);
                    Integer hvs29 = parseIntegerSafely(doc, "hvs29", i);

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
        Equipment equipment = equipmentRepository.findByHpid(hpid).orElse(new Equipment());

        if (equipment.getHpid() == null) {
            equipment.setHpid(hpid);
        }

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

        equipmentRepository.save(equipment);
    }

    private String getTextContentSafely(Document doc, String tagName, int index) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        return (nodeList != null && nodeList.item(index) != null) ? nodeList.item(index).getTextContent() : null;
    }

    private Integer parseIntegerSafely(Document doc, String tagName, int index) {
        String textContent = getTextContentSafely(doc, tagName, index);
        return textContent != null ? Integer.parseInt(textContent) : null;
    }

    private Boolean parseBooleanSafely(Document doc, String tagName, int index) {
        String textContent = getTextContentSafely(doc, tagName, index);
        if ("Y".equals(textContent)) {
            return true;
        } else if ("N1".equals(textContent)) {
            return false;
        }
        return null; // 다른 값일 경우 null로 처리
    }
}
