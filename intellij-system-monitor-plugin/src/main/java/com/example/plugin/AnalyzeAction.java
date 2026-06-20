package com.example.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/** 시스템 상태를 수집해 AI 분석 결과까지 함께 보여주는 액션. */
public class AnalyzeAction extends AnAction {
    private final SystemStatusService service = new SystemStatusService();
    private final AiAnalyzer analyzer = new AiAnalyzer();

    @Override
    public void actionPerformed(AnActionEvent e) {
        String status = service.collect();
        String summary = analyzer.summarize(status);
        Messages.showInfoMessage(status + "\n\n" + summary, "System Analyze (AI)");
    }
}
