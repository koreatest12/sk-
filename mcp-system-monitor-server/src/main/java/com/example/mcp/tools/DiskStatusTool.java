package com.example.mcp.tools;

import com.example.mcp.Tool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;

/** 디스크(파일시스템 루트)별 총/가용 용량을 조회하는 도구. */
public class DiskStatusTool implements Tool {

    @Override public String name() { return "disk_status"; }

    @Override public String description() {
        return "각 파일시스템 루트의 총 용량/가용 용량/사용률을 조회합니다.";
    }

    @Override public ObjectNode inputSchema(ObjectMapper m) {
        ObjectNode s = m.createObjectNode();
        s.put("type", "object");
        s.set("properties", m.createObjectNode());
        return s;
    }

    @Override public String call(JsonNode arguments, ObjectMapper m) {
        long gb = 1024L * 1024 * 1024;
        StringBuilder sb = new StringBuilder("Disk Status\n");
        for (File root : File.listRoots()) {
            long total = root.getTotalSpace() / gb;
            long free = root.getFreeSpace() / gb;
            long used = total - free;
            int pct = total > 0 ? (int) (used * 100 / total) : 0;
            sb.append("- ").append(root.getAbsolutePath())
              .append(": used ").append(used).append("GB / total ").append(total)
              .append("GB (").append(pct).append("%)\n");
        }
        return sb.toString().trim();
    }
}
