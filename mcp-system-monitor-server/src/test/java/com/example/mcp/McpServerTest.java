package com.example.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class McpServerTest {

    private final ObjectMapper m = new ObjectMapper();
    private final McpServer server = new McpServer();

    @Test
    void initialize_returnsProtocolAndServerInfo() throws Exception {
        JsonNode req = m.readTree("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\"}");
        JsonNode res = server.handleForTest(req);
        assertEquals("2024-11-05", res.path("result").path("protocolVersion").asText());
        assertEquals("system-monitor", res.path("result").path("serverInfo").path("name").asText());
    }

    @Test
    void toolsList_containsSystemStatus() throws Exception {
        JsonNode req = m.readTree("{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\"}");
        JsonNode res = server.handleForTest(req);
        JsonNode tools = res.path("result").path("tools");
        assertTrue(tools.isArray() && tools.size() >= 1);
        boolean found = false;
        for (JsonNode t : tools) if ("system_status".equals(t.path("name").asText())) found = true;
        assertTrue(found, "system_status 도구가 목록에 있어야 합니다");
    }

    @Test
    void toolsCall_systemStatus_returnsText() throws Exception {
        JsonNode req = m.readTree(
            "{\"jsonrpc\":\"2.0\",\"id\":3,\"method\":\"tools/call\"," +
            "\"params\":{\"name\":\"system_status\",\"arguments\":{}}}");
        JsonNode res = server.handleForTest(req);
        String text = res.path("result").path("content").get(0).path("text").asText();
        assertTrue(text.contains("System Status"));
    }

    @Test
    void unknownMethod_returnsError() throws Exception {
        JsonNode req = m.readTree("{\"jsonrpc\":\"2.0\",\"id\":4,\"method\":\"no/such\"}");
        JsonNode res = server.handleForTest(req);
        assertEquals(-32601, res.path("error").path("code").asInt());
    }
}
