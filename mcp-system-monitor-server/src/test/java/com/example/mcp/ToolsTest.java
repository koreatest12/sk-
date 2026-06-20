package com.example.mcp;

import com.example.mcp.tools.DiskStatusTool;
import com.example.mcp.tools.EchoTool;
import com.example.mcp.tools.PortCheckTool;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {
    private final ObjectMapper m = new ObjectMapper();

    @Test
    void disk_returnsStatus() {
        String out = new DiskStatusTool().call(m.createObjectNode(), m);
        assertTrue(out.contains("Disk Status"));
    }

    @Test
    void echo_returnsMessage() {
        ObjectNode args = m.createObjectNode(); args.put("message", "hello");
        assertEquals("echo: hello", new EchoTool().call(args, m));
    }

    @Test
    void portCheck_invalidInput() {
        String out = new PortCheckTool().call(m.createObjectNode(), m);
        assertTrue(out.contains("오류"));
    }

    @Test
    void portCheck_schemaHasRequired() {
        ObjectNode schema = new PortCheckTool().inputSchema(m);
        assertEquals("object", schema.path("type").asText());
        assertTrue(schema.path("required").toString().contains("host"));
    }
}
