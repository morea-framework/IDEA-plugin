package edu.hawaii.morea.idea.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.Configurable.NoScroll;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;


import javax.swing.*;
import java.awt.*;

/**
 * This file is a part of Morea-plugin.
 * <p/>
 * Created by Cam Moore on 1/10/16.
 * <p/>
 * Copyright (C) 2016 Cam Moore.
 * <p/>
 */
public class MoreaTemplateConfigurable implements Configurable, NoScroll {
  public static final String ID = "settings.morea.template";
  private JBCheckBox myAskForFileCreation;
  private MoreaTemplateSettings mySettings;
  private Project myProject;

  public MoreaTemplateConfigurable(Project project) {
    this.myProject = project;
  }


  @Nullable
  @Override
  public JComponent createComponent() {
    this.mySettings = MoreaTemplateSettings.getInstance();
    this.myAskForFileCreation = new JBCheckBox("Ask for file creation if it's not found when toggling between JS|HTML");
    FormBuilder builder = FormBuilder.createFormBuilder();
    //builder.addLabeledComponent(MeteorBundle.message("settings.meteor.configurable.executable", new Object[0]), this.myExecutablePathField);
    builder.addComponent(this.myAskForFileCreation);


    JPanel panel1 = builder.getPanel();
    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.add(panel1, "North");
    return wrapper;
  }

  @Override
  public boolean isModified() {
    return this.myAskForFileCreation.isSelected() != this.mySettings.ASK_FOR_FILE_CREATION;
  }

  @Override
  public void apply() throws ConfigurationException {
    if(this.mySettings != null) {
      this.mySettings.ASK_FOR_FILE_CREATION = this.myAskForFileCreation.isSelected();
    }
  }

  @Override
  public void reset() {
    if(this.mySettings != null) {
      this.myAskForFileCreation.setSelected(this.mySettings.ASK_FOR_FILE_CREATION);
    }
  }

  @Override
  public void disposeUIResources() {

  }

  @Nls
  @Override
  public String getDisplayName() {
    return "Morea Template";
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return "settings.morea";
  }
}
