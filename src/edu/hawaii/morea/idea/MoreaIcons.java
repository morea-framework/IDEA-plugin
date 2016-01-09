package edu.hawaii.morea.idea;

/**
 * This file is a part of Morea-plugin.
 * <p/>
 * Created by Cam Moore on 1/9/16.
 * <p/>
 * Copyright (C) 2016 Cam Moore.
 * <p/>
 * The MIT License (MIT)
 * <p/>
 */

import com.intellij.openapi.util.IconLoader;

import javax.swing.Icon;

public class MoreaIcons {

  public static final Icon Meteor = load("/icons/morea-16x16.png");

  private static Icon load(String path) {
    return IconLoader.getIcon(path, MoreaIcons.class);
  }

}
