package com.devdaily.imagen.actions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Window;
import java.util.EventObject;

import com.devdaily.imagen.view.MetaFrame;

/**
 * A {@link RubberBand} which is rectangular
 * 
 * @author rwickesser
 * @since 1.0 $Revision: 1.1 $
 */
public class RectangularRubberBand extends AbstractRubberBand
{

  /**
   * Creates a new rubber band which is rectangular
   * 
   * @param canvas
   *          the canvas to draw the rubber band on
   */
  public RectangularRubberBand(RubberBandCanvas canvas, MetaFrame ourWindow)
  {
    super(canvas, ourWindow);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gui.rubberband.RubberBand#update(int, int, int, int)
   */
  public void update(int x, int y, int width, int height)
  {
    rubberband.setBounds(x, y, width, height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gui.rubberband.RubberBand#draw(java.awt.Graphics)
   */
  public void draw(Graphics g)
  {
    g.setColor(Color.YELLOW);
    g.drawRect(rubberband.x, rubberband.y, rubberband.width, rubberband.height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see RubberBand#startRubberBand(java.util.EventObject)
   */
  public void startRubberBand(EventObject event)
  {
    // don't need to do anything here
  }

  /*
   * (non-Javadoc)
   * 
   * @see RubberBand#stopRubberBand(java.util.EventObject)
   */
  public void stopRubberBand(EventObject event)
  {
    // don't need to do anything here
  }

  /*
   * (non-Javadoc)
   * 
   * @see RubberBand#updateRubberBand(java.util.EventObject)
   */
  public void updateRubberBand(EventObject event)
  {
    // don't need to do anything specific here for this rubber band
  }
}
