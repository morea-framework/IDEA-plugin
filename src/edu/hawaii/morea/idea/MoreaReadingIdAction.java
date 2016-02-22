package edu.hawaii.morea.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This file is a part of Morea-plugin.
 * <p/>
 * Created by Cam Moore on 1/31/16.
 * <p/>
 * Copyright (C) 2016 Cam Moore.
 * <p/>
 */
public class MoreaReadingIdAction extends AnAction {
  @Override
  public void actionPerformed(AnActionEvent anActionEvent) {
    //Get all the required data from data keys
    final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
    final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
    //Access document, caret, and selection
    final Document document = editor.getDocument();
    final SelectionModel selectionModel = editor.getSelectionModel();
    final int start = selectionModel.getSelectionStart();
    final int end = selectionModel.getSelectionEnd();
    List<String> ids = MoreaFileTemplateUtil.getReadingIds(project);
    System.out.println(ids);
    DialogBuilder builder = new DialogBuilder(project);

  }

  @Override
  public void update(final AnActionEvent e) {
    //Get required data keys
    final Project project = e.getData(CommonDataKeys.PROJECT);
    final Editor editor = e.getData(CommonDataKeys.EDITOR);
    //Set visibility only in case of existing project and editor
    e.getPresentation().setVisible((project != null && editor != null));
  }
}
