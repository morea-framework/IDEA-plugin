package edu.hawaii.morea.idea;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.util.Condition;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

/**
 * This file is a part of Morea-plugin.
 * <p/>
 * Created by Cam Moore on 1/9/16.
 * <p/>
 * Copyright (C) 2016 Cam Moore.
 * <p/>
 */
public class MoreaFileTemplateUtil {
  private final static String MOREA_TEMPLATE_PREFIX = "Morea ";

  public static List<FileTemplate> getMoreaTemplates() {
    return getApplicableTemplates(new Condition<FileTemplate>() {

      @Override
      public boolean value(FileTemplate fileTemplate) {
        return fileTemplate.getName().startsWith(MOREA_TEMPLATE_PREFIX);
      }
    });
  }

  public static List<FileTemplate> getApplicableTemplates(Condition<FileTemplate> filter) {
    List<FileTemplate> applicableTemplates = new SmartList<FileTemplate>();
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getInternalTemplates(),
        filter));
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getAllTemplates(), filter));
    return applicableTemplates;
  }

  public static String getTemplateShortName(String templateName) {
    if (templateName.startsWith(MOREA_TEMPLATE_PREFIX)) {
      return templateName.substring(MOREA_TEMPLATE_PREFIX.length());
    }
    return templateName;
  }

  @NotNull
  public static Icon getTemplateIcon(String name) {
    if (name.startsWith("ASSESSMENT")) {
      return AllIcons.FileTypes.Manifest;
    }
    else if (name.startsWith("EXPERIENCE")) {
      return AllIcons.FileTypes.Custom;
    }
    else if (name.startsWith("MODULE")) {
      return AllIcons.FileTypes.Json;
    }
    else if (name.startsWith("OUTCOME")) {
      return AllIcons.FileTypes.Archive;
    }
    else if (name.startsWith("READING")) {
      return AllIcons.FileTypes.Text;
    }
    return MoreaIcons.Morea;
  }

}
