package com.devdaily.imagerotator.controller;

import java.sql.*;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.datatransfer.*;
import com.devdaily.imagerotator.model.DropModel;

public class DropController {

  MainOld mainController;
//  DropTableModel dropTableModel;
//  DropTablePanel dropTablePanel;
//  DropAndClipMainPanel dropAndClipMainPanel;
  List dropList;

  public DropController(MainOld mainController) {
    this.mainController = mainController;
    // populate the list of items from the database
//    try {
//      DirectoryDO ddo = new DirectoryDO();
//      dropList = ddo.selectAll(mainController.getConnection());
//    }
//    catch (SQLException ex) {
//      /** @todo Need to do something here to handle this. */
//    }
//    dropTableModel = new DropTableModel(this,dropList);
//    dropTablePanel = new DropTablePanel(this, dropTableModel);
//    dropAndClipMainPanel = new DropAndClipMainPanel(this, dropTablePanel);
  }

//  public DropAndClipMainPanel getDropAndClipMainPanel() {
//    return this.dropAndClipMainPanel;
//  }

  public void addNewContent(String content) {

  }

  public void doReceivedNewDropContentAction(String content) {
    // 1 - add to the database
//    DirectoryDO dao = new DirectoryDO();
    DropModel model = new DropModel();
    model.setContent(content);
    model.setDate(Calendar.getInstance().getTime());
//    try {
//      dao.insert(mainController.getConnection(), model);
//    }
//    catch (SQLException ex) {
//      //not doing anything with this at this time
//    }
    /** @todo I'm making an assumption here that the database hit worked. */
    // 2 - add to the table model
    dropList.add(0,model);
//    dropTableModel.fireTableDataChanged();
  }

  public void sendCurrentlySelectedRowToClipboard(int selectedRow) {
    if (selectedRow<0) return;
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//    DropModel drop = (DropModel)dropTableModel.getObjectAtRow(selectedRow);
//    StringSelection ss = new StringSelection(drop.getContent());
//    clipboard.setContents(ss,null);
  }

  public void doDeleteAction(int selectedRow) {
    if (selectedRow<0) return;
//    DropModel drop = (DropModel)dropTableModel.getObjectAtRow(selectedRow);
//    DirectoryDO ddo = new DirectoryDO();
//    try {
//      ddo.deleteDrop(mainController.getConnection(), drop.getId());  // delete from database
//      dropList.remove(drop);                                         // delete from active model
//      dropTableModel.fireTableDataChanged();
//    }
//    catch (SQLException ex) {
//      /** @todo Need to handle this. */
//    }
  }
}


