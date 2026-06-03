# @PrePersist

Entity가 DB에 처음 저장되기 직전에 자동으로 실행되는 메서드.

## 동작 순서

```
save() 호출
    → @PrePersist 실행 (createdAt = 현재시간 세팅)
    → DB에 INSERT
```

## 사용 예시

```java
@PrePersist
public void prePersist() {
    this.createdAt = LocalDateTime.now();
}
```

## @PrePersist 없을 때

```java
// 매번 직접 세팅해야 함
user.setCreatedAt(LocalDateTime.now());
userRepository.save(user);
```

## @PrePersist 있을 때

```java
// 그냥 저장만 해도 자동으로 세팅됨
userRepository.save(user);
```

## 주의사항

- 저장(INSERT) 할 때만 실행됨
- 수정(UPDATE) 할 때는 실행 안 됨
- 수정 시간 자동 세팅은 @PreUpdate 를 따로 사용

---

# JWT

JSON Web Token. 로그인 상태를 증명하는 표.

서버는 로그인 상태를 기억하지 않기 때문에 (Stateless) 클라이언트가 매번 JWT를 들고 와서 증명해야 함.

## 생김새

```
헤더.내용.서명
```

점(.)으로 3부분으로 나뉨.

- **헤더** - 어떤 방식으로 암호화했는지
- **내용** - username, 만료시간 등 실제 데이터 (JSON 형태)
- **서명** - 위조 방지용

## 내용은 JSON

```json
{
  "sub": "john",
  "iat": 1717200000,
  "exp": 1717203600
}
```

이걸 Base64로 인코딩한 것이 JWT의 내용 부분.
Base64는 암호화가 아니라 인코딩이라 누구나 디코딩해서 볼 수 있음.
그래서 비밀번호 같은 민감한 정보는 절대 넣으면 안 됨.

## 흐름

```
1. 로그인 성공
2. 서버가 JWT 발급해서 프론트에 줌
3. 프론트는 JWT 저장 (localStorage 등)
4. 다음 요청마다 JWT를 헤더에 담아서 보냄
5. 서버가 JWT 확인해서 누구인지 파악
```

## 암호화 vs 서명

- **암호화** - 내용을 못 보게 숨기는 것
- **서명** - 내용이 진짜인지 증명하는 것

JWT는 암호화가 아니라 서명만 함. 내용은 누구나 볼 수 있음.

## 비밀키를 사용하는 이유

비밀키는 내용을 숨기려는 게 아니라 서명을 만드는 재료.

```
내용 + 비밀키 → 서명 생성
```

해커가 내용을 바꾸면 서명을 다시 만들어야 하는데, 비밀키가 없으니 못 만듦.
서버는 서명 불일치로 위조된 토큰을 걸러냄.

내용을 숨기고 싶으면 HTTPS로 전송하면 됨. (보통 그렇게 함)

---

# SecurityContextHolder

JWT 토큰 확인이 끝나면 Spring Security 한테 "이 사람 로그인된 사람이야" 라고 알려줘야 함.

```java
// 1. username 으로 로그인 정보 객체 만들기
UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(username, null, List.of());

// 2. Spring Security 보관함에 저장
SecurityContextHolder.getContext().setAuthentication(auth);
```

- 1번 - username 담아서 로그인 정보 만들기 (비밀번호는 이미 토큰으로 인증했으니 null)
- 2번 - 그걸 Spring Security 보관함에 저장

저장해두면 컨트롤러에서 `@AuthenticationPrincipal` 로 꺼내 쓸 수 있음.

```java
@GetMapping("/rooms")
public List<ChatRoom> getMyChatRooms(@AuthenticationPrincipal String username) {
    return chatRoomService.getRoomsByUsername(username);
}
```

Spring 이 자동으로 현재 로그인한 사람의 username 을 넣어줌.

---

# WebSocket / STOMP 설정

## 용어 정리

- **WebSocket** - 서버와 클라이언트가 연결을 끊지 않고 계속 통신하는 방식. 채팅처럼 실시간이 필요할 때 사용.
- **STOMP** - WebSocket 위에서 동작하는 메시지 규칙. 어디로 보낼지 주소를 정할 수 있음.
- **SockJS** - WebSocket을 지원하지 않는 구형 브라우저에서도 동작하도록 해주는 fallback 라이브러리.

## 메시지 흐름

```
클라이언트 → /app/... 로 전송
    → 서버 @MessageMapping 메서드 처리
    → /topic/... 으로 구독자들에게 전달
```

## 설정 항목

| 설정 | 의미 |
|------|------|
| `enableSimpleBroker("/topic")` | 서버가 클라이언트로 메시지를 보내는 경로 |
| `setApplicationDestinationPrefixes("/app")` | 클라이언트가 서버로 메시지를 보내는 경로 prefix |
| `addEndpoint("/ws")` | 프론트에서 WebSocket 연결할 주소 |
| `withSockJS()` | WebSocket 미지원 브라우저 대비 fallback |

---

# @AuthenticationPrincipal UserDetails userDetails

JWT 인증이 완료된 현재 로그인한 사용자 정보를 자동으로 가져오는 어노테이션.

## 흐름

```
클라이언트가 요청할 때 Header에 JWT 토큰 포함
    → JwtFilter가 토큰 검증 후 사용자 정보를 SecurityContext에 저장
    → @AuthenticationPrincipal이 SecurityContext에서 사용자 정보를 꺼내줌
```

## 사용 예시

```java
@GetMapping("/rooms")
public ResponseEntity<List<ChatRoomResponse>> getRooms(
        @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername(); // 현재 로그인한 사용자 이름
    ...
}
```

## 주의사항

- JWT 생성 시 넣은 정보만 꺼낼 수 있음
- 저희 프로젝트는 username만 넣었기 때문에 getUsername() 만 사용 가능
- userId, role 등이 필요하면 JWT 생성 시 추가로 넣어야 함
