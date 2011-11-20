package com.devdaily.imagerotator.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import com.devdaily.imagerotator.view.MyInternalFrame;
import com.devdaily.imagerotator.view.PoopInternalFrame;

public class MainFrame extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    JSplitPane jSplitPane1 = new JSplitPane();
    JDesktopPane desktop = new JDesktopPane();
    MainOld controller;

    public MainFrame(MainOld controller) {
        this.controller = controller;
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public MainFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        // this speeds up iframe resizing
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(400,300));
        setTitle("DevDaily.com Image Rotator");
        contentPane.add(jSplitPane1, java.awt.BorderLayout.CENTER);
        jSplitPane1.add(desktop,JSplitPane.RIGHT);

        PoopInternalFrame pframe = new PoopInternalFrame();
        desktop.add(pframe);
        pframe.setLocation(1,1);
        pframe.setVisible(true);

        configF2KeyListener();
        configControlVKeyListener();

//        this.setUndecorated(true);


    }

    private void configF2KeyListener()
    {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
              doF2KeyAction();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void configControlVKeyListener()
    {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
              doControlVKeyAction();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        this.getRootPane().registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void doControlVKeyAction() {
        controller.doPasteAction();
    }

    /**
     * @todo This method only sets the divider to 0. It doesn't return it to the previous position.
     */
    public void doF2KeyAction() {
        if (jSplitPane1.getDividerLocation() == 0) {
            jSplitPane1.setDividerLocation(jSplitPane1.getLastDividerLocation());
        } else {
            jSplitPane1.setLastDividerLocation(jSplitPane1.getDividerLocation());
            jSplitPane1.setDividerLocation(0);
            System.err.println("loc: " + jSplitPane1.getDividerLocation());
        }
    }

    public void addIFrame(MyInternalFrame iframe)
    {
        desktop.add(iframe);
        iframe.setVisible(true);
    }
}

