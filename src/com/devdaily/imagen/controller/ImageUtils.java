package com.devdaily.imagen.controller;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import javax.swing.ImageIcon;

public class ImageUtils
{
  
  public static BufferedImage getFasterScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
      boolean progressiveBilinear)
  {
    int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = img;
    BufferedImage scratchImage = null;
    Graphics2D g2 = null;
    int w, h;
    int prevW = ret.getWidth();
    int prevH = ret.getHeight();
    boolean isTranslucent = img.getTransparency() != Transparency.OPAQUE;

    if (progressiveBilinear)
    {
      // Use multi-step technique: start with original size, then
      // scale down in multiple passes with drawImage()
      // until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    }
    else
    {
      // Use one-step technique: scale directly from original
      // size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }

    do
    {
      if (progressiveBilinear && w > targetWidth)
      {
        w /= 2;
        if (w < targetWidth)
        {
          w = targetWidth;
        }
      }

      if (progressiveBilinear && h > targetHeight)
      {
        h /= 2;
        if (h < targetHeight)
        {
          h = targetHeight;
        }
      }

      if (scratchImage == null || isTranslucent)
      {
        // Use a single scratch buffer for all iterations
        // and then copy to the final, correctly-sized image
        // before returning
        scratchImage = new BufferedImage(w, h, type);
        g2 = scratchImage.createGraphics();
      }
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
      prevW = w;
      prevH = h;

      ret = scratchImage;
    }
    while (w != targetWidth || h != targetHeight);

    if (g2 != null)
    {
      g2.dispose();
    }

    // If we used a scratch buffer that is larger than our target size,
    // create an image of the right size and copy the results into it
    if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight())
    {
      scratchImage = new BufferedImage(targetWidth, targetHeight, type);
      g2 = scratchImage.createGraphics();
      g2.drawImage(ret, 0, 0, null);
      g2.dispose();
      ret = scratchImage;
    }
    return ret;
  }
  
  // from http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
  // This method returns a buffered image with the contents of an image
  public static BufferedImage toBufferedImage(Image image)
  {
    if (image instanceof BufferedImage)
    {
      return (BufferedImage) image;
    }

    // This code ensures that all the pixels in the image are loaded
    image = new ImageIcon(image).getImage();

    // Determine if the image has transparent pixels; for this method's
    // implementation, see e661 Determining If an Image Has Transparent Pixels
    boolean hasAlpha = hasAlpha(image);

    // Create a buffered image with a format that's compatible with the screen
    BufferedImage bimage = null;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try
    {
      // Determine the type of transparency of the new buffered image
      int transparency = Transparency.OPAQUE;
      if (hasAlpha)
      {
        transparency = Transparency.BITMASK;
      }

      // Create the buffered image
      GraphicsDevice gs = ge.getDefaultScreenDevice();
      GraphicsConfiguration gc = gs.getDefaultConfiguration();
      bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
    }
    catch (HeadlessException e)
    {
      // The system does not have a screen
    }

    if (bimage == null)
    {
      // Create a buffered image using the default color model
      int type = BufferedImage.TYPE_INT_RGB;
      if (hasAlpha)
      {
        type = BufferedImage.TYPE_INT_ARGB;
      }
      bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
    }

    // Copy image to buffered image
    Graphics g = bimage.createGraphics();

    // Paint the image onto the buffered image
    g.drawImage(image, 0, 0, null);
    g.dispose();

    return bimage;
  }

  // This method returns true if the specified image has transparent pixels
  public static boolean hasAlpha(Image image)
  {
    // If buffered image, the color model is readily available
    if (image instanceof BufferedImage)
    {
      BufferedImage bimage = (BufferedImage) image;
      return bimage.getColorModel().hasAlpha();
    }

    // Use a pixel grabber to retrieve the image's color model;
    // grabbing a single pixel is usually sufficient
    PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
    try
    {
      pg.grabPixels();
    }
    catch (InterruptedException e)
    {
    }

    // Get the image's color model
    ColorModel cm = pg.getColorModel();
    return cm.hasAlpha();
  }

}
