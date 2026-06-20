package com.example.mcp;

import com.example.mcp.tools.SystemStatusTool;
import com.example.mcp.tools.DiskStatusTool;
import com.example.mcp.tools.PortCheckTool;
import com.example.mcp.tools.EchoTool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * 최소 구현 MCP(Model Context Protocol) 서버.
 *
 * 전송 방식: stdio (줄 단위 JSON-RPC 2.0). 표준입력으로 요청을 받고 표준출력으로 응답합니다.
 * 지원 메서드: initialize, tools/list, tools/call, ping, notifications/initialized
 *
 *  ── 동작 확인(로컬) ───────────────────────────────────────────
 *   ./gradlew shadowJar
 *   echo '{"jsonrpc":"2.0","id":1,"method":"tools/list"}' | java -jar build/libs/mcp-system-monitor-server-0.1.0-all.jar
 *  ─────────────────────────────────────────────────────────────
 *
 *  ── Claude Desktop 등에 연결할 때 (예시) ─────────────────────
 *   "mcpServers": {
 *     "system-monitor": { "command": "java", "args": ["-jar", "<위 jar 경로>"] }
 *   }
 *  ─────────────────────────────────────────────────────────────
 */
public class McpServer {

    private static final ObjectMapper M = new ObjectMapper();
    private static final String PROTOCOL_VERSION = "2024-11-05";

    private final ToolRegistry registry = new ToolRegistry();

    public McpServer() {
        // 사용할 도구를 여기서 등록합니다.
        registry.register(new SystemStatusTool());
        registry.register(new DiskStatusTool());
        registry.register(new PortCheckTool());
        registry.register(new EchoTool());
        // TODO: 본인이 만든 도구를 추가하세요. 예) registry.register(new LogTailTool());
    }

    public static void main(String[] args) throws Exception {
        new McpServer().run();
    }

    public void run() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        // 로그는 stdout을 오염시키지 않도록 반드시 stderr로 남깁니다.
        System.err.println("[mcp] system-monitor server started");

        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            try {
                JsonNode req = M.readTree(line);
                ObjectNode res = handle(req);
                if (res != null) {                 // 알림(notification)은 응답이 없습니다.
                    out.println(M.writeValueAsString(res));
                }
            } catch (Exception e) {
                System.err.println("[mcp] parse/handle error: " + e.getMessage());
            }
        }
    }

    /** 메서드별로 요청을 분기 처리한다. */
    private ObjectNode handle(JsonNode req) {
        String method = req.path("method").asText("");
        JsonNode id = req.get("id");

        switch (method) {
            case "initialize":
                return result(id, initializeResult());
            case "tools/list":
                return result(id, registry.listAsJson(M));
            case "tools/call":
                return handleToolCall(id, req.path("params"));
            case "ping":
                return result(id, M.createObjectNode());
            case "notifications/initialized":
                return null; // 알림: 응답 없음
            default:
                if (id == null) return null;
                return error(id, -32601, "Method not found: " + method);
        }
    }

    private ObjectNode initializeResult() {
        ObjectNode r = M.createObjectNode();
        r.put("protocolVersion", PROTOCOL_VERSION);
        ObjectNode caps = r.putObject("capabilities");
        caps.putObject("tools"); // 이 서버가 tools 기능을 제공함을 알림
        ObjectNode info = r.putObject("serverInfo");
        info.put("name", "system-monitor");
        info.put("version", "0.1.0");
        return r;
    }

    /** tools/call 실행: 도구를 찾아 실행하고 결과를 MCP content 형식으로 감싼다. */
    private ObjectNode handleToolCall(JsonNode id, JsonNode params) {
        String name = params.path("name").asText("");
        Tool tool = registry.get(name);
        if (tool == null) {
            return error(id, -32602, "Unknown tool: " + name);
        }
        try {
            String text = tool.call(params.path("arguments"), M);
            ObjectNode res = M.createObjectNode();
            ArrayNode content = res.putArray("content");
            ObjectNode block = content.addObject();
            block.put("type", "text");
            block.put("text", text);
            res.put("isError", false);
            return result(id, res);
        } catch (Exception e) {
            return error(id, -32000, "Tool execution failed: " + e.getMessage());
        }
    }

    // ── JSON-RPC 응답 헬퍼 ──────────────────────────────────────
    private ObjectNode result(JsonNode id, ObjectNode resultNode) {
        ObjectNode res = M.createObjectNode();
        res.put("jsonrpc", "2.0");
        res.set("id", id == null ? null : id);
        res.set("result", resultNode);
        return res;
    }

    private ObjectNode error(JsonNode id, int code, String message) {
        ObjectNode res = M.createObjectNode();
        res.put("jsonrpc", "2.0");
        res.set("id", id == null ? null : id);
        ObjectNode err = res.putObject("error");
        err.put("code", code);
        err.put("message", message);
        return res;
    }

    // 테스트에서 접근하기 위한 패키지 공개 메서드
    ObjectNode handleForTest(JsonNode req) { return handle(req); }
    ToolRegistry registry() { return registry; }
}
