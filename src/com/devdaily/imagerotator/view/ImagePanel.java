package com.devdaily.imagerotator.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.*;

public class ImagePanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    ImageIcon imageIcon;
    JLabel imageLabel = new JLabel();

    public ImagePanel(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ImagePanel() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        if (imageLabel!=null) {
            imageLabel.setIcon(imageIcon);
            this.add(imageLabel, java.awt.BorderLayout.CENTER);
            this.setSize(this.getPreferredSize());
        }
    }
}
