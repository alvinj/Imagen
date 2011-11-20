package com.devdaily.imagerotator.model;

import java.util.Date;
import java.util.Calendar;

public class DropModel {
  private int id;
  private String content;
  private Date date;

  public DropModel() {
  }
  public DropModel(String content) {
    this.content = content;
    this.date = new Date();
  }
  public String getContent() {
    return content;
  }
  public Date getDate() {
    return date;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  public void setContent(String content) {
    this.content = content;
  }

}

