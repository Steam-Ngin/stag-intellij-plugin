package com.steamngin.stag;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class BaseDialogWrapper extends DialogWrapper {

    public BaseDialogWrapper(String title, String content, @Nullable String okText, @Nullable String cancelText) {
        super(true); // use current window as parent
        setTitle(title);
        setOKButtonText(okText == null ? "OK" : okText);
        setCancelButtonText(cancelText == null ? "Cancel" : cancelText);
        setErrorText(content);
        _content = content;
        init();
    }

    private final String _content;

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(_content);
        label.setPreferredSize(new Dimension(100, 100));

        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }
}