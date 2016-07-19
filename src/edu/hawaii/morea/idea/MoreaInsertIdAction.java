package edu.hawaii.morea.idea;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * This file is a part of Morea-plugin.
 * <p>
 * Created by Cam Moore on 7/19/16.
 * <p>
 * Copyright (C) 2016 Cam Moore.
 * <p>
 * The MIT License (MIT)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy , modify, merge, publish, deistribute, sublicense, and/or sell
 * copies of the Software, and to permit person to whom the Software is
 * furnished to do so, subject to the following condtions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHOERS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISIGN FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */
public class MoreaInsertIdAction extends AnAction {
  @NonNls
  private static final String DIMENSION_SERVICE_KEY = "readingsId";

  private Application application;
  private Project project;
  private Editor editor;
  private CaretModel caretModel;
  private Document document;
  private JBPopup typePopup;

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    application = ApplicationManager.getApplication();
    if (application == null) {
      return;
    }
    project = anActionEvent.getProject();
    if (project == null) {
      return;
    }
    editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    caretModel = editor.getCaretModel();
    if (caretModel == null) {
      return;
    }
    document = editor.getDocument();
    if (document == null) {
      return;
    }
    final int offset = caretModel.getOffset();
    if (typePopup != null && typePopup.isVisible()) {
      typePopup.cancel();
      return;
    }
    final JBList list = new JBList();
    list.setListData(ArrayUtil.toObjectArray(MoreaFileTemplateUtil.getMoreaTypes()));
    typePopup = JBPopupFactory.getInstance().createListPopupBuilder(list)
        .setTitle("Select Id Type")
        .setResizable(true)
        .setMinSize(new Dimension(100, 150))
        .setDimensionServiceKey(".Select " + DIMENSION_SERVICE_KEY)
        .setItemChoosenCallback(new GetIdsRunnable(list, anActionEvent.getDataContext()))
        .createPopup();
    typePopup.showInBestPositionFor(anActionEvent.getDataContext());
  }

  private class GetIdsRunnable implements Runnable {
    private JBList types;
    private DataContext dataContext;
    public GetIdsRunnable(JBList types, DataContext dataContext) {
      this.types = types;
      this.dataContext = dataContext;
    }
    @Override
    public void run() {
      String type = types.getSelectedValue().toString();
      JBList list = new JBList();
      list.setListData(ArrayUtil.toObjectArray(MoreaFileTemplateUtil.getIds(project, type)));
      int prefHeight = list.getItemsCount() * 35;
      if (prefHeight > 150) {
        prefHeight = 150;
      }
      JBPopup idsPopup = JBPopupFactory.getInstance().createListPopupBuilder(list)
          .setTitle("Select id")
          .setResizable(true)
          .setMinSize(new Dimension(200, prefHeight))
          .setDimensionServiceKey(".Select id." + DIMENSION_SERVICE_KEY)
          .setItemChoosenCallback(new Runnable() {
            @Override
            public void run() {
              int offset = caretModel.getOffset();
              String value = list.getSelectedValue().toString();
              String insertStr = "\n  - " + value;
              ApplicationManager.getApplication().runWriteAction(new InsertIdRunnable(offset, insertStr));
            }
          })
          .createPopup();
      idsPopup.showInBestPositionFor(dataContext);
    }
  }
  private class InsertIdRunnable implements Runnable {
    private int offset;
    private String id;
    public InsertIdRunnable(int offset, String id) {
      this.offset = offset;
      this.id = id;
    }
    @Override
    public void run() {
      CommandProcessor.getInstance().executeCommand(project, new Runnable() {
        public void run() {
          document.insertString(offset, id);
          caretModel.moveToOffset(offset + id.length() );
        }
      }, DIMENSION_SERVICE_KEY, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
    }
  }
}
