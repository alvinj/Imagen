package com.devdaily.imagerotator.tests;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.image.RescaleOp;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

class SeeThroughComponent extends Component {

    private BufferedImage bi;
    private BufferedImage bi2;
    float[] scales = { 1f, 1f, 1f, 0.5f };
    float[] offsets = new float[4];
    RescaleOp rop;

    float[] scales2 = { 1f, 1f, 1f, 1f };
    float[] offsets2 = new float[4];
    RescaleOp rop2;

    /**
     * I changed this to draw two images so I could see how the transparency
     * could work. It actually works w/ Duke painted on top of the other
     * image, i can make duke more or less transparent.
     */
    public SeeThroughComponent(URL imageSrc, URL imageSrc2) {
        try {
            BufferedImage img = ImageIO.read(imageSrc);
            BufferedImage img2 = ImageIO.read(imageSrc2);
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            int w2 = img2.getWidth(null);
            int h2 = img2.getHeight(null);
            bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            bi2 = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, null);
            // my second image
            Graphics g2 = bi2.getGraphics();
            g2.drawImage(img2, 0, 0, null);
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
        rop2 = new RescaleOp(scales2, offsets2, null);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.white);
        //g2d.fillRect(0,0, getWidth(), getHeight());
        g2d.fillRect(0,0, getWidth(), getHeight());
        g2d.setColor(Color.black);
//        g2d.setFont(new Font("Dialog", Font.BOLD, 24));
//        g2d.drawString("Java 2D is great!", 10, 80);
        g2d.drawImage(bi2, rop2, 0, 0);
        g2d.drawImage(bi, rop, 100, 200);
    }
}
