package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.entity.Icu;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.repository.IcuRepository;
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
public class IcuApiService {
    private final CommonApiService commonApiService;
    private final IcuRepository icuRepository;
    private final HospitalRepository hospitalRepository;

    @EventListener
    public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
        log.info("ICU 데이터 이벤트 수신: " + event.getMessage());
        getIcuInfoForAllDistricts();
    }

    @Transactional
    public String getIcuInfoForAllDistricts() {
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

// item 노드 리스트 가져오기
                        NodeList items = doc.getElementsByTagName("item");
                        for (int i = 0; i < items.getLength(); i++) {
                            // 현재 item 노드 가져오기
                            Node currentItem = items.item(i);

                            // 데이터 추출
                            String hpid = XmlParsingUtils.getTextContentSafely(currentItem, "hpid");
                            Integer hvcc = XmlParsingUtils.parseIntegerSafely(currentItem, "hvcc");
                            Integer hvncc = XmlParsingUtils.parseIntegerSafely(currentItem, "hvncc");
                            Integer hvccc = XmlParsingUtils.parseIntegerSafely(currentItem, "hvccc");
                            Integer hvicc = XmlParsingUtils.parseIntegerSafely(currentItem, "hvicc");
                            Integer hv2 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv2");
                            Integer hv3 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv3");
                            Integer hv6 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv6");
                            Integer hv8 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv8");
                            Integer hv9 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv9");
                            Integer hv32 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv32");
                            Integer hv34 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv34");
                            Integer hv35 = XmlParsingUtils.parseIntegerSafely(currentItem, "hv35");
                            Integer hvs11 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs11");
                            Integer hvs08 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs08");
                            Integer hvs16 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs16");
                            Integer hvs17 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs17");
                            Integer hvs06 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs06");
                            Integer hvs07 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs07");
                            Integer hvs12 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs12");
                            Integer hvs13 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs13");
                            Integer hvs14 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs14");
                            Integer hvs09 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs09");
                            Integer hvs15 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs15");
                            Integer hvs18 = XmlParsingUtils.parseIntegerSafely(currentItem, "hvs18");

                            // ICU 데이터 저장 또는 업데이트
                            saveOrUpdateIcu(hpid, hvcc, hvncc, hvccc, hvicc, hv2, hv3, hv6, hv8, hv9, hv32, hv34, hv35, hvs11,
                                    hvs08, hvs16, hvs17, hvs06, hvs07, hvs12, hvs13, hvs14, hvs09, hvs15, hvs18);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("XML 파싱 오류 발생");
                }
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
            log.error("해당 hpid(" + hpid + ")에 대한 Hospital을 찾을 수 없습니다.");
        }
    }

}
