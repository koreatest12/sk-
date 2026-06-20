package com.example.plugin;

/** 시스템 상태를 문자열로 수집한다. (AI 분석 입력으로 그대로 넘길 수 있음) */
public class SystemStatusService {

    public String collect() {
        Runtime rt = Runtime.getRuntime();
        long mb = 1024 * 1024;
        long total = rt.totalMemory() / mb;
        long free = rt.freeMemory() / mb;
        int cpus = rt.availableProcessors();
        long procs;
        try { procs = ProcessHandle.allProcesses().count(); } catch (Exception e) { procs = -1; }
        String os = System.getProperty("os.name") + " " + System.getProperty("os.version");

        return "System Status\n"
                + "- OS: " + os + "\n"
                + "- CPU(available processors): " + cpus + "\n"
                + "- JVM memory: used " + (total - free) + "MB / total " + total + "MB\n"
                + "- Running processes: " + (procs >= 0 ? procs : "N/A");
    }

    /**
     * AI 분석 훅(자리표시자). 실제로는 여기서 Claude API 등을 호출해
     * collect() 결과를 분석/요약하도록 본인이 구현하세요.
     */
    public String analyzeWithAI(String status) {
        // TODO: Claude API 연동 (본인 구현)
        return "[AI 분석 자리] 아래 상태를 요약/진단하도록 연결하세요:\n" + status;
    }
}
