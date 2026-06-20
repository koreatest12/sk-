package com.example.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/** Tools 메뉴에서 실행되는 액션. 시스템 상태를 조회해 대화상자로 보여준다. */
public class SystemStatusAction extends AnAction {

    private final SystemStatusService service = new SystemStatusService();

    @Override
    public void actionPerformed(AnActionEvent e) {
        String status = service.collect();
        // 필요 시: String result = service.analyzeWithAI(status);
        Messages.showInfoMessage(status, "System Status (AI)");
    }
}
