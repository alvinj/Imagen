package com.devdaily.imagen.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import com.devdaily.imagen.Imagen;
import com.devdaily.imagen.actions.AnimatedRectangularRubberBand;
import com.devdaily.imagen.actions.RectangularRubberBand;
import com.devdaily.imagen.actions.RubberBand;
import com.devdaily.imagen.actions.RubberBandCanvas;
import com.devdaily.imagen.controller.ImageUtils;
import com.sun.onlineexample.ImageSelection;

// TODO clean up code by moving logic to a controller class
public class MetaFrame extends JFrame implements RubberBandCanvas
{
  Imagen controller;

  // trying to support rubberbanding
  private RubberBand rubberband;
  
  // TODO get rid of this label
  JLabel label = new JLabel();
  ImageComponent imageComponent;

  Image originalImage;
  Image tmpImage;
  BufferedImage currentBufferedImage;

  private DropPanel dropPanel;
  // drag-drop stuff
  private DropTarget dropTarget;

  int crazyDamnCounter = 0;
  boolean ignoreResizeEvent;

  // working here to get height and width and do scaling
  float originalAspectRatio = 1;
  int currentHeight;
  int currentWidth;
  int screenHeight;
  int screenWidth;

  int tmpH, tmpW = 0;

  public MetaFrame(Imagen controller)
  {
    this.controller = controller;
    setUndecorated(true);
    setResizable(true);
    
    JPanel panel = new JPanel();
    panel.setBackground(Color.WHITE);
    dropTarget = new DropTarget(panel,new DropTargetImplementation(this, panel));
    getContentPane().add(panel, BorderLayout.CENTER);

    // TODO this isn't right; it really needs to call a shutdown method
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // from http://www.java2s.com/Open-Source/Java-Document/Swing-Library/swingfx/net/java/swingfx/rubberband/demo/Main.java.htm
    RubberBandCanvas canvas = new TransparentRubberBandPanel();
    RubberBand rb = new RectangularRubberBand(canvas, this);
    this.setGlassPane(canvas.getCanvas());
    this.getGlassPane().setVisible(true);
    
    setInitialWindowSize();
    configureEventHandlers();
  }

  /**
   * Handle the action where the user crops the image.
   * The user just drew a rectangle to select what they wanted, so I now need
   * to get the subimage from the original image, and then redisplay that subimage
   * in the same frame.
   */
  public void doCropImageAction(int x, int y, int w, int h)
  {
    // debug
//    System.err.println("ENTERED doCropImageAction");
//    System.err.format(" (x, y, h, w) = (%d, %d, %d, %d)", x, y, h, w);
    int h1 = currentBufferedImage.getHeight();
    int w1 = currentBufferedImage.getWidth();
//    System.err.format(" cbi init (h,w) = (%d, %d)", h1, w1);

    // (1) get the subimage
//    System.err.println("   currentBufferedImage was NOT null");
    currentBufferedImage = currentBufferedImage.getSubimage(x,y,w,h);
    currentHeight = currentBufferedImage.getHeight(); // should be h
    currentWidth = currentBufferedImage.getWidth();   // should be w
//    System.err.format(" cbi (currH,currW) = (%d, %d)", currentHeight, currentWidth);

    // (2) set the new image on our image component
//    imageComponent.setImage(currentBufferedImage, currentWidth, currentHeight);
//    imageComponent.invalidate();
//    imageComponent.setSize(currentWidth, currentHeight);
//    this.setSize(currentWidth, currentHeight);

    imageComponent = new ImageComponent(currentBufferedImage);
    getContentPane().removeAll();
    getContentPane().add(imageComponent, BorderLayout.CENTER);
    ignoreResizeEvent = true;
    this.setSize(currentWidth, currentHeight);
    
    // housekeeping - setting height, width, and currentBufferedImage is the right image
    updateCurrentDimensions(currentHeight, currentWidth);
    System.err.println(" ");
  }
  
  /**
   * A method to help keep the current image dimensions in sync.
   */
  private void updateCurrentDimensions(int h, int w)
  {
    currentHeight = h;
    currentWidth = w;
  }

  
  /**
   * Trying to make this a panel i can draw a rubber band on,
   * without interfering with other drag and drop stuff.
   */
  private static class TransparentRubberBandPanel 
  extends JPanel 
  implements RubberBandCanvas, ActionListener
  {
    private RubberBand rubberband;

    public TransparentRubberBandPanel()
    {
      this.setOpaque(false);
    }

    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      rubberband.draw(g);
    }

    public void setRubberBand(RubberBand rubberband)
    {
      this.rubberband = rubberband;
    }

    public JComponent getCanvas()
    {
      return this;
    }

    public void actionPerformed(ActionEvent e)
    {
    }
  }  
  
  
  private class DropTargetImplementation extends DropTargetAdapter
  {
    MetaFrame metaFrame;
    JPanel panel;
    
    public DropTargetImplementation(MetaFrame metaFrame, JPanel panel)
    {
      this.metaFrame = metaFrame;
      this.panel = panel;
    }

    public void drop(DropTargetDropEvent e)
    {
//      System.err.println("The DropPanel received the DropEvent");

      // Called when the user finishes or cancels the drag operation.
      Transferable transferable = e.getTransferable();
      try
      {
        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        {
          e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
          Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
          e.getDropTargetContext().dropComplete(true);
          if (image==null) System.err.println("Uh-oh, Image was null ...");
          metaFrame.addImage(image);
        }
        else
        {
//          System.err.println("DROP::That wasn't an image!");
          e.rejectDrop();
        }
      }
      catch (IOException ioe)
      {
        ioe.printStackTrace();
        e.rejectDrop();
      }
      catch (UnsupportedFlavorException ufe)
      {
        ufe.printStackTrace();
        e.rejectDrop();
      }
    }
    
    public void dragEnter(DropTargetDragEvent e)
    {
      // called when the user is dragging and enters our target
      panel.setBackground(Color.GREEN);
    }

    public void dragExit(DropTargetEvent e)
    {
      // called when the user is dragging and leaves our target
      panel.setBackground(Color.WHITE);
    }

    public void dragOver(DropTargetDragEvent e)
    {
      // called when the user is dragging and moves over our target
      // (i'm not sure what the difference is between this and dragEnter()?)
      panel.setBackground(Color.GREEN);
    }

  } // end of DropTargetImpl class

  private void configureEventHandlers()
  {
    configurePasteImageListener();
    configureCloseWindowListener();
    configureNewWindowListener();
    configureDecreaseSizeListener();
    configureIncreaseSizeListener();
    addComponentListener(new MyComponentAdapter(this));
  }

  private void setInitialWindowSize()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenHeight = screenSize.height;
    screenWidth = screenSize.width;
    setSize(screenWidth / 2, screenHeight / 2);
    System.err.format("Actual W, H: %s, %s\n", screenWidth, screenHeight);
  }

  private void configureCloseWindowListener()
  {
    ActionListener actionListener = new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        doCloseWindowAction();
      }
    };
    // TODO fix these keystrokes to be platform-neutral
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    //KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.META_DOWN_MASK);
    this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  private void configureDecreaseSizeListener()
  {
    ActionListener actionListener = new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        doDecreaseSizeAction();
      }
    };
    // TODO fix these keystrokes to be platform-neutral
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  private void configureIncreaseSizeListener()
  {
    ActionListener actionListener = new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        doIncreaseSizeAction();
      }
    };
    // TODO fix these keystrokes to be platform-neutral
    // doing [command][=] here because that works on my macbook air. did not work as VK_PLUS
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  private void configurePasteImageListener()
  {
    ActionListener actionListener = new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        doPasteAction();
      }
    };
    // TODO fix these keystrokes to be platform-neutral
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  private void configureNewWindowListener()
  {
    ActionListener actionListener = new ActionListener()
    {
      public void actionPerformed(ActionEvent actionEvent)
      {
        doNewWindowAction();
      }
    };
    // TODO fix these keystrokes to be platform-neutral
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  private void doNewWindowAction()
  {
    controller.doNewWindowAction();
  }

  private void doCloseWindowAction()
  {
    controller.doCloseWindowAction(this);
  }

  /* make the image 10% smaller */
  private void doDecreaseSizeAction()
  {
    int desiredHeight = (int)(0.90 * currentBufferedImage.getHeight());
    int desiredWidth = (int)(0.90 * currentBufferedImage.getWidth());
    doResizeCurrentBufferedImage(desiredHeight, desiredWidth);
  }

  /**
   * Make the image 10% larger.
   * Assumes that currentBufferedImage is the correct image to work with.
   */
  private void doIncreaseSizeAction()
  {
    int desiredHeight = (int)(1.10 * currentBufferedImage.getHeight());
    int desiredWidth = (int)(1.10 * currentBufferedImage.getWidth());
    doResizeCurrentBufferedImage(desiredHeight, desiredWidth);
  }

  /**
   * Use the given height and width to resize the currentBufferedImage
   * and then update the metaframe to be the right size.
   */
  private void doResizeCurrentBufferedImage(int desiredHeight, int desiredWidth)
  {
    BufferedImage newBI = ImageUtils.getFasterScaledInstance(currentBufferedImage, desiredWidth, desiredHeight,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
    imageComponent.setImage(newBI, (int) desiredWidth, desiredHeight);
    imageComponent.invalidate();
    imageComponent.repaint();

    // tell frameComponentResized(null) to leave us alone during setSize
    ignoreResizeEvent = true;
    setSize(desiredWidth, desiredHeight);

    // housekeeping
    currentBufferedImage = newBI;
    currentHeight = currentBufferedImage.getHeight();
    currentWidth = currentBufferedImage.getWidth();
    updateCurrentDimensions(currentHeight, currentWidth);
  }
  
  
  public void addImage(Image image)
  {
    originalImage = image;
    // convert the given image to a buffered image.
    // TODO use this instead.
    currentBufferedImage = ImageUtils.toBufferedImage(originalImage);
    currentHeight = currentBufferedImage.getHeight();
    currentWidth = currentBufferedImage.getWidth();
    //System.err.format("original image width, height: %s, %s\n", currentWidth, currentHeight);

    originalAspectRatio = (float) currentWidth / currentHeight;
    //System.err.println("aspect ratio: " + originalAspectRatio);

    // if the image height is greater than the screen height, or
    // the image width is greater than the screen width, resize
    // the image to fit
    if (currentHeight > screenHeight || currentWidth > screenWidth)
    {
      if (currentHeight > screenHeight)
        System.err.println("height is too tall");
      if (currentWidth > screenWidth)
        System.err.println("width is too wide");
      tmpH = screenHeight;
      tmpW = (int) (tmpH * originalAspectRatio);
      System.err.format("initial W, H: %s, %s\n", tmpW, tmpH);

      if (tmpW > screenWidth)
      {
        // if the new width is still too wide, use it to scale the image
        System.err.println("new image is too wide, compensating");
        tmpW = screenWidth;
        tmpH = (int) (tmpW / originalAspectRatio);
        System.err.format("second W, H: %s, %s\n", tmpW, tmpH);
      }
      tmpImage = originalImage.getScaledInstance(tmpW, tmpH, Image.SCALE_SMOOTH);
      imageComponent = new ImageComponent(tmpImage);
      setSize(tmpW, tmpH);
      updateCurrentDimensions(tmpH, tmpW);
    }
    else
    {
      imageComponent = new ImageComponent(originalImage);
      setSize(currentWidth, currentHeight);
    }

    getContentPane().removeAll();
    getContentPane().add(imageComponent, BorderLayout.CENTER);
    validate();
  }

  public void doPasteAction()
  {
    originalImage = getImageFromClipboard();

    // TODO handle this better
    if (originalImage == null)
      return;

    addImage(originalImage);
  }

  /**
   * Get an image off the system clipboard.
   * 
   * @return Returns an Image if successful; otherwise returns null.
   */
  public Image getImageFromClipboard()
  {
    Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
    if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
    {
      try
      {
        return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
      }
      catch (UnsupportedFlavorException e)
      {
        // handle this as desired
        e.printStackTrace();
      }
      catch (IOException e)
      {
        // handle this as desired
        e.printStackTrace();
      }
    }
    else
    {
      System.err.println("getImageFromClipboard::That wasn't an image!");
    }
    return null;
  }

  // /////////////////////// CODE TO HANDLE IMAGE RESIZING
  // //////////////////////////////////

  /**
   * The user is resizing the window. Based on the new AR, re-scale the image.
   */
  public void frameComponentResized(ComponentEvent e)
  {
    // this is a hack to keep this event from running when i don't
    // want it to run
    if (ignoreResizeEvent)
    {
      ignoreResizeEvent = false;
      return;
    }
    System.err.println("*** Entered frameComponentResized");

    if (crazyDamnCounter++ < 2)
      return;
    // if (isInitialEvent) return;
    System.err.println("*** proceeding with frameComponentResized");

    Dimension d = this.getSize();
    double newH = d.getHeight();
    double newW = d.getWidth();
    double newAR = newW / newH;
    if (newAR > originalAspectRatio)
    {
      // scale w from h
      newW = newH * originalAspectRatio;
    }
    else
    {
      // scale h from w
      newH = newW / originalAspectRatio;
    }

    BufferedImage newBI = ImageUtils.getFasterScaledInstance(currentBufferedImage, (int) newW, (int) newH,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
    imageComponent.setImage(newBI, (int) newW, (int) newH);
    updateCurrentDimensions((int) newH, (int) newW);
    imageComponent.validate();
    currentBufferedImage = newBI;
  }

  
  @Override
  public JComponent getCanvas()
  {
    // TODO guessing about this
    return this.getLayeredPane();
  }

  @Override
  public void setRubberBand(RubberBand rubberband)
  {
    this.rubberband = rubberband;
  }

} // end of MetaFrame

class MyComponentAdapter extends ComponentAdapter
{
  private MetaFrame adaptee;

  MyComponentAdapter(MetaFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void componentResized(ComponentEvent e)
  {
    adaptee.frameComponentResized(e);
  }
}
