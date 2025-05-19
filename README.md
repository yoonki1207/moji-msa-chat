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

## 🚀 배포
### 1. jar 파일 빌드 후 서버로 옮기기.
`scp` 명령어를 이용하여 배포할 jar 파일을 서버의 특정 리렉토리로 옮깁니다.
```
scp -P {PORT} {JAR_FILE_DIR}.jar {USERNAME}@{SERVER_IP}:/downloads/a-chat
```

### 2. SSH 접속
SSH에 접속합니다.
```
ssh -p {SERVER_PORT} {USERNAME}@{SERVER_IP}
```
옮겼던 파일 위치로 이동합니다.
```
cd ./downloads/a-chat
```

### 3. 백그라운드 실행
jar 파일을 백그라운드에서 실행시킵니다.
이미 포트를 사용중이라면 `netstat -tupln` 명령어로 해당 포트를 사용하는 프로세스를 찾고 종료한 후에 jar 파일을  실행시킵니다.
```
nohup java -jar {JAR_FILE} &
```

