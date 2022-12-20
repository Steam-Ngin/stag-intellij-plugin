package com.steamngin.stag;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class StagWizardStep extends ModuleWizardStep {
    StagWizardStep(StagModuleBuilder moduleBuilder, WizardContext context) {
        this.moduleBuilder = moduleBuilder;
        this.context = context;
    }

    private final StagModuleBuilder moduleBuilder;
    private final WizardContext context;
    private JTextField textField;
    private JTextField textField2;
    private ComboBox<String> comboBox;
    private ComboBox<String> comboBox2;
    private JCheckBox checkBox;

    @Override
    public JComponent getComponent() {
        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
        GridLayout gridLayout = new GridLayout(20, 1);
        JPanel mainPanel = new JPanel(gridLayout);
        textField = new JTextField();
        textField.setText("petstag");
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setText("");
            }
        });
        JLabel label = new JLabel("STAG Name (must end with 'stag'. e.g. petstag)");
        JLabel label2 = new JLabel("STAG Pattern");
        comboBox = new ComboBox<>();
        comboBox.addItem("rubicon");
//        comboBox.addItem("apollo");
        comboBox2 = new ComboBox<>();
        comboBox2.addItem("1.0.0-alpha");
//        comboBox2.addItem("1.0.0");
        JLabel label3 = new JLabel("STAG Version");
        JLabel label4 = new JLabel("STAG Path");
        textField2 = new JTextField();
        textField2.setText(Constants.stagDefaultPathStr);

//        textField2.setText(isOsx ? "/Users/" + Constants.userName + "/steamworks/stem/{stag_pattern}/{stag_version}/{stag_name}" : "C:\\Users\\" + Constants.userName + "\\steamworks\\stem\\{stag_pattern}\\{stag_version}\\{stag_name}");//TODO: CHANGE THIS TO YOUR PATH
        JLabel label6 = new JLabel("Override Config");
        checkBox = new JCheckBox();

        mainPanel.add(label);
        mainPanel.add(textField);
        mainPanel.add(label2);
        mainPanel.add(comboBox);
        mainPanel.add(label3);
        mainPanel.add(comboBox2);
        mainPanel.add(label4);
        mainPanel.add(textField2);
        mainPanel.add(label6);
        mainPanel.add(checkBox);

        return mainPanel;
    }

    private String parseStagPath(String stagPath) {
        try {
            return stagPath
                    .replace("{stag_name}", Objects.equals(textField.getText(), "Must end with 'stag'. default: petstag") ? "petstag" : textField.getText())
                    .replace("{stag_pattern}", Objects.requireNonNull(comboBox.getSelectedItem()).toString())
                    .replace("{stag_version}", Objects.requireNonNull(comboBox2.getSelectedItem()).toString());
        } catch (Exception e) {
            new BaseDialogWrapper("Error", "Invalid path pattern", null, null).showAndGet();
            throw e;
        }
    }

    private String getStagDynamicPath() {
        try {
            final String stagDynamicName = Objects.equals(textField.getText(), "petstag") ? "petstag" : textField.getText();
            final String stagDynamicPattern = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
            final String stagDynamicPatternVersion = Objects.requireNonNull(comboBox2.getSelectedItem()).toString();

            Path stagDynamicPath = Paths.get(Constants.stemPath,
                    stagDynamicPattern,
                    stagDynamicPatternVersion,
                    stagDynamicName);

            final String stagDynamicPathStr = stagDynamicPath.toString();

            return  stagDynamicPathStr;
        } catch (Exception e) {
            new BaseDialogWrapper("Error", "Invalid path pattern", null, null).showAndGet();
            throw e;
        }
    }


    public static void checkPathExistsOrCreate(String path) {
        final Path newPath = Paths.get(path);
        if (newPath.toFile().exists()) {
            return;
        }
        try {
            Files.createDirectories(newPath);
        } catch (Exception e) {
            new BaseDialogWrapper("Error", "Invalid path", null, null).showAndGet();
        }
    }

    public static boolean checkScriptExists() {
        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
        final File steamctlFile = Paths.get(isOsx ? Constants.osxScriptPath + "steamctl" : Constants.windowsScriptPath + "steamctl.exe").toFile();
//        final File scriptFile = Paths.get(isOsx ? Constants.osxScriptPath + "stag_helper.py" : Constants.windowsScriptPath + "stag_helper.py").toFile();
        return steamctlFile.exists();
    }

    @Override
    public void updateDataModel() {
//        if(!checkScriptExists()) {
//            new BaseDialogWrapper("Error", "steamctl executable or stag_helper.py not found", null, null).showAndGet();
//            throw new RuntimeException("steamctl executable or stag_helper.py not found");
//        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (!textField.getText().endsWith("stag") && !textField.getText().isEmpty()) {
            new BaseDialogWrapper("Error", "Invalid STAG name, this must end with 'stag'", null, null).showAndGet();
            throw new RuntimeException("Invalid STAG name, this must end with 'stag'");
        }
        final boolean isOsx = Objects.equals(System.getProperty("os.name"), "Mac OS X");
        final String stagName = Objects.equals(textField.getText(), "petstag") || textField.getText().isEmpty() ? "petstag" : textField.getText();
        final String stagPattern = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
        final String stagVersion = Objects.requireNonNull(comboBox2.getSelectedItem()).toString();
//        final String stagPath = parseStagPath(textField2.getText());
        final String stagPath = getStagDynamicPath();
        final boolean overrideConfig = checkBox.isSelected();

        try {
            StagConfigManager.saveStagConfig(new StagConfig(stagName, stagPattern, stagVersion, stagPath, overrideConfig));
        } catch (Exception e) {
            new BaseDialogWrapper("Error", "Invalid path", null, null).showAndGet();
            throw new RuntimeException(e);
        }

//        checkPathExistsOrCreate(stagPath);
        checkPathExistsOrCreate(Constants.workingDirPath);

        GeneralCommandLine commandLine = new GeneralCommandLine();

//        commandLine.setWorkDirectory(isOsx ? Constants.osxScriptPath : Constants.windowsScriptPath);
        commandLine.setWorkDirectory(Constants.workingDirPath);
//        commandLine.setExePath(isOsx ? "./steamctl" : "steamctl.exe");
        commandLine.setExePath("steamctl");
        commandLine.addParameter("desktop");
        commandLine.addParameter("create");
        commandLine.addParameter("stag");
        commandLine.addParameter("--name");
        commandLine.addParameter(stagName);
        commandLine.addParameter("--pattern");
        commandLine.addParameter(stagPattern);
        commandLine.addParameter("--version");
        commandLine.addParameter(stagVersion);
        commandLine.addParameter("--stag-dir");
        commandLine.addParameter(stagPath);
        if (overrideConfig) {
            commandLine.addParameter("--override-cfg");
        }

        try {
            ProcessHandler processHandler = null;
            try {
                processHandler = new OSProcessHandler(commandLine);
            } catch (ExecutionException e1) {
                new BaseDialogWrapper("Error", e1.getMessage(), null, null).showAndGet();
                e1.printStackTrace();
                throw e1;
            }
            processHandler.startNotify();
            processHandler.waitFor();

            new Thread(() -> {
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                assert robot != null;
                robot.delay(1000);
                int ctrlKeyEvt = isOsx ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;

                StringSelection stringSelection = new StringSelection(stagName);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                robot.keyPress(ctrlKeyEvt);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(ctrlKeyEvt);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                stringSelection = new StringSelection(stagPath);
                clipboard.setContents(stringSelection, null);
                robot.keyPress(ctrlKeyEvt);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(ctrlKeyEvt);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }).start();

        } catch (Exception e) {
            new BaseDialogWrapper("Error", "Failed to create STAG project", null, null).showAndGet();
        }
    }

}

