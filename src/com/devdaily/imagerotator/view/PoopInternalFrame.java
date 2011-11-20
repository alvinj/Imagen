package com.devdaily.imagerotator.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class PoopInternalFrame extends JInternalFrame {
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel pnlImage = new JPanel();
    BorderLayout borderLayout2 = new BorderLayout();
    JLabel lblImage = new JLabel();
    ImageIcon imageIcon = new ImageIcon("07.jpg");

    public PoopInternalFrame() {
        super();
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(borderLayout1);
        pnlImage.setLayout(borderLayout2);
        lblImage.setIcon(imageIcon);
        this.addKeyListener(new PoopInternalFrame_this_keyAdapter(this));
        pnlImage.add(lblImage, java.awt.BorderLayout.CENTER);
        pnlImage.setPreferredSize(pnlImage.getPreferredSize());
        pnlImage.setSize(pnlImage.getPreferredSize());
        this.setSize(pnlImage.getPreferredSize());

        this.setResizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setFrameIcon(imageIcon);

        this.getContentPane().add(pnlImage, java.awt.BorderLayout.CENTER);
    }

    public void this_keyPressed(KeyEvent e) {
        System.err.println("iframe: keyPressed");
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
          System.err.println("iframe: Got keystroke");
        }
    }

    public void this_keyTyped(KeyEvent e) {
        System.err.println("iframe: keyTyped");
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
          System.err.println("iframe: Got keystroke");
        }
    }
}


class PoopInternalFrame_this_keyAdapter extends KeyAdapter {
    private PoopInternalFrame adaptee;
    PoopInternalFrame_this_keyAdapter(PoopInternalFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void keyPressed(KeyEvent e) {
        adaptee.this_keyPressed(e);
    }
}
