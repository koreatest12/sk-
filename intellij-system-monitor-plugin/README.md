# System Monitor (AI) — IntelliJ Plugin

시스템 프로세스/리소스 상태를 조회하고 AI 분석 훅으로 넘길 수 있는 IntelliJ 플러그인(학습용 스타터).

## 기술 스택
- Java 17, Gradle, `org.jetbrains.intellij` 플러그인, IntelliJ Platform SDK

## 빌드 & 실행
```bash
./gradlew build       # 최초 빌드 시 IDE SDK 다운로드(네트워크 필요)
./gradlew runIde      # 플러그인이 설치된 IDE 인스턴스 실행 후 Tools > System Status (AI)
```

## 구조
- `SystemStatusService` : 시스템 상태 수집 + AI 분석 훅(자리표시자)
- `SystemStatusAction`  : Tools 메뉴 액션
- `META-INF/plugin.xml` : 플러그인 메타데이터/액션 등록

> 스타터 코드입니다. `analyzeWithAI()`에 실제 AI 연동을 구현하고, 본인 점검 항목을 추가해 발전시키세요.
