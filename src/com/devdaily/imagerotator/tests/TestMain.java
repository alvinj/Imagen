package com.devdaily.imagerotator.tests;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestMain {
    boolean packFrame = false;
    TestMainFrame frame;

    public TestMain() {

        frame = new TestMainFrame(this);

        // maximize the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(0,0);
        frame.setSize(screenSize);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              try {
                  UIManager.setLookAndFeel(UIManager.
                                           getSystemLookAndFeelClassName());
              } catch (Exception exception) {
                  exception.printStackTrace();
              }

              new TestMain();
          }
      });
    }

} // end of TestMain



