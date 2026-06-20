# MCP System Monitor Server (Java)

생성형 AI(Claude 등 MCP 클라이언트)가 **호스트 시스템 상태를 직접 조회**할 수 있도록 만든
최소 구현 **MCP(Model Context Protocol) 서버**입니다. 데이터센터 운영에서 익숙한 "상태 점검"을
AI가 호출 가능한 도구(tool)로 옮긴 예제입니다.

> ⚠️ 이 저장소는 **동작하는 스타터 코드**입니다. 그대로 제출하지 말고, 직접 실행해 구조를 이해한 뒤
> 본인 도구를 추가해 **본인 프로젝트로 발전**시키세요. 면접에서 설명할 수 있어야 진짜 포트폴리오가 됩니다.

## 기술 스택
- Java 17
- Jackson (JSON-RPC 메시지 처리)
- Gradle (application 플러그인: 실행 스크립트 생성)
- GitHub Actions (CI: 빌드·테스트 자동화)

## 아키텍처
```
MCP 클라이언트(Claude)  ──stdin(JSON-RPC)──▶  McpServer  ──▶  ToolRegistry  ──▶  Tool 구현체
        ▲                                         │
        └──────────────stdout(JSON-RPC)───────────┘
```
- `McpServer` : stdio로 JSON-RPC 요청을 받아 `initialize` / `tools/list` / `tools/call` 처리
- `ToolRegistry` : 도구 등록·조회, `tools/list` 응답 생성
- `Tool` : 도구가 구현할 인터페이스 (`SystemStatusTool`이 예시 구현체)

## 빌드 & 실행
```bash
# 의존성 포함 실행 스크립트 생성
./gradlew installDist
# 생성된 실행 스크립트 경로: build/install/mcp-system-monitor-server/bin/mcp-system-monitor-server

# tools/list 호출 예시
echo '{"jsonrpc":"2.0","id":1,"method":"tools/list"}' \
  | build/install/mcp-system-monitor-server/bin/mcp-system-monitor-server

# system_status 도구 호출 예시
echo '{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"system_status","arguments":{}}}' \
  | build/install/mcp-system-monitor-server/bin/mcp-system-monitor-server

# (개발 중 빠른 실행)
./gradlew run
```

## 테스트
```bash
./gradlew test
```

## MCP 클라이언트 연결 (예시)
```json
{
  "mcpServers": {
    "system-monitor": {
      "command": "<repo>/build/install/mcp-system-monitor-server/bin/mcp-system-monitor-server"
    }
  }
}
```

## 직무 연결고리
- **IT(AMHS)**: 시스템 상태를 실시간 조회·연동·자동화하는 구조는 반송 설비 관제·연계 운영과 맞닿습니다.
- **Product Engineering**: 수집 데이터를 가공해 의미 있는 신호로 만드는 접근은 수율·품질 데이터 분석으로 이어집니다.

## 직접 확장해 보기 (본인 작업 영역)
- [ ] 디스크 사용량 / 특정 포트 점검 도구 추가
- [ ] 로그 마지막 N줄 조회 도구 추가
- [ ] 임계치 초과 시 경고 메시지를 반환하는 로직 추가
- [ ] 단위 테스트 보강
