# 서비스 이름: Chat Service

## 📌 개요
모지(MOJI)서비스의 채팅 서비스를 담당하는 마이크로 서비스입니다. 채팅방 참가, 퇴장 등 채팅방과 상호작용할 수 있는 REST API와 WebSocket을 이용한 채팅 통신이 가능합니다.

## 🚀 주요 기능
- 채팅방 참여
- 채팅방 생성
- 채팅방 퇴장
- 유저 강제 퇴장
- 메시지 조회
- 채팅방 삭제
- 메시지 송수신

## ⚙️ 기술 스택
- Language: Java 17
- Framework: Spring Boot 3.1.4
- Database: MongoDB 2.1.3
- Container: Docker 27.1.1

## 📦 기술 명세
API 문서는 [팀 Notion](https://www.notion.so/yoonki1207/MOJI-API-1e5a3b67e4c9807b91b3c99996fc9342?pvs=4)에 명시되어있습니다.

## 📄 환경 변수 (.env)
```
spring:
  data:
    mongodb:
      host: {HOST_IP}
      port: 3002
      authentication-database: admin
      username: {DB_USERNAME}
      password: {DB_PASSWORD}
      database: moji

jwt:
  secret-key: {SECERT_KEY}
```

> [!NOTE]
> 누락되거나 보안 관련 정보는 관리자 @yoonki1207로 연락하시면 됩니다.
