package com.devdaily.imagen.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageComponent extends Component
{

  Image image;
  int newW, newH = 0;

  public ImageComponent(Image image)
  {
    this.image = image;
  }

  public void setImage(Image image, int newW, int newH)
  {
    this.image = image;
    this.newW = newW;
    this.newH = newH;
  }

  public void paint(Graphics g)
  {
    g.drawImage(image, 0, 0, null);
  }

  public Dimension getPreferredSize()
  {
    if (image == null)
    {
      return new Dimension(100, 100);
    }
    else if (newW != 0)
    {
      return new Dimension(newW, newH);
    }
    else
    {
      return new Dimension(image.getWidth(null), image.getHeight(null));
    }
  }
}
