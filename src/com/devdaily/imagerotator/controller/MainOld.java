package com.devdaily.imagerotator.controller;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.ImageIcon;
import java.net.MalformedURLException;
import java.util.List;
import java.io.File;
import javax.swing.JLabel;
import java.lang.reflect.InvocationTargetException;
import com.sun.onlineexample.ImageSelection;
import java.awt.Image;
import com.devdaily.imagerotator.view.MyInternalFrame;
import com.graphicjava.ImageSelection2;
import javax.swing.TransferHandler;
import java.awt.image.BufferedImage;
import java.net.URL;


public class MainOld {

    ClipController clipController;
    MainFrame frame;

    public MainOld() {

        clipController = new ClipController(this);

//        ImageIcon imageIcon = new ImageIcon("07.jpg");
//        ImagePanel imagePanel = new ImagePanel(imageIcon);
        frame = new MainFrame(this);
        frame.pack();

        // make the frame 1/2 the height and width
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setSize(width/2, height/2);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              try {
                  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                  SwingUtilities.updateComponentTreeUI(this);
              } catch (Exception exception) {
                  exception.printStackTrace();
              }

              new MainOld();
          }
      });
    }

    public void doPasteAction()
    {
      System.err.println("Getting clipboard image");
      // get clipboard img
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      if (clipboard == null) System.err.println("clipboard == null");
      Transferable contents = clipboard.getContents(this);
//      if (contents == null) System.err.println("contents == null");
      if (contents != null && contents.isDataFlavorSupported(DataFlavor.imageFlavor))
      if (contents != null)
      {
        System.err.println("  contents are not null");
        try
        {
          ImageIcon imageIcon = new ImageIcon("07.jpg");
          JLabel label = new JLabel(imageIcon);
          label.setTransferHandler(new ImageSelection());
          TransferHandler handler = label.getTransferHandler();
          handler.importData(label, contents);

//          Image image;
//          image = (Image) contents.getTransferData(ImageSelection2.JPGFlavor);
          MyInternalFrame iframe = new MyInternalFrame(label);
//          iframe.setImage(image);
          frame.addIFrame(iframe);
          System.err.println("  added frame to mainframe");
        }
        catch (Exception e)
        {
          System.err.println("Got an exception during a paste operation.");
          System.err.println(e.getMessage());
        }
      }
    }

    private void processFiles(List listOfThings)
    {
//        bar.setIndeterminate(true);
        Thread thread = new Thread(new ProcessFilesRunnable(listOfThings));
//        dropTarget.setActive(false);
        thread.start();
    }

    private class DropTargetImplementation extends DropTargetAdapter
    {
      public void drop(DropTargetDropEvent dtde)
      {
        try
        {
          dtde.acceptDrop(DnDConstants.ACTION_LINK);
          List dndThings = (java.util.List)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
          dtde.dropComplete(true);
          processFiles(dndThings);
        }
        catch (Exception e)
        {
          dtde.rejectDrop();
        }
      }

    } // end of DropTargetImpl class

    /** @todo Do something here */
    private class ProcessFilesRunnable implements Runnable
    {
      private List files;

      public ProcessFilesRunnable(List files)
      {
        this.files = files;
      }

      public void run()
      {
        for (int i = 0; i < files.size(); i++)
        {
          final File file = (File) files.get(i);

          String content = file.getAbsolutePath();
          System.err.println("content: " + content);
//          mainController.doReceivedNewDropContentAction(content);

          try
          {
            SwingUtilities.invokeAndWait(new Runnable()
            {
              public void run()
              {
//                bar.setString(file.getName());
                if(file.isFile()&& file.getName().matches(".*\\.(?:jpg|png|gif)$"))
                {
                  try
                  {
                    ImageIcon i = new ImageIcon(file.toURL());
                    JLabel imageLabel = new JLabel(i);
//                    imageBox.add(imageLabel,0);
//                    scrollPane.validate();
//                    scrollPane.repaint();
                  }
                  catch (MalformedURLException e)
                  {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                  }
                }
              }
            });
            Thread.sleep(1000);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
          catch (InvocationTargetException e)
          {
            e.printStackTrace();
          }
        }

        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
//            bar.setIndeterminate(false);
            //DropDialog.this.dispose();
          }
        });
      }
  } // end of ProcessFilesRunnable

} // end of Main



