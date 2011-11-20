package com.devdaily.imagen.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;

/**
 * This approach did not work on the Mac.
 * Mac OS X v10.5.7.
 * Java version 1.5.0_16
 * @author al
 *
 */
public class DropPanel extends JPanel implements DropTargetListener
{
  MetaFrame metaFrame;
  BufferedImage bufferedImage;
  int h, w = 0;
  float aspectRatio;
  
  public DropPanel(MetaFrame metaFrame)
  {
    this.metaFrame = metaFrame;
    setBackground(Color.PINK);
  }
  
  public void dragEnter(DropTargetDragEvent e)
  {
    // Called when the user is dragging and enters this drop target.
    setBackground(Color.GREEN);
  }

  public void dragExit(DropTargetEvent e)
  {
    // Called when the user is dragging and leaves this drop target.
    setBackground(Color.WHITE);
  }

  public void dragOver(DropTargetDragEvent e)
  {
    // Called when the user is dragging and moves over this drop target.
    setBackground(Color.GREEN);
  }

  public void dropActionChanged(DropTargetDragEvent e)
  {
    // Called when the user changes the drag action between copy or move.
  }

  public void drop(DropTargetDropEvent e)
  {
    // Called when the user finishes or cancels the drag operation.
    Transferable transferable = e.getTransferable();
    try
    {
      if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
      {
        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
        e.getDropTargetContext().dropComplete(true);
        
        System.err.println("The DropPanel received the DropEvent");
        metaFrame.addImage(image);
      }
      else
      {
        e.rejectDrop();
      }
    }
    catch (IOException ioe)
    {
      //ioe.rejectDrop();
    }
    catch (UnsupportedFlavorException ufe)
    {
      //ufe.rejectDrop();
    }

  }
}
