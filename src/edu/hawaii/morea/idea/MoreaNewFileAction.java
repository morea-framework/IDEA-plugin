package edu.hawaii.morea.idea;

/**
 * This file is a part of Morea-plugin.
 * <p/>
 * Created by Cam Moore on 1/9/16.
 * <p/>
 * Copyright (C) 2016 Cam Moore.
 * <p/>
 */

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;

/**
 * @author Cam Moore
 */
public class MoreaNewFileAction extends CreateFileFromTemplateAction {
  private static final String NEW_MOREA_FILE = "New Morea file";

  public MoreaNewFileAction() {
    super("Create new Morea File", "Morea files",
        MoreaIcons.Morea);
  }
  @Override
  protected void buildDialog(Project project, PsiDirectory psiDirectory, CreateFileFromTemplateDialog.Builder builder) {
    builder.setTitle("Add Morea File");
    for (FileTemplate fileTemplate : MoreaFileTemplateUtil.getMoreaTemplates()) {
      final String templateName = fileTemplate.getName();
      final String shortName = MoreaFileTemplateUtil.getTemplateShortName(templateName);
      builder.addKind(shortName, MoreaFileTemplateUtil.getTemplateIcon(shortName), templateName);
    }
  }

  @Override
  protected String getActionName(PsiDirectory psiDirectory, String s, String s1) {
    return NEW_MOREA_FILE;
  }
}
