package com.devdaily.imagerotator.view;

import java.awt.Image;

import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

public class MyInternalFrame extends JInternalFrame {
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    protected String title;
    protected String prefsLabel;
    BorderLayout borderLayout1 = new BorderLayout();
    JLabel lblImage = new JLabel();
    Image originalImage;

    // working here to get height and width and do scaling
    Image tmpImage;
    float aspectRatio = 1;
    int h;
    int w;

    public MyInternalFrame(String title, String prefsLabel) {
      super(title,
            true, //resizable
            true, //closable
            true, //maximizable
            true);//iconifiable
      this.title = title;
      this.prefsLabel = prefsLabel;

      //...Then set the window size or call pack...
      setSize(300,300);

      //Set the window's location.
      setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }

    /**
     * Use this method.
     */
    public MyInternalFrame(JLabel label) {
        super("",
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

        //Set the window's location.
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.lblImage = label;
        lblImage.setBackground(Color.PINK);
        lblImage.setOpaque(true);
        this.getContentPane().add(lblImage, java.awt.BorderLayout.CENTER);

        // this originalImage is used later; not sure this is coded well at all
        ImageIcon ii = (ImageIcon)lblImage.getIcon();
        this.originalImage = ii.getImage();

        h = lblImage.getIcon().getIconHeight();
        w = lblImage.getIcon().getIconWidth();
        setSize(w,h);
        aspectRatio = (float)h/w;

        // this doesn't work as intended
        this.setFrameIcon(lblImage.getIcon());
    }

    public MyInternalFrame() {
        super("Document #" + (++openFrameCount),
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

        setupFrame();

        //Set the window's location.
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setupFrame() {
        this.setResizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
    }
  public String getPrefsLabel() {
    return prefsLabel;
  }

  public void setImage(Image image)
  {
    lblImage.setIcon(new ImageIcon(image));
  }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        this.addComponentListener(new MyInternalFrame_this_componentAdapter(this));
    }

    /**
     * It may be possible to not do all of this, and just do one redraw
     * after the iframe has been resized. That would require listening to
     * different events.
     */
    public void this_componentResized(ComponentEvent e) {
      Dimension d = this.getSize();
      double newH = d.getHeight();
      double newW = d.getWidth();
      double newAR = newH/newW;
      if (newAR>aspectRatio)
      {
        // scale w from h
        newW = newH / aspectRatio;
      }
      else
      {
        // scale h from w
        newH = newW*aspectRatio;
      }
      // this has an interesting effect if you comment this out
      // you can resize the iframe w/o resizing the image
      tmpImage = originalImage.getScaledInstance((int)newW, (int)newH,Image.SCALE_SMOOTH);
      lblImage.setIcon(new ImageIcon(tmpImage));
    }
}


class MyInternalFrame_this_componentAdapter extends ComponentAdapter {
    private MyInternalFrame adaptee;
    MyInternalFrame_this_componentAdapter(MyInternalFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void componentResized(ComponentEvent e) {
        adaptee.this_componentResized(e);
    }
}
