package com.devdaily.imagerotator.model;

import java.util.Date;
import java.util.Calendar;

public class ClipModel {
  private String content;
  private Date date;

  public ClipModel() {
  }
  public ClipModel(String content) {
    this.content = content;
    this.date = new Date();
  }
  public String getContent() {
    return content;
  }
  public Date getDate() {
    return date;
  }

}
