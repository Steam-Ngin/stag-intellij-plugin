package com.steamngin.stag;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StagAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        StagDeployDialogWrapper dialog = new StagDeployDialogWrapper(Objects.requireNonNull(event.getProject()).getBasePath());
        dialog.show();
    }

}
