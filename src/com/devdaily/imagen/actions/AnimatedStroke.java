package com.devdaily.imagen.actions;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;



/**
 * Handles the necessary work of animating the stroke of a {@link RubberBand}.
 *
 * The idea for having an animated rubber band came from the java.net project,
 * <a href="https://rubber-band.dev.java.net/">Rubber-Band</a> 
 * 
 * The code was extracted from the project and modified to work with this
 * Rubber Band API.
 *
 * @author rwickesser
 * @since 1.0
 * $Revision: 1.1 $
 */
public class AnimatedStroke {
    private static final int DASH_WIDTH = 3;
    private static final int STROKE_COUNT = 2 * DASH_WIDTH;
    private static final int DASH_SPEED = 50;

    private int strokeIndex;
    private final Stroke[] strokes;
    private final Timer animationTimer;

    /** the canvas which the rubber band will be drawn on */
    private RubberBandCanvas canvas;

    public AnimatedStroke(RubberBandCanvas canvas) {
        this .canvas = canvas;
        animationTimer = new Timer(DASH_SPEED,
                getAnimationActionListener());
        strokes = new Stroke[STROKE_COUNT];
        initStrokes();
    }

    /**
     * Initializes the array of <code>Stroke</code>'s
     */
    private void initStrokes() {
        float[] dash = { DASH_WIDTH, DASH_WIDTH };
        for (int i = 0; i < STROKE_COUNT; i++) {
            strokes[i] = new BasicStroke(1, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1, dash, i);
        }
    }

    /**
     * Returns the <code>Stroke</code> at <code>strokeIndex</code>
     * 
     * @return  the <code>Stroke</code> at <code>strokeIndex</code>
     */
    public Stroke getStroke() {
        return strokes[strokeIndex];
    }

    /**
     * Starts the animated stroke
     */
    public void startAnimation() {
        animationTimer.start();
    }

    /**
     * Stops the animated stroke
     */
    public void stopAnimation() {
        animationTimer.stop();
    }

    /**
     * Returns a new <code>ActionListener</code> that updates the stroke
     * as needed
     * 
     * @return  a new <code>ActionListener</code> that updates the stroke
     *          as needed
     */
    private ActionListener getAnimationActionListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                strokeIndex++;
                strokeIndex %= STROKE_COUNT;
                canvas.getCanvas().repaint();
            }
        };
    }
}
