package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedHashMap;
import java.util.Map;

/** 등록된 도구들을 이름으로 보관/조회하고, tools/list 응답을 만들어 준다. */
public class ToolRegistry {

    private final Map<String, Tool> tools = new LinkedHashMap<>();

    public void register(Tool tool) {
        tools.put(tool.name(), tool);
    }

    public Tool get(String name) {
        return tools.get(name);
    }

    /** tools/list 응답에 들어갈 { "tools": [ ... ] } 형태를 만든다. */
    public ObjectNode listAsJson(ObjectMapper m) {
        ObjectNode result = m.createObjectNode();
        ArrayNode arr = result.putArray("tools");
        for (Tool t : tools.values()) {
            ObjectNode node = m.createObjectNode();
            node.put("name", t.name());
            node.put("description", t.description());
            node.set("inputSchema", t.inputSchema(m));
            arr.add(node);
        }
        return result;
    }
}
