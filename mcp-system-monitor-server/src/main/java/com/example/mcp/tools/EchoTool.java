package com.example.mcp.tools;

import com.example.mcp.Tool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** 입력 메시지를 그대로 돌려주는 가장 단순한 도구(연동 테스트용). */
public class EchoTool implements Tool {

    @Override public String name() { return "echo"; }

    @Override public String description() { return "입력한 message 를 그대로 돌려줍니다. (연동 테스트용)"; }

    @Override public ObjectNode inputSchema(ObjectMapper m) {
        ObjectNode s = m.createObjectNode();
        s.put("type", "object");
        ObjectNode props = s.putObject("properties");
        ObjectNode msg = props.putObject("message");
        msg.put("type", "string");
        s.putArray("required").add("message");
        return s;
    }

    @Override public String call(JsonNode args, ObjectMapper m) {
        return "echo: " + args.path("message").asText("");
    }
}
