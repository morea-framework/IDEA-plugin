package edu.hawaii.morea.idea.settings;

/**
 * This file is a part of Morea-plugin.
 * <p/>
 * Created by Cam Moore on 1/10/16.
 * <p/>
 * Copyright (C) 2016 Cam Moore.
 * <p/>
 */

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;
@State(
    name = "MoreaTemplateSettings",
    storages = {
        @Storage(
            file = StoragePathMacros.APP_CONFIG + "/other.xml"
        )}
)
public class MoreaTemplateSettings implements PersistentStateComponent<MoreaTemplateSettings> {
  public boolean ASK_FOR_FILE_CREATION = true;

  public static MoreaTemplateSettings getInstance() {
    return ServiceManager.getService(MoreaTemplateSettings.class);
  }

  @Nullable
  @Override
  public MoreaTemplateSettings getState() {
    return this;
  }

  @Override
  public void loadState(MoreaTemplateSettings moreaTemplateSettings) {
    XmlSerializerUtil.copyBean(moreaTemplateSettings, this);
  }
}
