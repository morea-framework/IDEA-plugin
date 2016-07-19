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

  public static final Icon Morea = load("/icons/morea-image-16x16.png");
  public static final Icon Module = load("/icons/module-16x16.png");
  public static final Icon Assessment = load("/icons/assessment-16x16.png");
  public static final Icon Experience = load("/icons/experience-16x16.png");
  public static final Icon Outcome = load("/icons/outcome-16x16.png");
  public static final Icon Reading = load("/icons/reading-16x16.png");
  public static final Icon Prerequisite = load("/icons/prerequisite-16x16.png");

  private static Icon load(String path) {
    return IconLoader.getIcon(path, MoreaIcons.class);
  }

}
