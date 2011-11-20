package com.devdaily.imagen.actions;

import javax.swing.JComponent;


/**
 * An interface for which any {@link javax.swing.JComponent} that
 * wishes to enable rubber band functionality should implement.
 * 
 * The canvas is the area on which the {@link RubberBand} will be drawn
 * onto.
 * 
 * @author rwickesser
 * @since 1.0
 * $Revision: 1.1 $
 */
public interface RubberBandCanvas {

    /**
     * Returns the <code>JComponent</code> which is the canvas for the
     * <code>RubberBand</code>
     * 
     * @return  the <code>JComponent</code> which is the canvas for the
     *          <code>RubberBand</code>
     */
    public JComponent getCanvas();

    /**
     * Sets the <code>RubberBand</code> for the canvas
     * 
     * @param rubberband    the <code>RubberBand</code> for the canvas
     */
    public void setRubberBand(RubberBand rubberband);
}
