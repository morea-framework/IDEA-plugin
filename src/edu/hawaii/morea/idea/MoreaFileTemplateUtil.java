package edu.hawaii.morea.idea;

import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.wm.impl.content.MoreIcon;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.HashMap;
import com.sun.org.apache.regexp.internal.RE;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  public final static String MODULE = "module";
  public final static String OUTCOME = "outcome";
  public final static String READING = "reading";
  public final static String EXPERIENCE = "experience";
  public final static String ASSESSMENT = "assessment";
  public final static String PREREQUISITE = "prerequisite";

  private static Map<String, List<String>> moreaIds;

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
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getDefaultInstance().getInternalTemplates(),
        filter));
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getDefaultInstance().getAllTemplates(), filter));
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
      return MoreaIcons.Assessment;
    } else if (name.startsWith("EXPERIENCE")) {
      return MoreaIcons.Experience;
    } else if (name.startsWith("MODULE")) {
      return MoreaIcons.Module;
    } else if (name.startsWith("OUTCOME")) {
      return MoreaIcons.Outcome;
    } else if (name.startsWith("READING")) {
      return MoreaIcons.Reading;
    } else if (name.startsWith("PREREQUISITE")) {
      return MoreaIcons.Prerequisite;
    }
    return MoreaIcons.Morea;
  }

  public static PsiFile[] getMoreaFiles(Project project) {
    PsiSearchHelper psiSearchHelper = PsiSearchHelper.SERVICE.getInstance(project);
    return psiSearchHelper.findFilesWithPlainTextWords("morea_id");
  }

  public static ArrayList<String> getMoreaIds(Project project) {
    ArrayList<String> ret = new ArrayList<String>();
    PsiFile[] moreaFiles = getMoreaFiles(project);
    for (int i = 0; i < moreaFiles.length; i++) {
      String id = getMoreaId(moreaFiles[i]);
      if (id != null) {
        ret.add(id);
      }
    }
    return ret;
  }

  private static String getMoreaId(PsiFile psiFile) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(psiFile.getVirtualFile().getInputStream()));
      String line = reader.readLine();
      while (line != null && !line.startsWith("morea_id")) {
        line = reader.readLine();
      }
      if (line != null) {
        return line.substring(line.indexOf(' ') + 1);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String getMoreaType(PsiFile psiFile) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(psiFile.getVirtualFile().getInputStream()));
      String line = reader.readLine();
      while (line != null && !line.startsWith("morea_type")) {
        line = reader.readLine();
      }
      if (line != null) {
        return line.substring(line.indexOf(' ') + 1);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void buildIds(Project project) {
    PsiFile[] moreaFiles = getMoreaFiles(project);
    if (moreaIds == null) {
      moreaIds = new HashMap<String, List<String>>();
    }
    for (int i = 0; i < moreaFiles.length; i++) {
      String id = getMoreaId(moreaFiles[i]);
      String type = getMoreaType(moreaFiles[i]);
      List<String> ids = moreaIds.get(type);
      if (ids == null) {
        ids = new ArrayList<String>();
        moreaIds.put(type, ids);
      }
      if (!ids.contains(id)) {
        ids.add(id);
      }
    }
  }

  public static List<String> getReadingIds(Project project) {
    buildIds(project);
    return moreaIds.get(READING);
  }

  public static List<String> getModuleIds(Project project) {
    buildIds(project);
    return moreaIds.get(MODULE);
  }

  /**
   * Returns the Morea Ids for the given type in the given project.
   *
   * @param project the project to search in.
   * @param type    One of the following MODULE, OUTCOME, READING, EXERCISE, ASSESSMENT, or PREREQUISITE.
   * @return A list of the ids as Strings.
   */
  public static List<String> getIds(Project project, String type) {
    buildIds(project);
    return moreaIds.get(type);
  }

  public static List<String> getMoreaTypes() {
    List<String> retVal = new ArrayList<String>();
    retVal.add(MODULE);
    retVal.add(ASSESSMENT);
    retVal.add(EXPERIENCE);
    retVal.add(OUTCOME);
    retVal.add(READING);
    retVal.add(PREREQUISITE);
    return retVal;
  }
}
