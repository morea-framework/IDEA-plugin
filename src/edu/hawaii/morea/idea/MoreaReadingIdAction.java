package edu.hawaii.morea.idea;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.DimensionService;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.components.JBList;
import com.intellij.ui.popup.util.*;
import com.intellij.util.ArrayUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
public class MoreaReadingIdAction extends AnAction implements DumbAware, MasterDetailPopupBuilder.Delegate {
    @NonNls
    private static final String DIMENSION_SERVICE_KEY = "readingsId";


    private JBPopup myPopup;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//    //Get all the required data from data keys
//    final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
//    final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
//    //Access document, caret, and selection
//    final Document document = editor.getDocument();
//    final SelectionModel selectionModel = editor.getSelectionModel();
//    final int start = selectionModel.getSelectionStart();
//    final int end = selectionModel.getSelectionEnd();
//    List<String> ids = MoreaFileTemplateUtil.getReadingIds(project);
//    System.out.println(ids);
////    ActionManager.getInstance().createActionPopupMenu(CommonDataKeys.EDITOR, )

        final Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        final CaretModel caretModel = editor.getCaretModel();
        final int offset = caretModel.getOffset();
        final Document document = editor.getDocument();
        if (myPopup != null && myPopup.isVisible()) {
            myPopup.cancel();
            return;
        }
        final Application application = ApplicationManager.getApplication();
        final JBList list = new JBList();
        list.setListData(ArrayUtil.toObjectArray(MoreaFileTemplateUtil.getIds(project, MoreaFileTemplateUtil.READING)));
        myPopup = JBPopupFactory.getInstance().createListPopupBuilder(list)
                .setTitle("Select reading id")
                .setResizable(true)
                .setDimensionServiceKey(".Select " + DIMENSION_SERVICE_KEY)
                .setItemChoosenCallback(new Runnable() {
                    @Override
                    public void run() {
                        String value = list.getSelectedValue().toString();
                        String insertStr = "\n  - " + value;
                        ApplicationManager.getApplication().runWriteAction(new InsertIdRunnable(project, document,
                                caretModel, offset, insertStr));
                    }
                })
                .createPopup();
        final Point location = DimensionService.getInstance().getLocation(DIMENSION_SERVICE_KEY, project);
        if (location != null) {
            myPopup.showInScreenCoordinates(WindowManager.getInstance().getFrame(project), location);
        } else {
            myPopup.showCenteredInCurrentWindow(project);
        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabled(e.getProject() != null);
    }

    @Nullable
    @Override
    public String getTitle() {
        return "Reading Ids";
    }

    @Override
    public void handleMnemonic(KeyEvent e, Project project, JBPopup popup) {

    }

    @Nullable
    @Override
    public JComponent createAccessoryView(Project project) {
        return null;
    }

    @Override
    public Object[] getSelectedItemsInTree() {
        return new Object[0];
    }

    @Override
    public void itemChosen(ItemWrapper item, Project project, JBPopup popup, boolean withEnterOrDoubleClick) {

    }

    @Override
    public void removeSelectedItemsInTree() {

    }

    private static DefaultListModel buildModel(@NotNull Project project) {
        DefaultListModel model = new DefaultListModel();
        List<String> ids = MoreaFileTemplateUtil.getReadingIds(project);
        for (String id : ids) {
            model.addElement(id);
        }
        return model;
    }

    private class InsertIdRunnable implements Runnable {
        private Project project;
        private Document document;
        private CaretModel caretModel;
        private int offset;
        private String id;
        public InsertIdRunnable(Project project, Document document, CaretModel caretModel, int offset, String id) {
            this.project = project;
            this.document = document;
            this.caretModel = caretModel;
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
