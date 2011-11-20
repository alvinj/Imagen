package com.devdaily.imagen.actions;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.devdaily.imagen.view.MetaFrame;

/**
 * An abstract implementation of {@link RubberBand} which handles the basic
 * drawing/setup of the rubber band.
 * 
 * @author rwickesser
 * @since 1.0 $Revision: 1.2 $
 */
public abstract class AbstractRubberBand extends MouseInputAdapter implements
    RubberBand
{
  /** the canvas where the rubber band will be drawn onto */
  protected RubberBandCanvas canvas;

  /** maintains the size and location of the rubber band */
  protected Rectangle rubberband;

  /** stores the x coordinate of the mouse pressed event */
  private int pressX;
  /** stores the y coordinate of the mouse pressed event */
  private int pressY;

  /**
   * if <code>true</code> then the rubber band will disappear after the mouse is
   * released, otherwise the rubber band stays visible until a new rubber band
   * is drawn. Subclasses should override
   */
  private boolean hideOnRelease;
  
  /**
   * the underlying jframe or jwindow 
   */
  protected MetaFrame metaFrame;
  Point startDragPoint;
  Point startLocPoint;
  
  // a list of the mouse history, used for "throwing" a window
  private List<MouseHistory> mouseHistory = new ArrayList<MouseHistory>();

  /**
   * Creates a new <code>RubberBand</code> and sets the canvas
   * 
   * @param canvas
   *          the <code>RubberBandCanvas</code> on which the rubber band will be
   *          drawn
   * 
   * @see #setCanvas(RubberBandCanvas)
   */
  public AbstractRubberBand(RubberBandCanvas canvas, MetaFrame ourWindow)
  {
    this.canvas = canvas;
    this.metaFrame = ourWindow;
    init();
  }

  /**
   * Initializes the rubber band
   */
  private void init()
  {
    rubberband = new Rectangle();
    setHideOnRelease(true);

    if (canvas != null)
    {
      canvas.setRubberBand(this);
      addMouseListeners();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see gui.rubberband.RubberBand#addMouseListeners()
   */
  public void addMouseListeners()
  {
    canvas.getCanvas().addMouseListener(this);
    canvas.getCanvas().addMouseMotionListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.swingfx.rubberband.RubberBand#getBounds()
   */
  public Rectangle getBounds()
  {
    return rubberband.getBounds();
  }

  /*
   * (non-Javadoc)
   * 
   * @see gui.rubberband.RubberBand#setCanvas(RubberBandCanvas)
   */
  public void setCanvas(RubberBandCanvas canvas)
  {
    this.canvas = canvas;
    this.canvas.setRubberBand(this);
    addMouseListeners();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.event.MouseInputAdapter#mouseDragged(java.awt.event.MouseEvent)
   */
  public void mouseDragged(MouseEvent e)
  {
    // aja: only work when [command] key is down
    if (e.isMetaDown())
    {
      mouseDraggedMetaDown(e);
    }
    else if (e.isAltDown())
    {
      // build a list of the mouse history
      mouseHistory.add(new MouseHistory(e.getX(), e.getY()));
      // want to track the mouse history so the user can 'throw' the window
      System.err.format("(x,y) = (%d, %d)\n", e.getX(), e.getY());
      mouseDraggedNoMetaKeys(e);
    }
    else
    {
      mouseDraggedNoMetaKeys(e);
    }
  }

  /**
   * When the mouse is dragged with no meta keys held down, move the jframe.
   */
  private void mouseDraggedNoMetaKeys(MouseEvent e)
  {
    // want to move the jframe
    Point current = this.getScreenLocation(e);
    Point offset = new Point((int) current.getX() - (int) startDragPoint.getX(), (int) current.getY() - (int) startDragPoint.getY());
    Point new_location = new Point((int) (this.startLocPoint.getX() + offset.getX()), (int) (this.startLocPoint.getY() + offset.getY()));
    metaFrame.setLocation(new_location);
  }

  /**
   * When the mouse is dragged with the meta key down, do the image crop action.
   */
  private void mouseDraggedMetaDown(MouseEvent e)
  {
    updateRubberBand(e);

    int x = e.getX();
    int y = e.getY();
    int w = 0;
    int h = 0;

    // adjust x and width
    if (pressX < x)
    {
      int tmp = x;
      x = pressX;
      w = tmp - x;
    } else
    {
      w = pressX - x;
    }

    // adjust y and height
    if (pressY < y)
    {
      int tmp = y;
      y = pressY;
      h = tmp - y;
    } else
    {
      h = pressY - y;
    }

    // update rubber band size and location
    update(x, y, w, h);

    // repaint the canvas so the rubber band is updated visually
    updateCanvas();
  }
  
  
  Point getScreenLocation(MouseEvent e)
  {
    Point cursor = e.getPoint();
    Point targetLocation = metaFrame.getLocationOnScreen();
    return new Point((int) (targetLocation.getX() + cursor.getX()), (int) (targetLocation.getY() + cursor.getY()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.event.MouseInputAdapter#mousePressed(java.awt.event.MouseEvent)
   */
  public void mousePressed(MouseEvent e)
  {
    // aja: only work when [command] key is down
    if (e.isMetaDown())
    {
      startRubberBand(e);
      pressX = e.getX();
      pressY = e.getY();

      // update rubber band size and location
      update(pressX, pressY, 0, 0);
    }
    else if (e.isAltDown())
    {
      System.err.println("clearing mouse history");
      // user wants to "throw" the window, make sure history is clear
      mouseHistory.clear();
    }
    else
    {
      this.startDragPoint = this.getScreenLocation(e);
      this.startLocPoint = metaFrame.getLocation();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * javax.swing.event.MouseInputAdapter#mouseReleased(java.awt.event.MouseEvent
   * )
   */
  public void mouseReleased(MouseEvent e)
  {
    if (e.isMetaDown())
    {
      stopRubberBand(e);

      // erase the rubber band if hideOnRelease is true
      if (isHideOnRelease())
      {
        // update rubber band size and location
        update(-1, -1, 0, 0);

        // repaint the canvas so the rubber band disappears
        updateCanvas();
      }

      // TODO this assumes direction is always from UL corner to LR corner
      int w = e.getX() - pressX;
      int h = e.getY() - pressY;

      // TODO i really don't want to do this here, i want to do it
      //      when the user presses the magic keystroke, but right now
      //      i really want to quit working on this.
      metaFrame.doCropImageAction(pressX, pressY, w, h);
    }
    else if (e.isAltDown())
    {
      // TODO do the action of "throwing" the window across the screen
      System.err.format("# mouse history items = %d\n", mouseHistory.size());
      for (int i=0; i<mouseHistory.size(); i++)
      {
        System.err.format(" %d: %f3.0, %f3.0, %d\n", i, mouseHistory.get(i).getPoint().getX(), mouseHistory.get(i).getPoint().getY(), mouseHistory.get(i).getTime() );
      }
      
      // use the last 25% of the mouse history to determine the slope and velocity
      int n = mouseHistory.size();
      int a = (int)(n * 0.75);  // if n==100, get me #75
      MouseHistory p1 = mouseHistory.get(0);
      MouseHistory p2 = mouseHistory.get(n-1);
      
      // assume i'm throwing from left to right
      int xLast = (int)p2.getPoint().getX();
      int yLast = (int)p2.getPoint().getY();
      double deltaX = (int) (p2.getPoint().getX() - p1.getPoint().getX());  
      double deltaY = (int) (p2.getPoint().getY() - p1.getPoint().getY());
      long deltaT = p2.getTime() - p1.getTime();
//      double distance = Math.sqrt(deltaX^2 + deltaY^2);
//      float velocity = (float) (distance / deltaT);  // (pixels / millisecond)
      double speedX = (deltaX / deltaT);
      double speedY = (deltaY / deltaT);
      
      // the frame's coordinates
      xLast = metaFrame.getX();
      yLast = metaFrame.getY();

      System.err.format("    deltaT = %d\n", deltaT);
      System.err.format(" last(x,y) = %d, %d\n", xLast, yLast);
      System.err.format("delta(x,y) = %f, %f\n", deltaX, deltaY);
      System.err.format("speed(x,y) = %f, %f\n", speedX, speedY);
      
      // animate the frame moving for the desired time
      long sleepTime = 20;
      int numSteps = 50;
      for (int i=0; i<numSteps; i++)
      {
        xLast = (int) (xLast + speedX * sleepTime);
        yLast = (int) (yLast + speedY * sleepTime);
        metaFrame.setLocation(xLast, yLast);
        sleep(sleepTime);
      }
    }
  }
  
  private void sleep (long millis)
  {
    try
    {
      Thread.sleep(millis);
    } catch (InterruptedException e)
    {
      // ignore
    }
  }

  /**
   * Makes a call to the canvas' repaint method
   */
  private void updateCanvas()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        canvas.getCanvas().repaint();
        canvas.getCanvas().invalidate();
      }
    });
  }

  /**
   * Sets whether the rubber band should disappear when the mouse is released or
   * not
   * 
   * @param hideOnRelease
   *          if <code>true</code> the rubber band will disappear when the mouse
   *          is released, if <code>false</code> the rubber band will remain
   *          visible until a new rubber band is created
   */
  protected void setHideOnRelease(boolean hideOnRelease)
  {
    this.hideOnRelease = hideOnRelease;
  }

  /**
   * Returns whether or not the rubber band should disappear after the mouse is
   * released
   * 
   * @return <code>true</code> if the rubber band should disappear when the
   *         mouse is released, else if <code>false</code> the rubber band
   *         should remain visible until a new rubber band is created
   */
  protected boolean isHideOnRelease()
  {
    return hideOnRelease;
  }
}

/**
 * A simple class to help us track the mouse history.
 */
class MouseHistory
{
  Point p;
  long time;
  
  public MouseHistory(int x, int y)
  {
    p = new Point(x, y);
    time = System.currentTimeMillis();
  }
  public Point getPoint()
  {
    return p;
  }
  public long getTime()
  {
    return time;
  }
}

