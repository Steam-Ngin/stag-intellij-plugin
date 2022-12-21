package com.steamngin.stag;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class StagDeployDialogWrapper extends DialogWrapper {
    final String path;
    StagConfig config;
    JCheckBox checkBox;
    JTextField textField;

    public StagDeployDialogWrapper(String _path) {
        super(true); // use current window as parent
        path = _path;
        setTitle("Deploy STAG");
        setOKButtonText("Deploy");
        setCancelButtonText("Cancel");
        init();
    }

    private String parseStagPath(String path) {
        try {
            return path
                    .replace("{stag_name}", config.stagName)
                    .replace("{stag_pattern}", config.stagPattern)
                    .replace("{stag_version}", config.stagVersion);
        } catch (Exception e) {
            new BaseDialogWrapper("Error", "Invalid path pattern", null, null).showAndGet();
            throw e;
        }
    }

    @Override
    protected void doOKAction() {
        final String cfgPath = parseStagPath(textField.getText());

        GeneralCommandLine commandLine = new GeneralCommandLine();
        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
//        commandLine.setWorkDirectory(isOsx ? Constants.osxScriptPath : Constants.windowsScriptPath);
        commandLine.setWorkDirectory(Constants.workingDirPath);
//        commandLine.setExePath(isOsx ? "./steamctl" : "steamcctl.exe");
        commandLine.setExePath("steamctl");
        commandLine.addParameter("desktop");
        commandLine.addParameter("deploy");
        commandLine.addParameter("stag");
        commandLine.addParameter("--name");
        commandLine.addParameter(config.stagName);
        commandLine.addParameter("--pattern");
        commandLine.addParameter(config.stagPattern);
        commandLine.addParameter("--version");
        commandLine.addParameter(config.stagVersion);
        commandLine.addParameter("--file");
        commandLine.addParameter(cfgPath);
        if (checkBox.isSelected()) {
            commandLine.addParameter("--emulate-cluster");
        }

        ProcessHandler processHandler = null;
        try {
            processHandler = new OSProcessHandler(commandLine);
        } catch (Exception e) {
            new BaseDialogWrapper("Error", e.getMessage(), null, null).showAndGet();
            throw new RuntimeException(e);
        }
        processHandler.startNotify();
        processHandler.waitFor();

        super.doOKAction();
    }

    private String getstagDesktopDeploymentConfigFile() {
        Path stagDesktopDeploymentConfigFilePath = Paths.get(Constants.steamCtlCfgPath,
                config.stagPattern,
                config.stagVersion,
                config.stagName + "steamctlcfg",
                "desktop_deployment_config.yml"
        );

        final String stagDesktopDeploymentConfigFilePathStr = stagDesktopDeploymentConfigFilePath.toString();

        return  stagDesktopDeploymentConfigFilePathStr;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        if (path == null) {
            throw new RuntimeException("Path is null");
        }
        try {
            config = StagConfigManager.loadStagConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (config == null) {
            throw new RuntimeException("Config is null");
        }

        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
        final String stagDesktopDeploymentConfigFilePath = getstagDesktopDeploymentConfigFile();

        JPanel dialogPanel = new JPanel(new GridLayout(8, 2));
        dialogPanel.setPreferredSize(new Dimension(500, 200));

        JLabel label = new JLabel("Emulate Cluster");
        checkBox = new JCheckBox();
        JLabel label2 = new JLabel("Deployment Config");
        textField = new JTextField();
        textField.setText(stagDesktopDeploymentConfigFilePath);

//        textField.setText(isOsx ? "/Users/" + Constants.userName + "/steamworks/steamctlcfg/{stag_pattern}/{stag_version}/{stag_name}steamctlcfg/desktop_deploment_config.yml" : "c:\\users\\" + Constants.userName + "\\steamworks\\steamctlcfg\\{stag_pattern}\\{stag_version}\\{stag_name}steamctlcfg\\desktop_deployment_config.yml");

        dialogPanel.add(label);
        dialogPanel.add(checkBox);
        dialogPanel.add(label2);
        dialogPanel.add(textField);

        return dialogPanel;
    }
}