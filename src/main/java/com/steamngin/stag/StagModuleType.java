package com.steamngin.stag;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class StagModuleType extends ModuleType<StagModuleBuilder> {

    private static final String ID = "DEMO_MODULE_TYPE";

    public StagModuleType() {
        super(ID);
    }

    public static StagModuleType getInstance() {
        return (StagModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public StagModuleBuilder createModuleBuilder() {
        return new StagModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "STAG";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Stag wizard";
    }

    @NotNull
    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return AllIcons.Ide.Readwrite;
    }

    @Override
    public ModuleWizardStep @NotNull [] createWizardSteps(@NotNull WizardContext wizardContext,
                                                          @NotNull StagModuleBuilder moduleBuilder,
                                                          @NotNull ModulesProvider modulesProvider) {
        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
    }

}