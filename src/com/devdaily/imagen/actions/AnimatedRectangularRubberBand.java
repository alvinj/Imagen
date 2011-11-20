package com.devdaily.imagen.actions;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Window;
import java.util.EventObject;

import com.devdaily.imagen.view.MetaFrame;

/**
 * A {@link RubberBand} which is rectangular and animated
 * 
 * @author rwickesser
 * @since 1.0 $Revision: 1.1 $
 */
public class AnimatedRectangularRubberBand extends RectangularRubberBand
{

  /** the object responsible for animating the rubber band */
  private AnimatedStroke animStroke;

  /**
   * Creates a new rubber band which is rectangular and animated
   * 
   * @param canvas
   *          the canvas to draw the rubber band on
   */
  public AnimatedRectangularRubberBand(RubberBandCanvas canvas, MetaFrame ourWindow)
  {
    super(canvas, ourWindow);
    animStroke = new AnimatedStroke(canvas);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gui.rubberband.RubberBand#draw(java.awt.Graphics)
   */
  public void draw(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    Stroke oldStroke = g2.getStroke();
    g2.setStroke(animStroke.getStroke());
    g2.draw(rubberband);
    g2.setStroke(oldStroke);
  }

  /*
   * (non-Javadoc)
   * 
   * @see RubberBand#startRubberBand(java.util.EventObject)
   */
  public void startRubberBand(EventObject event)
  {
    animStroke.startAnimation();
  }

  /*
   * (non-Javadoc)
   * 
   * @see RubberBand#stopRubberBand(java.util.EventObject)
   */
  public void stopRubberBand(EventObject event)
  {
    animStroke.stopAnimation();
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
