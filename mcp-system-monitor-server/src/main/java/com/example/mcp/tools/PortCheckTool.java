package com.example.mcp.tools;

import com.example.mcp.Tool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.InetSocketAddress;
import java.net.Socket;

/** 특정 host:port 의 TCP 연결 가능 여부를 점검하는 도구. (운영 관제에서 자주 쓰는 점검) */
public class PortCheckTool implements Tool {

    @Override public String name() { return "port_check"; }

    @Override public String description() {
        return "지정한 host:port 에 TCP 연결이 가능한지(서비스 응답 여부) 점검합니다.";
    }

    @Override public ObjectNode inputSchema(ObjectMapper m) {
        ObjectNode s = m.createObjectNode();
        s.put("type", "object");
        ObjectNode props = s.putObject("properties");
        ObjectNode host = props.putObject("host");
        host.put("type", "string"); host.put("description", "대상 호스트(IP 또는 도메인)");
        ObjectNode port = props.putObject("port");
        port.put("type", "integer"); port.put("description", "대상 포트");
        s.putArray("required").add("host").add("port");
        return s;
    }

    @Override public String call(JsonNode args, ObjectMapper m) {
        String host = args.path("host").asText("");
        int port = args.path("port").asInt(0);
        if (host.isEmpty() || port <= 0) return "오류: host 와 port 를 지정하세요.";
        long start = System.currentTimeMillis();
        try (Socket sock = new Socket()) {
            sock.connect(new InetSocketAddress(host, port), 2000);
            long ms = System.currentTimeMillis() - start;
            return "Port Check\n- " + host + ":" + port + " → OPEN (" + ms + "ms)";
        } catch (Exception e) {
            return "Port Check\n- " + host + ":" + port + " → CLOSED/UNREACHABLE (" + e.getClass().getSimpleName() + ")";
        }
    }
}
