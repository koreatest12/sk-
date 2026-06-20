package com.example.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * MCP 도구(tool) 하나가 구현해야 하는 인터페이스.
 *
 *  ── 본인이 새 도구를 추가하려면 ──────────────────────────────
 *  1) 이 인터페이스를 구현하는 클래스를 tools 패키지에 만들고
 *  2) McpServer 생성자에서 registry.register(new MyTool()); 로 등록하세요.
 *  ───────────────────────────────────────────────────────────
 */
public interface Tool {

    /** 도구 식별 이름 (예: "system_status"). LLM이 이 이름으로 호출합니다. */
    String name();

    /** 도구 설명. LLM이 "언제 이 도구를 쓸지" 판단하는 근거가 됩니다. */
    String description();

    /** 입력 파라미터의 JSON Schema. 파라미터가 없으면 빈 object를 돌려주면 됩니다. */
    ObjectNode inputSchema(ObjectMapper m);

    /**
     * 실제 도구 실행 로직.
     * @param arguments tools/call 로 전달된 arguments (없을 수 있음)
     * @return MCP content 배열에 담길 결과(텍스트). 여기에 본인 로직을 넣으세요.
     */
    String call(JsonNode arguments, ObjectMapper m);
}
