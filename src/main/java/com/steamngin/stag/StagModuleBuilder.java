package com.steamngin.stag;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
public class StagModuleBuilder extends ModuleBuilder {

    @Override
    public void setupRootModel(@NotNull ModifiableRootModel model) {
    }

    @Override
    public StagModuleType getModuleType() {
        return StagModuleType.getInstance();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }


    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        return new StagWizardStep(this, context);
    }
}