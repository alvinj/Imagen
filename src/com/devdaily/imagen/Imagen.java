package com.devdaily.imagen;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.apple.eawt.Application;
import com.apple.eawt.ApplicationEvent;
import com.devdaily.imagen.macosx.MacOSXApplicationInterface;
import com.devdaily.imagen.macosx.MacOSXApplicationAdapter;
import com.devdaily.imagen.view.MetaFrame;
import com.devdaily.swing.DragWindowListener;

// TODO let user drag image into existing image
// TODO need a help screen (for keystrokes)
// TODO set up menus and menu items (paste, new window, help)
// TODO show image size and scale ratio in the titlebar
// TODO let user copy to clipboard
// TODO let user save to desktop or any folder
// TODO i probably don't handle really large initial images well (scale to screen size)

// DONE resize immediately after cropping does not work right
// DONE need an icon
// DONE fix cropping bug (aspect ratio)
// DONE let users resize windows with + and -
// DONE allow frames to be resizable
// DONE can't drag and drop onto that first window
// DONE close one window without closing app
// DONE use ALT to drag windows
// DONE get rid of window adornments
// DONE handle "quit" action
// DONE show images without using a jlabel

// TODO handle "about" action
// TODO handle images from Firefox (not currently wkg; turns green, no image)
// TODO what to do when an image is dragged into an existing image
// TODO make sure i'm not doing anything to hold onto temporary images i get from safari
// TODO image hotkey: maximize to full screen
// TODO image hotkey: go to image "natural" size
// TODO let user drag image by clicking anywhere on the window
// TODO "+" and "-" image sizing
// TODO create "spread" functionality like the F8 or F9 key
// TODO slide show

public class Imagen implements MacOSXApplicationInterface
{
  // this controller is aware that we have, or will have, a list of (j)frames
  List frames = new LinkedList();
  static Imagen imageRotator;

  public static void main(String[] args)
  {
    System.setProperty("apple.awt.graphics.EnableQ2DX","true");

    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          imageRotator = new Imagen();
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
        }
      }
    });
  }

  // display one frame when the application is first started
  public Imagen()
  {
    // sets up the mac dock event stuff
    // TODO replace this with the singleton approach (Application.getApplication())?
    Application theApplication = Application.getApplication();
    DockBarAdapter dockBarAdapter = new DockBarAdapter(this);
    theApplication.addApplicationListener(dockBarAdapter);

    // handle about, preferences, and quit
    MacOSXApplicationAdapter macAdapter = new MacOSXApplicationAdapter(this);
    theApplication.addApplicationListener(macAdapter);
    
    // must enable the preferences option manually
    //theApplication.setEnabledPreferencesMenu(true);

    MetaFrame frame1 = createNewFrameAction();
    frame1.setVisible(false);
  }
  
  private MetaFrame createNewFrameAction()
  {
    final MetaFrame metaFrame = new MetaFrame(imageRotator);
    frames.add(metaFrame);
    metaFrame.setLocationRelativeTo(null);
    metaFrame.setVisible(true);
    return metaFrame;
  }
  
  private void printNumberOfFrames()
  {
    System.err.println("Current # of frames: " + frames.size());
  }
  
  public void doCloseWindowAction(MetaFrame frame)
  {
    frame.dispose();
    frames.remove(frame);
    if (frames.size()==0) 
    {
      JOptionPane.showMessageDialog(null, "down to zero frames, i'm quitting");
      System.exit(0);
    }
  }

  /**
   * Create a new window, and add it to our list of known windows (frames).
   */
  public void doNewWindowAction()
  {
    createNewFrameAction();
  }
  
  /**
   * This method handles the callback from the DockBarAdapter.
   */
  public void handleOpenFileEvent(final ApplicationEvent applicationEvent)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        // create a new frame, and add an image to it
        MetaFrame currentFrame = createNewFrameAction();
        Image i = loadImageFile(applicationEvent.getFilename());
        currentFrame.addImage(i);
      }
    });
  }
  
  private Image loadImageFile(String filename)
  {
    BufferedImage image = null;
    try
    {
      image = ImageIO.read(new File(filename));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return image;

  }

  public void doAboutAction()
  {
    // TODO implement (or not)
  }

  public void doPreferencesAction()
  {
    // TODO implement (or not)
    // TODO if you implement this, you need to un-comment the "setEnabledPreferencesMenu" line in the main method
    //JOptionPane.showMessageDialog(null, "Sorry, no preferences at this time.");
  }

  public void doQuitAction()
  {
    // TODO fix this to close things gracefully. may also be running into another "quit" method.
    System.exit(0);
    
  }

}








