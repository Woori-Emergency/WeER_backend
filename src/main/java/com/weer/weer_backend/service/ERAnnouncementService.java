package com.weer.weer_backend.service;

import com.weer.weer_backend.constants.DistrictConstants;
import com.weer.weer_backend.entity.ERAnnouncement;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.repository.ERAnnouncementRepository;
import com.weer.weer_backend.repository.HospitalRepository;
import com.weer.weer_backend.util.XmlParsingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@EnableScheduling  // 스케줄링 활성화
@Service
@RequiredArgsConstructor
public class ERAnnouncementService {

  private final ERAnnouncementRepository erAnnouncementRepository;
  private final HospitalRepository hospitalRepository;

  @Value("${OPENAPI_SERVICE_KEY}")
  private String SERVICE_KEY;
    private final RestTemplate restTemplate;

  // @PostConstruct 제거하고, 대신 @Scheduled 사용

  //@Scheduled(fixedRate = 3600000) // 60분마다 호출 (60분 = 3600000밀리초)
  public void scheduledTask() {
    log.info("스케줄링 작업: 병원 공지 데이터를 60분마다 불러옵니다.");
    getHospitalInfoForAllDistricts();
  }

  @Transactional
  public void getHospitalInfoForAllDistricts() {
    int pageNo = 1;
    int numOfRows = 10;
    String stage1 = "서울특별시";
    for (String stage2 : DistrictConstants.DISTRICTS) {
      getErAnnouncement(stage1, stage2, pageNo, numOfRows);
    }
  }

  @Transactional
  public String getErAnnouncement(String stage1, String stage2, int pageNo, int numOfRows) {
    List<Hospital> hospitals = hospitalRepository.findByCityAndState(stage1, stage2);

    for (Hospital hospital : hospitals) {

        String baseURL = "http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmSrsillDissMsgInqire";
        URI uri = UriComponentsBuilder.fromHttpUrl(baseURL)
              .queryParam("serviceKey", SERVICE_KEY)
              .queryParam("HPID", hospital.getHpid())
              .queryParam("pageNo", pageNo)
              .queryParam("numOfRows", numOfRows)
              .encode(StandardCharsets.UTF_8)
              .build()
              .toUri();
      log.info("요청 URI: " + uri);
      String xmlResponse = restTemplate.getForObject(uri, String.class);
      log.info("API 응답: " + xmlResponse); // API 응답 로그 출력

      try {
        // XML 문자열을 Document 객체로 파싱 (설정 변경 : 외부 엔티티 비활성화)
        DocumentBuilder builder = XmlParsingUtils.createDocumentBuilder();
          assert xmlResponse != null;
          Document doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes(StandardCharsets.UTF_8)));

        // 응답 코드 확인
        String resultCode = doc.getElementsByTagName("resultCode").item(0).getTextContent();
        if (!"00".equals(resultCode)) {
          String resultMsg = doc.getElementsByTagName("resultMsg").item(0).getTextContent();
          log.info("API 호출 실패: " + resultMsg); // 실패 메시지 로그 추가
          return "API 호출 실패: " + resultMsg;
        }

        // item 노드 리스트 가져오기
        NodeList items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
          Node currentItem = items.item(i);

          // XmlParsingUtils를 사용하여 각 태그 값 추출
          String symBlkMsg = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkMsg");
          String symBlkMsgTyp = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkMsgTyp");
          String symTypCod = XmlParsingUtils.getTextContentSafely(currentItem, "symTypCod");
          String symBlkSttDtm = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkSttDtm");
          String symBlkEndDtm = XmlParsingUtils.getTextContentSafely(currentItem, "symBlkEndDtm");

          // 날짜 파싱 (symBlkSttDtm과 symBlkEndDtm을 LocalDateTime으로 변환)
          LocalDateTime startTime = parseDateTime(symBlkSttDtm);
          LocalDateTime endTime = parseDateTime(symBlkEndDtm);

          // 로그 추가: 값들이 올바르게 추출되었는지 확인
          log.info("Hospital: " + hospital.getName() + " | Start Time: " + startTime + " | End Time: " + endTime);



          // ERAnnouncement 객체 생성 및 저장
          ERAnnouncement erAnnouncement = ERAnnouncement.builder()
                  .hospitalId(hospital)  // Ensure hospital is managed
                  .msgType(symBlkMsgTyp)
                  .message(symBlkMsg)
                  .diseaseType(symTypCod)
                  .startTime(startTime)  // start_time 저장
                  .endTime(endTime)  // end_time 저장
                  .build();

          // 로그 추가: 저장된 ERAnnouncement 값 확인
          log.info("Saving ERAnnouncement: " + erAnnouncement);

          // 실제로 저장되는지 확인
          erAnnouncementRepository.save(erAnnouncement);
        }
      } catch (Exception e) {
        log.error("XML 파싱 오류: " + e.getMessage());
        e.printStackTrace();
      }
    }
    return "서울특별시 병원 공지 데이터가 저장되었습니다.";
  }

  public static LocalDateTime parseDateTime(String dateTimeStr) {
    if (dateTimeStr == null || dateTimeStr.isEmpty()) {
      return null;
    }
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      return LocalDateTime.parse(dateTimeStr, formatter);
    } catch (DateTimeParseException e) {
      log.error("Invalid date format: " + dateTimeStr);
      return null;
    }
  }
}
