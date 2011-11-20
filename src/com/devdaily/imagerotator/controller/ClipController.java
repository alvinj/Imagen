package com.devdaily.imagerotator.controller;

import java.awt.*;
import java.awt.datatransfer.*;

public class ClipController {

  MainOld mainController;

  public ClipController(MainOld mainController) {
    this.mainController = mainController;
  }


//  public void doReceivedNewClipContentAction(String content) {
//    clipTableModel.addNewContent(content);
//  }

  public void sendCurrentlySelectedRowToClipboard(int selectedRow) {
    if (selectedRow<0) return;
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//    ClipModel clip = (ClipModel)clipTableModel.getObjectAtRow(selectedRow);
//    StringSelection ss = new StringSelection(clip.getContent());
//    clipboard.setContents(ss,null);
  }

}



