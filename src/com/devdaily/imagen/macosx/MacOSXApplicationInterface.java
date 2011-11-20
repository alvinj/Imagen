package com.devdaily.imagen.macosx;

import com.apple.eawt.ApplicationEvent;

/**
 * The Mac OS X "Application" class requires us to handle About, Preferences, and Quit.
 */
public interface MacOSXApplicationInterface
{
  public void doAboutAction();
  public void doPreferencesAction();
  public void doQuitAction();
}
