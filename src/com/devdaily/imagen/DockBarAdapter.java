package com.devdaily.imagen;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

/**
 * This class helps implement the "drag to dock" behavior.
 * I don't necessarily need that for this application, but I tested
 * it here, because you have to build the application with the
 * JarBundler build process (to get the DocumentType settings
 * you need in the info.plist file).
 * 
 * @author alvin alexander, devdaily.com.
 * @see http://devworld.apple.com/documentation/Java/Reference/1.5.0/appledoc/api/index.html
 *
 */
public class DockBarAdapter extends ApplicationAdapter
{
  private Imagen handler;
  
  public DockBarAdapter(Imagen handler)
  {
    this.handler = handler;
  }
  public void handleOpenFile(ApplicationEvent e)
  {
    handler.handleOpenFileEvent(e);
  }
}
