package com.devdaily.imagen.macosx;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

public class MacOSXApplicationAdapter extends ApplicationAdapter
{
  private MacOSXApplicationInterface handler;
  
  public MacOSXApplicationAdapter(MacOSXApplicationInterface handler)
  {
    this.handler = handler;
  }

  public void handleQuit(ApplicationEvent e)
  {
    handler.doQuitAction();
  }

  public void handlePreferences(ApplicationEvent e)
  {
    handler.doPreferencesAction();
  }

  public void handleAbout(ApplicationEvent e)
  {
    // tell the system we're handling this, so it won't display
    // the default system "about" dialog after ours is shown.
    
    //e.setHandled(true);
    //handler.doAboutAction();
  }
}












