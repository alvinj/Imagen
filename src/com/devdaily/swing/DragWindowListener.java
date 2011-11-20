package com.devdaily.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 * NOTE: THIS CLASS ISN'T REALLY USED ANY MORE.
 *       ALL OF THE MOUSE BEHAVIOR IS NOW IN THE 
 *       "ABSTRACT RUBBER BAND" CLASS B/C I COULDN'T GET
 *       EVERYTHING TO WORK TOGETHER RIGHT (COULDN'T PASS
 *       MOUSE EVENTS FROM THERE TO HERE PROPERLY).
 * 
 * This was named MoveMouseListener in the Swing Hacks book.
 * This is Swing Hack #34.
 * 
 * Modified so "drag" only works when ALT is held down.
 * 
 */
public class DragWindowListener implements MouseListener, MouseMotionListener
{
  JFrame jFrame;
  Point startDragPoint;
  Point startLocPoint;

  public DragWindowListener(JFrame jComponent)
  {
    this.jFrame = jComponent;
  }

  Point getScreenLocation(MouseEvent e)
  {
    Point cursor = e.getPoint();
    Point targetLocation = this.jFrame.getLocationOnScreen();
    return new Point((int) (targetLocation.getX() + cursor.getX()), (int) (targetLocation.getY() + cursor.getY()));
  }

  public void mousePressed(MouseEvent e)
  {
    System.err.println("DragWindowListener::mousePressed() called");
    if (!e.isAltDown() && !e.isControlDown() && !e.isMetaDown() && !e.isShiftDown())
    {
      this.startDragPoint = this.getScreenLocation(e);
      this.startLocPoint = jFrame.getLocation();
    }
  }

  public void mouseDragged(MouseEvent e)
  {
    System.err.println("DragWindowListener::mouseDragged() called");
    // only work if there are no meta keys in use
    if (!e.isAltDown() && !e.isControlDown() && !e.isMetaDown() && !e.isShiftDown())
    {
      Point current = this.getScreenLocation(e);
      Point offset = new Point((int) current.getX() - (int) startDragPoint.getX(), (int) current.getY() - (int) startDragPoint.getY());
      //JFrame frame = this.getFrame(jComponent);
      Point new_location = new Point((int) (this.startLocPoint.getX() + offset.getX()), (int) (this.startLocPoint.getY() + offset.getY()));
      jFrame.setLocation(new_location);
    }
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void mouseMoved(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

}






