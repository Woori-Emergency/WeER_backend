package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.entity.Icu;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.repository.IcuRepository;
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
public class IcuApiService {

    @Autowired
    private CommonApiService commonApiService;

    @Autowired
    private IcuRepository icuRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    private final List<String> districts = DistrictConstants.DISTRICTS;

    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        System.out.println("ICU 데이터 이벤트 수신: " + event.getMessage());
        getIcuInfoForAllDistricts();
    }

    @Transactional
    public String getIcuInfoForAllDistricts() {
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

                    Integer hvcc = XmlParsingUtils.parseIntegerSafely(doc, "hvcc", i);
                    Integer hvncc = XmlParsingUtils.parseIntegerSafely(doc, "hvncc", i);
                    Integer hvccc = XmlParsingUtils.parseIntegerSafely(doc, "hvccc", i);
                    Integer hvicc = XmlParsingUtils.parseIntegerSafely(doc, "hvicc", i);
                    Integer hv2 = XmlParsingUtils.parseIntegerSafely(doc, "hv2", i);
                    Integer hv3 = XmlParsingUtils.parseIntegerSafely(doc, "hv3", i);
                    Integer hv6 = XmlParsingUtils.parseIntegerSafely(doc, "hv6", i);
                    Integer hv8 = XmlParsingUtils.parseIntegerSafely(doc, "hv8", i);
                    Integer hv9 = XmlParsingUtils.parseIntegerSafely(doc, "hv9", i);
                    Integer hv32 = XmlParsingUtils.parseIntegerSafely(doc, "hv32", i);
                    Integer hv34 = XmlParsingUtils.parseIntegerSafely(doc, "hv34", i);
                    Integer hv35 = XmlParsingUtils.parseIntegerSafely(doc, "hv35", i);
                    Integer hvs11 = XmlParsingUtils.parseIntegerSafely(doc, "hvs11", i);
                    Integer hvS08 = XmlParsingUtils.parseIntegerSafely(doc, "hvs08", i);
                    Integer hvs16 = XmlParsingUtils.parseIntegerSafely(doc, "hvs16", i);
                    Integer hvs17 = XmlParsingUtils.parseIntegerSafely(doc, "hvs17", i);
                    Integer hvs06 = XmlParsingUtils.parseIntegerSafely(doc, "hvs06", i);
                    Integer hvs07 = XmlParsingUtils.parseIntegerSafely(doc, "hvs07", i);
                    Integer hvs12 = XmlParsingUtils.parseIntegerSafely(doc, "hvs12", i);
                    Integer hvs13 = XmlParsingUtils.parseIntegerSafely(doc, "hvs13", i);
                    Integer hvs14 = XmlParsingUtils.parseIntegerSafely(doc, "hvs14", i);
                    Integer hvs09 = XmlParsingUtils.parseIntegerSafely(doc, "hvs09", i);
                    Integer hvs15 = XmlParsingUtils.parseIntegerSafely(doc, "hvs15", i);
                    Integer hvs18 = XmlParsingUtils.parseIntegerSafely(doc, "hvs18", i);

                    saveOrUpdateIcu(hpid, hvcc, hvncc, hvccc, hvicc, hv2, hv3, hv6, hv8, hv9, hv32, hv34, hv35, hvs11,
                            hvS08, hvs16, hvs17, hvs06, hvs07, hvs12, hvs13, hvs14, hvs09, hvs15, hvs18);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("XML 파싱 오류 발생");
            }
        }
        return "서울특별시의 모든 구에 대한 중환자실 데이터가 성공적으로 저장되었습니다.";
    }

    private void saveOrUpdateIcu(String hpid, Integer hvcc, Integer hvncc, Integer hvccc, Integer hvicc, Integer hv2, Integer hv3, Integer hv6,
                                 Integer hv8, Integer hv9, Integer hv32, Integer hv34, Integer hv35, Integer hvs11, Integer hvS08,
                                 Integer hvs16, Integer hvs17, Integer hvs06, Integer hvs07, Integer hvs12, Integer hvs13, Integer hvs14,
                                 Integer hvs09, Integer hvs15, Integer hvs18) {
        Optional<Hospital> hospitalOptional = hospitalRepository.findByHpid(hpid);

        if (hospitalOptional.isPresent()) {
            Hospital hospital = hospitalOptional.get();

            // 먼저 Icu를 저장
            Icu icu = icuRepository.findByHpid(hpid).orElse(new Icu());
            icu.setHpid(hpid);
            icu.setHvcc(hvcc);
            icu.setHvncc(hvncc);
            icu.setHvccc(hvccc);
            icu.setHvicc(hvicc);
            icu.setHv2(hv2);
            icu.setHv3(hv3);
            icu.setHv6(hv6);
            icu.setHv8(hv8);
            icu.setHv9(hv9);
            icu.setHv32(hv32);
            icu.setHv34(hv34);
            icu.setHv35(hv35);
            icu.setHvs11(hvs11);
            icu.setHvs08(hvS08);
            icu.setHvs16(hvs16);
            icu.setHvs17(hvs17);
            icu.setHvs06(hvs06);
            icu.setHvs07(hvs07);
            icu.setHvs12(hvs12);
            icu.setHvs13(hvs13);
            icu.setHvs14(hvs14);
            icu.setHvs09(hvs09);
            icu.setHvs15(hvs15);
            icu.setHvs18(hvs18);

            // Icu를 먼저 저장하여 영속 상태로 만듦
            icuRepository.save(icu);

            // Hospital 엔티티에 Icu 참조 설정
            hospital.setIcuId(icu);

            // Hospital 저장
            hospitalRepository.save(hospital);
        } else {
            System.out.println("해당 hpid(" + hpid + ")에 대한 Hospital을 찾을 수 없습니다.");
        }
    }

}
