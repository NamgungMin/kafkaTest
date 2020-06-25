## Kafka producer / consumer 구현
* 요구 조건
    * Kafka Producer, Consumer 를 Java 로 구현
    * Kafka 내부 데이터 포맷은 Avro 로 설계 및 구현
    * Kudu Table Schema 설계 및 생성
    * 데이터 생성시점인 로그일시(yyyymmddhhmmss) 로 부터 Kudu 에 저장되어 조회 가능한 시간까지 요구되는 SLA(Service Level Agreement)는 10초 이내
    
* 제출 내역
   * High level Architecture Diagram
        * High_level_Architecture_Diagram.PNG
   * Software, Hardware 구성표
        * software_hardware 구성표.txt
   * Source codes
   * Kudu Table 생성 쿼리 및 스키마 디자인 설명
        * kudu_table_sql.txt
   * SLA 증빙자료
        * Service Level Agreement.txt
   * (optional) Troubleshooting RCA(Root Cause Analysis)
        * Troubleshooting

* 기타 제출 내역
    * kudu 테이블 조회 결과(test 데이터가 같이 있어서, 테이블이 깨끗한 정보만 있는 것은 아님.)
        * 모바일 로그 테이블 - mobile.csv
        * 거래 내역 마스터 테이블 - banking.csv
        * 거래 내역 상세 테이블 - banking_transactioncontent.csv
        
* 구현 후기
    * 전체 시스템(kudu, impala, kafka, Schema Register)를 구성하는데 생각보다 시간이 걸림.
    * 재직 중인 회사내의 테스트 하둡을 기본을 해당 system을 구성
    * consumer / producer는 로컬 PC에서 구현하여 처리
    * kudu max value를 늦게 발견하여 해당 내역을 다시 구현하느라 시간을 소비
      (kudu를 처음 사용하여, 내용 파악에 시간이 걸림. )
    * 요구 조건을 맞추긴 하였으나 아래의 내역들이 미흡
    * 해당 구현의 추가 보충점
        * kafka 관련 내역 File config로 추출
        * 데이터 예외 사항에 대한 에러 테스트 및 방어 코드 미흡
        * java doc 관련 내역 처리 미흡
        * test code의 부재
        * kafka producer를 자체 구현이 아닌 fluentd를 사용한 테스트 못함.
          (fluentd의 플러그인을 사용하면 kafka에 avro로 전송 가능 -> 조금 더 확인은 필요함)               
        
        