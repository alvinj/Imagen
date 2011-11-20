package com.devdaily.imagerotator;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * I grabbed this example so I could look at the Java2D stuff, especially the
 * transparency stuff.
 */
class SeeThroughComponent extends Component {

    private BufferedImage bi;
    float[] scales = { 1f, 1f, 1f, 0.5f };
    float[] offsets = new float[4];
    RescaleOp rop;

    public SeeThroughComponent(URL imageSrc) {
        try {
            BufferedImage img = ImageIO.read(imageSrc);
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, null);

        } catch (IOException e) {
            System.out.println("Image could not be read");
            System.exit(1);
        }
        setOpacity(0.5f);
    }

    public Dimension getPreferredSize() {
        return new Dimension(bi.getWidth(null), bi.getHeight(null));
    }

    public void setOpacity(float opacity) {
        scales[3] = opacity;
        rop = new RescaleOp(scales, offsets, null);
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

public class SeeThroughImageApplet extends JApplet {

    static String imageFileName = "duke_skateboard.jpg";
    private URL imageSrc;

    public SeeThroughImageApplet () {
    }

    public SeeThroughImageApplet (URL imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void init() {
        try {
            System.err.println("CodeBase: " + getCodeBase());
            imageSrc = new URL(getCodeBase(), imageFileName);
        } catch (MalformedURLException e) {
        }
        buildUI();
    }

    public void buildUI() {
        final SeeThroughComponent st = new SeeThroughComponent(imageSrc);
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
        resize(size.width, size.height+sliderSize.height);
        getContentPane().add("South", opacitySlider);
    }

    public static void main(String s[]) {
        JFrame f = new JFrame("See Through Image");
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        URL imageSrc = null;
        try {
             imageSrc = ((new File(imageFileName)).toURI()).toURL();
        } catch (MalformedURLException e) {
        }
        SeeThroughImageApplet sta = new SeeThroughImageApplet(imageSrc);
        sta.buildUI();
        f.getContentPane().add("Center", sta);
        f.pack();
        f.setVisible(true);
    }
}
