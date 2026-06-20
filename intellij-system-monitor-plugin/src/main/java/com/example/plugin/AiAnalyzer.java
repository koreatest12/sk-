package com.example.plugin;

/**
 * 시스템 상태 텍스트를 AI로 분석/요약하는 컴포넌트(자리표시자).
 * 실제로는 여기서 Claude API 등을 호출하도록 본인이 구현하세요.
 */
public class AiAnalyzer {

    public String summarize(String status) {
        // TODO: Claude API 연동. 지금은 규칙 기반 간이 요약.
        StringBuilder sb = new StringBuilder("[요약]\n");
        for (String line : status.split("\n")) {
            if (line.contains("%")) sb.append("· ").append(line.trim()).append('\n');
        }
        if (sb.length() == 4) sb.append("· 특이사항 없음");
        return sb.toString().trim();
    }
}
