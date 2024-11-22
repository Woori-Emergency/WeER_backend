package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.ERAnnouncement;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.event.DataUpdateCompleteEvent;
import com.weer.weer_backend.repository.ERAnnouncementRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.util.XmlParsingUtils;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Service
@RequiredArgsConstructor
public class ERAnnouncementService {
  private final ERAnnouncementRepository erAnnouncementRepository;
  private final HospitalRepository hospitalRepository;

  private final List<String> districts = DistrictConstants.DISTRICTS;

  @Value("${OPENAPI_SERVICE_KEY}")
  private String SERVICE_KEY;
  private final String BASE_URL = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmSrsillDissMsgInqire";
  private final RestTemplate restTemplate;

  @EventListener
  public void handleDataUpdateCompleteEvent(DataUpdateCompleteEvent event) {
    System.out.println("이벤트 수신: " + event.getMessage());
    getHospitalInfoForAllDistricts();
  }

  @Transactional
  public void getHospitalInfoForAllDistricts() {
    int pageNo = 1;
    int numOfRows = 10;
    String stage1 = "서울특별시";
    for (String stage2 : districts) {
      getErAnnouncement(stage1, stage2, pageNo, numOfRows);
    }
  }

  @Transactional
  public String getErAnnouncement(String stage1, String stage2, int pageNo, int numOfRows) {
    List<Hospital> hospitals = hospitalRepository.findByCityAndState(stage1, stage2);

    for (Hospital hospital : hospitals) {

      URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
          .queryParam("serviceKey", SERVICE_KEY)
          .queryParam("HPID", hospital.getHpid())
          .queryParam("pageNo", pageNo)
          .queryParam("numOfRows", numOfRows)
          .encode(StandardCharsets.UTF_8)
          .build()
          .toUri();

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
          // 현재 item 노드 가져오기
          org.w3c.dom.Node currentItem = items.item(i);

          // 데이터 추출
          String rnum = XmlParsingUtils.getTextContentSafely(currentItem, "rnum");
          String dutyAddr = XmlParsingUtils.getTextContentSafely(currentItem, "dutyAddr");
          String dutyName = XmlParsingUtils.getTextContentSafely(currentItem, "dutyName");
          String emcOrgCod = XmlParsingUtils.getTextContentSafely(currentItem, "emcOrgCod");
          String hpid = XmlParsingUtils.getTextContentSafely(currentItem, "hpid");
          String symBlkMsg = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkMsg");
          String symBlkMsgTyp = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkMsgTyp");
          String symTypCod = XmlParsingUtils.getTextContentSafely(currentItem, "symTypCod");
          String symTypCodMag = XmlParsingUtils.getTextContentSafely(currentItem, "symTypCodMag");
          String symOutDspYon = XmlParsingUtils.getTextContentSafely(currentItem, "symOutDspYon");
          String symOutDspMth = XmlParsingUtils.getTextContentSafely(currentItem, "symOutDspMth");
          String symBlkSttDtm = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkSttDtm");
          String symBlkDtm = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkDtm");
          ERAnnouncement erAnnouncement = ERAnnouncement.builder()
              .hospitalId(hospital)  // Ensure hospital is managed
              .msgType(symBlkMsgTyp)
              .message(symBlkMsg)
              .diseaseType(symTypCod).build();

          erAnnouncementRepository.save(erAnnouncement);
        }

      } catch (Exception e) {
        e.printStackTrace();
        return "XML 파싱 오류 발생";
      }
    }
    return "서울특별시 병원 공지 데이터가 저장되었습니다.";
  }
}
