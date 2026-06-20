package com.example.mcp.tools;

import com.example.mcp.Tool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 현재 머신의 시스템 상태(메모리/CPU/실행 중 프로세스 수)를 돌려주는 예시 도구.
 *
 * 데이터센터 운영에서 익숙한 "상태 점검"을 MCP 도구로 옮긴 사례입니다.
 * → 본인 환경에 맞게 점검 항목(디스크, 특정 서비스 포트, 로그 등)을 추가해 확장하세요.
 */
public class SystemStatusTool implements Tool {

    @Override public String name() { return "system_status"; }

    @Override public String description() {
        return "현재 호스트의 시스템 상태(가용 프로세서 수, JVM 메모리, 실행 중 프로세스 수)를 조회합니다.";
    }

    @Override public ObjectNode inputSchema(ObjectMapper m) {
        // 입력 파라미터가 없는 도구. (있다면 properties/required 를 채우면 됩니다.)
        ObjectNode schema = m.createObjectNode();
        schema.put("type", "object");
        schema.set("properties", m.createObjectNode());
        return schema;
    }

    @Override public String call(JsonNode arguments, ObjectMapper m) {
        Runtime rt = Runtime.getRuntime();
        long mb = 1024 * 1024;
        long total = rt.totalMemory() / mb;
        long free = rt.freeMemory() / mb;
        long used = total - free;
        int cpus = rt.availableProcessors();

        long processCount;
        try {
            processCount = ProcessHandle.allProcesses().count();
        } catch (Exception e) {
            processCount = -1; // 권한 등으로 실패할 수 있음
        }

        String os = System.getProperty("os.name") + " " + System.getProperty("os.version");

        StringBuilder sb = new StringBuilder();
        sb.append("System Status\n");
        sb.append("- OS: ").append(os).append('\n');
        sb.append("- CPU(available processors): ").append(cpus).append('\n');
        sb.append("- JVM memory: used ").append(used).append("MB / total ").append(total).append("MB\n");
        sb.append("- Running processes: ").append(processCount >= 0 ? processCount : "N/A");
        return sb.toString();
    }
}
