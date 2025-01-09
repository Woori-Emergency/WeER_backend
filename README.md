# 🦺 WeER(위알): 응급실 가용병상 모니터링 및 예약 서비스 <br>

### 🏆 우리FIS 아카데미 클라우드 엔지니어링 3기 🏅최종 프로젝트 우수상
### 🏥 고가용성 클라우드 시스템 구축(HA) 


<br>

| 🛠 Tech Stack              |                                                                                                                                                                                                                                                                                                                                                          |
| -------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 🌐 **Frontend**            | ![React](https://img.shields.io/badge/-React-61DAFB?logo=react&logoColor=white) ![Styled Components](https://img.shields.io/badge/-styled--components-DB7093?logo=styled-components&logoColor=white)                                                                                                               |
| 🖥 **Backend**             | ![Spring Framework](https://img.shields.io/badge/-Spring%20Framework-6DB33F?logo=spring&logoColor=white) ![Spring JPA](https://img.shields.io/badge/-Spring%20JPA-6DB33F?logo=spring&logoColor=white) ![Spring Security](https://img.shields.io/badge/-Spring%20Security-6DB33F?logo=springsecurity&logoColor=white) ![JWT](https://img.shields.io/badge/-JWT-000000?logo=jsonwebtokens&logoColor=white) |
| 🗃 **DB**                  | ![MySQL](https://img.shields.io/badge/-MySQL-4479A1?logo=mysql&logoColor=white)                                                                                                                                                                                                                                                                          |
| 🎥 **Monitoring & Logging**| ![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?logo=elastic&logoColor=white) ![Fluentd](https://img.shields.io/badge/-Fluentd-0E83C8?logo=fluentd&logoColor=white) ![Kibana](https://img.shields.io/badge/-Kibana-005571?logo=kibana&logoColor=white) ![Prometheus](https://img.shields.io/badge/-Prometheus-E6522C?logo=prometheus&logoColor=white) ![Grafana](https://img.shields.io/badge/-Grafana-F46800?logo=grafana&logoColor=white) |
| 🚀 **DevOps**              | ![Jenkins](https://img.shields.io/badge/-Jenkins-D24939?logo=jenkins&logoColor=white) ![ArgoCD](https://img.shields.io/badge/-ArgoCD-EB2547?logo=argo&logoColor=white) ![Docker](https://img.shields.io/badge/-Docker-2496ED?logo=docker&logoColor=white) ![SonarQube](https://img.shields.io/badge/-SonarQube-4E9BCD?logo=sonarqube&logoColor=white) |
| 🧪 **Test**                | ![K6](https://img.shields.io/badge/-k6-7D64FF?logo=k6&logoColor=white) ![JUnit](https://img.shields.io/badge/-JUnit-25A162?logo=junit5&logoColor=white)                                                                                                                                                                                                |
| 🔧 **Tools**               | ![GitHub](https://img.shields.io/badge/-GitHub-181717?logo=github&logoColor=white) ![Notion](https://img.shields.io/badge/-Notion-000000?logo=notion&logoColor=white) ![Swagger UI](https://img.shields.io/badge/-Swagger%20UI-85EA2D?logo=swagger&logoColor=black) ![Slack](https://img.shields.io/badge/-Slack-4A154B?logo=slack&logoColor=white) |                                                                                                                                                                   |
| ☁ **Amazon Web Services**                           | - **Compute:** Elastic Compute Cloud (Amazon EC2), Amazon EC2 Auto Scaling, AWS EKS, AWS Lambda<br>- **Load Balancing:** Application Load Balancer (ALB)<br> - **Storage:** Amazon S3, Amazon Relational Database Service (Amazon RDS)<br> - **Networking & Content Delivery:** Amazon Route53, Amazon Certificate Manager, Amazon Site-to-Site VPN<br> - **Monitoring & Management:** Amazon CloudWatch |
☁ **Google Cloud Platform**                           | - **Compute:** GCP Compute Engine,  GCP Instance Groups <br>- **Load Balancing:** GCP Load Balancing<br> - **Storage:** Google Cloud Storage,  Cloud SQL, Artifact Registry<br> - **Networking & Content Delivery:** Cloud VPN, Cloud DNS, Certificate Manager, Cloud Armor<br> - **Monitoring & Management:** Cloud Logging |
 
## 📁 Project
최근 ‘응급실 뺑뺑이’ 문제로 인해 생명이 위급한 중증 환자들이 적절한 치료를 받지 못하는 상황이 빈번하게 발생하고 있습니다. 이를 해결하고자, **‘WeER’** 프로젝트는 실시간 응급실 병상 정보를 제공하는 고가용성 클라우드 시스템을 구축하여 긴급 상황에서 신속한 병상 확보를 지원하는 것을 목표로 합니다.

**‘WeER’는** 클라우드 기반의 하이브리드 워크로드 환경을 도입하여, 사용자 접속이 급증하는 상황에서도 안정적인 서비스 제공이 가능하도록 설계되었습니다. 또한, 멀티 클라우드 DR(재해 복구) 환경을 구축하여 클라우드 전반의 중단 사태에도 대비하고자 합니다. 이는 최근 Microsoft 클라우드의 전체 다운타임과 같은 사고에 대응하기 위해 AWS와 GCP를 함께 사용하는 멀티 클라우드 환경을 통해 중단 없는 서비스 제공을 목표로 하고 있습니다.

**‘WeER’는** 시간대별로 변동하는 사용자 수요에 따라 유연한 확장과 축소가 가능하며, 이를 통해 응급 상황에서 환자들이 신속하게 적절한 병상에 배정되어 치료를 받을 수 있도록 하여 응급 의료 시스템의 효율성을 높이는 데 기여할 것입니다.

# 🌐 Front-End 

## 🏥 개요
본 프로젝트의 프론트엔드는 긴급 상황에서의 정보 가독성 향상에 중점을 두었습니다. 기존 상황 종합판의 정보 과잉 문제를 해결하고자, 사용자 중심의 UI/UX 개선을 진행했습니다. 특히 긴박한 순간에도 필요한 정보를 즉각적으로 파악할 수 있도록 직관적인 인터페이스를 구현했으며, 이를 통해 의사결정 속도와 업무 효율성을 향상시킬 수 있을 것으로 판단됩니다.

## 📱 화면 구성
| 페이지 | 스크린샷 | 구현 기능 |
|--------------------------------|--------------------------|--------------------------|
| 🏠 메인 화면 | <img src="https://github.com/user-attachments/assets/8e04f9ad-d34e-4d98-a24d-5aa860cc9e3b" width="300" alt="메인 페이지"/> | - 🗺️ 지도에 현재 가용 가능한 응급실 표시 <br>- 📍 거리순 응급실 조회<br>- 🔍 필터링순 응급실 조회<br> - 🏥 응급실 이름 검색으로 현 위치에서 거리 및 소요 시간 확인 |
| 📍 거리순 응급실 조회 | <img src="https://github.com/user-attachments/assets/599e05fc-58b0-4efa-b5e8-80c31b4e227b" width="300" alt="거리순 응급실 조회"/> | - 🚶‍♂️ 현재 위치에서 가장 가까운 응급실 확인 <br>- 🔧 현재 장비 확인<br> - 📆 예약 기능 |
| 🔍 필터링별 응급실 조회 | <img src="https://github.com/user-attachments/assets/348197cd-4690-41dd-9a86-9d2dc86c9ee1" width="300" alt="필터링별 응급실 조회"/> | - ⚕️ 병원 필터링 조건 제시<br>- 🏥 현재 상황에 맞는 응급실 확인 가능 |
| 👤 환자 정보 입력 | <img src="https://github.com/user-attachments/assets/ba545f3b-c18a-49c8-aa7a-9def84c9eef9" width="300" alt="환자 정보 입력"/> | - 📝 이송할 환자 상태 정보 입력 |
| 📋 환자 내역 페이지 | <img src="https://github.com/user-attachments/assets/97b329f6-da32-4f67-84f2-0195b5e1c6b3" width="300" alt="환자 내역 페이지"/> | - 🚑 현재 이송중인 환자 정보<br>- 📊 이송 완료한 환자 정보 리스트 <br>- ✅ 이송 완료 기능 |
| 📆 응급실 예약 조회 | <img src="https://github.com/user-attachments/assets/dad8ff2f-ce35-4431-b319-219ca50d939f" width="300" alt="응급실 예약 조회 페이지"/> | - 🏃 현재 이송중인 환자 정보<br>- 🔖 해당 환자를 위한 응급실 예약<br>- ✔️ 예약 승인/반려 확인<br> - ✅ 이송 완료 기능 |
| 👨‍💼 관리자 대시보드 | <img src="https://github.com/user-attachments/assets/9c96b1cd-88a7-4dca-9ba8-479c37abc651" width="300" alt="관리자 대시보드"/> | - 👥 회원 정보 확인 <br>- 📝 회원가입 요청 리스트 확인 <br>- ✅ 회원가입 승인/반려 |
| 🏥 병원 측 승인/반려 | <img src="https://github.com/user-attachments/assets/8f103fcf-2f19-4223-9620-655e9e5eeb09" width="300" alt="병원 측 승인/반려 페이지"/> | - 📋 요청 들어온 예약 확인<br>- ✔️ 예약 요청 승인/반려 |

# 👨🏻‍💻 Back-End

## 🧩 시스템 아키텍처

<br>

**| 인프라 아키텍처**

![image](https://github.com/user-attachments/assets/c39aa785-ca73-4614-a319-1683cead07e8)

WeER 프로젝트의 인프라 아키텍처는 **고가용성(HA)과** **멀티 클라우드 DR(재해 복구)를** 위해 AWS와 GCP를 결합하여 구축되었습니다. 이 시스템은 사용자 요청의 안정적인 처리와 데이터 보호를 목표로 설계되었습니다.

- AWS 인프라에서는 Amazon Route 53, AWS WAF, Amazon CloudFront를 통해 사용자 요청을 최적의 리전으로 라우팅하고 보안을 강화합니다. 웹 및 애플리케이션 서버는 다중 가용 영역에 배치되어 있으며, Application Load Balancer (ALB)가 사용자 요청을 여러 EC2 웹 서버에 분산하여 처리합니다. 데이터는 Aurora DB MySQL Connector를 통해 글로벌하게 동기화되어 병상 정보를 신속하게 조회할 수 있습니다. AWS Transit Gateway는 리전 간 네트워크 통신을 관리하며, CloudWatch와 AWS KMS가 실시간 모니터링과 데이터 보안을 제공합니다. 또한, VPN Gateway를 통해 온프레미스 데이터 센터와의 안전한 연결이 가능합니다.
- GCP 인프라에서는 Cloud Router, Cloud VPN, Cloud Armor를 통해 AWS와의 보안 통신을 지원하며, Compute Engine, Cloud SQL, Cloud Storage로 데이터 처리와 저장을 담당합니다. Database Migration Service를 통해 AWS와 데이터를 동기화하고, 재해 발생 시 Route53의 failover 기능으로 GCP로 트래픽을 자동 전환하여 서비스 연속성을 보장합니다. 

이 아키텍처는 단일 클라우드 장애에도 중단 없는 서비스를 제공할 수 있도록 AWS와 GCP의 보안 및 모니터링 도구를 결합하여 데이터 보호와 성능 최적화를 달성합니다.

#

<br>

**| 서비스 아키텍처**
![image](https://github.com/user-attachments/assets/74e69cfa-cad4-46d6-a8a7-5ab0d35fdafc)

백엔드 서버는 **Spring Framework**로 구축되었으며, **Spring Security**로 보안을 강화하고, **Spring Data JPA**를 통해 MySQL 데이터베이스와 상호작용합니다. 정기적인 데이터 업데이트와 처리는 **Spring Batch**를 통해 자동화되며, 서버는 Kubernetes 환경에서 운영되어 높은 가용성과 확장성을 제공합니다.

**Prometheus**와 **Grafana**로 서버 상태를 모니터링하여 문제 발생 시 신속히 대응합니다. 로그 관리는 **ELK Stack**을 통해 이루어지며, 시스템 활동을 추적하여 안정성을 높입니다.

CI/CD 파이프라인은 **Jenkins와 ArgoCD**를 통해 자동 빌드와 배포를 지원하며, **Docker와 SonarQube**로 애플리케이션을 컨테이너화하고 코드 품질을 관리합니다. 이 아키텍처는 사용자 수요에 따라 유연한 확장 및 축소가 가능하며, 다중 클라우드 환경에서 재해 복구 능력을 통해 중단 없는 서비스를 제공합니다.

## 🛢 DB 설계

![image](https://github.com/user-attachments/assets/f2379a12-9007-47c8-bce1-79aecb83bf7b)

| 테이블 이름             | 설명                                                                                                                           |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| **User**                | 사용자 정보를 저장하는 테이블로, 로그인 ID, 이름, 비밀번호, 역할, 이메일, 전화번호 등의 필드를 포함합니다. 인증 및 권한 부여에 필요한 정보를 관리합니다. |
| **Patient_Condition**   | 환자의 상태 정보를 기록하는 테이블입니다. 성별, 연령대, 혈압, 맥박, 체온, 호흡 수, 질병 여부, 의식 수준 등의 필드를 통해 실시간 환자 상태를 모니터링합니다. |
| **Reservation**         | 병상 예약 정보를 저장하는 테이블입니다. 병원 ID, 환자 상태 ID, 예약 상태(대기, 승인, 거절, 취소) 등을 포함하여 예약의 진행 상태를 관리합니다. |
| **ER_Announcement**     | 응급실 공지 사항을 저장하는 테이블로, 병원 ID, 메시지 유형, 메시지 내용, 중증 질환명 등을 포함하여 응급실 관련 중요 공지를 제공합니다. |
| **Hospital**            | 병원의 기본 정보를 저장하는 테이블로, 병원 ID, 기관명, 주소, 대표 전화, 응급실 전화, 위도, 경도 등의 정보를 포함하여 병원의 위치와 연락처를 관리합니다. |
| **Equipment**           | 병원 내 의료 장비 정보를 저장하는 테이블입니다. 장비의 종류와 사용 가능 여부 등을 관리하여 필요한 장비가 사용 가능한지 확인할 수 있습니다. |
| **ICU**                 | 중환자실 정보를 저장하는 테이블로, 중환자실 병상 수와 기타 세부 정보를 포함하여 중환자실의 상태를 관리합니다. |
| **Emergency_Room_Info** | 응급실 정보를 저장하는 테이블로, 병원의 응급실 구역별 병상 수 등의 정보를 기록하여 응급실에서 사용할 수 있는 병상을 관리합니다. |
