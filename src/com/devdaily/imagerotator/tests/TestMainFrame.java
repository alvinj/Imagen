package com.devdaily.imagerotator.tests;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import java.net.URL;
import javax.swing.event.ChangeListener;
import java.io.File;
import javax.swing.event.ChangeEvent;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.RescaleOp;
import java.awt.image.BufferedImage;

public class TestMainFrame extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    TestMain controller;
    SeeThroughComponent st;

    public TestMainFrame(TestMain controller) {
        this.controller = controller;
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
            moveTheImage();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public TestMainFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void jbInit() throws Exception {
        String imageFileName  = "duke_skateboard.jpg";
        String imageFileName2 = "07.jpg";
        URL imageSrc = null;
        URL imageSrc2 = null;
        imageSrc = ((new File(imageFileName)).toURI()).toURL();
        imageSrc2 = ((new File(imageFileName2)).toURI()).toURL();
        st = new SeeThroughComponent(imageSrc, imageSrc2);
        // i need to be drawing this at a specific location instead of doing this
        getContentPane().add("Center", st);
        JSlider opacitySlider = new JSlider(0, 100);
        opacitySlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    JSlider b = (JSlider)e.getSource();
                    JSlider slider = (JSlider)e.getSource();
                    st.setOpacity(slider.getValue()/100f);
                    st.repaint();
                };
            });
        Dimension size = st.getPreferredSize();
        Dimension sliderSize = opacitySlider.getPreferredSize();
        //resize(size.width, size.height+sliderSize.height);
        getContentPane().add("South", opacitySlider);
    }

    void moveTheImage()
    {

    }
} // end of TestMainFrame


class AlPanel extends JPanel
{
    SeeThroughComponent stc;
    private BufferedImage bi;
    float[] scales = { 1f, 1f, 1f, 0.5f };
    float[] offsets = new float[4];
    RescaleOp rop;

    public AlPanel(SeeThroughComponent stc)
    {
        super();
        this.stc = stc;
        // do a little animation
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.white);
        g2d.fillRect(0,0, getWidth(), getHeight());
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Dialog", Font.BOLD, 24));
        g2d.drawString("Java 2D is great!", 10, 80);
        g2d.drawImage(bi, rop, 0, 0);
    }
}





