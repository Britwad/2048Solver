package GameEngineV3.Tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Methods {

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
    public static int getLength(int num) {
        return (int) (Math.log10(num) + 1);
    }


    public static Color brighter(Color color, double factor) {
        //Directly Taken and modified from Color.class
        int var1 = color.getRed();
        int var2 = color.getGreen();
        int var3 = color.getBlue();
        int var4 = color.getAlpha();
        byte var5 = 3;
        if (var1 == 0 && var2 == 0 && var3 == 0) {
            return new Color(var5, var5, var5, var4);
        } else {
            if (var1 > 0 && var1 < var5) {
                var1 = var5;
            }

            if (var2 > 0 && var2 < var5) {
                var2 = var5;
            }

            if (var3 > 0 && var3 < var5) {
                var3 = var5;
            }

            return new Color(Math.min((int)((double)var1 / factor), 255), Math.min((int)((double)var2 / factor), 255), Math.min((int)((double)var3 / factor), 255), var4);
        }
    }
    public static Color darker(Color color, double factor) {
        return new Color(Math.max((int)((double)color.getRed() * factor), 0), Math.max((int)((double)color.getGreen() * factor), 0), Math.max((int)((double)color.getBlue() * factor), 0), color.getAlpha());
    }
    public static Color changeAlpha(Color color, int amount) {
        return new Color (color.getRed(),color.getBlue(),color.getGreen(), Math.max(0,Math.min(color.getAlpha()+amount,255)));
    }
    public static Color setAlpha(Color color, int amount) {
        return new Color (color.getRed(),color.getBlue(),color.getGreen(), Math.max(0,Math.min(amount,255)));
    }

    public static boolean arrayContains(Object[] a, Object n) {
        for (Object i: a) {
            if (i==n) return true;
        }
        return false;
    }
    public static boolean arrayContains(int[] a, int n) {
        for (int i: a) {
            if (i==n) return true;
        }
        return false;
    }
    public static int arrayMin(int[] a) {
        int min = a[0];
        for (int n: a) {
            if (n<min) min=n;
        }
        return min;
    }
    public static int arrayMax(int[] a) {
        int max = a[0];
        for (int n: a) {
            if (n>max) max=n;
        }
        return max;
    }


    public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        int type = BufferedImage.TYPE_INT_ARGB;
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, type);

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
    public static void resize(String inputImagePath, String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }


    public static void drawCenteredString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(s))/2;
        int y = rect.y + ((rect.height - metrics.getHeight())/2) + metrics.getAscent();
        g.drawString(s,x,y);
    }
    public static void drawVerticallyCenteredString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int y = rect.y + ((rect.height - metrics.getHeight())/2) + metrics.getAscent();
        g.drawString(s,rect.x,y);
    }
    public static void drawHorizontallyCenteredString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rect.x + (rect.width - metrics.stringWidth(s))/2;
        g.drawString(s,x,rect.y);
    }
    public static void drawWrappedString(Graphics g, String s, int x, int y, int maxX) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String[] sentence = s.split(" ");
        int i = 0;
        int yPos = y+metrics.getHeight();
        while (i< sentence.length) {
            StringBuilder temp = new StringBuilder();
            while (metrics.stringWidth(temp + sentence[i] + " ") <= maxX - x) {
                temp.append(sentence[i]).append(" ");
                i++;
                if (i==sentence.length) break;
            }
            g.drawString(temp.toString(), x, yPos);
            yPos += metrics.getHeight();
        }
    }
    public static void drawCenteredWrappedString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String[] sentence = s.split(" ");
        ArrayList<String> sentences = new ArrayList<>();
        int i = 0;
        while (i< sentence.length) {
            StringBuilder temp = new StringBuilder();
            while (metrics.stringWidth(temp + sentence[i] + " ") <= rect.width) {
                temp.append(sentence[i]).append(" ");
                i++;
                if (i==sentence.length) break;
            }
            sentences.add(temp.toString().trim());
        }
        int width = 0;
        int height = metrics.getHeight()*(sentences.size()-1);
        for (String str: sentences) {
            if (metrics.stringWidth(str)>width) width = metrics.stringWidth(str);
        }
        int xPos = rect.x + (rect.width-width)/2;
        int yPos = rect.y+(rect.height-height)/2;
        for (String str: sentences) {
            g.drawString(str,xPos,yPos);
            yPos+=metrics.getHeight();
        }
    }
    public static void drawCenteredHorizontallyWrappedString(Graphics g, String s, Rectangle rect) {
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        String[] sentence = s.split(" ");
        ArrayList<String> sentences = new ArrayList<>();
        int i = 0;
        while (i< sentence.length) {
            StringBuilder temp = new StringBuilder();
            while (metrics.stringWidth(temp + sentence[i] + " ") <= rect.width) {
                temp.append(sentence[i]).append(" ");
                i++;
                if (i==sentence.length) break;
            }
            sentences.add(temp.toString().trim());
        }
        int width = 0;
        for (String str: sentences) {
            if (metrics.stringWidth(str)>width) width = metrics.stringWidth(str);
        }
        int xPos = rect.x + (rect.width-width)/2;
        int yPos = rect.y + metrics.getHeight();
        for (String str: sentences) {
            g.drawString(str,xPos,yPos);
            yPos+=metrics.getHeight();
        }
    }

    public static int minimumDifferenceInArray(int n, int[] values) {
        int minDifference = Math.abs(n-values[0]);
        int minIndex = 0;
        for (int i = 0;i<values.length;i++) {
            if (Math.abs(n-values[i])<minDifference) {
                minDifference = Math.abs(n-values[i]);
                minIndex = i;
            }
        }
        return values[minIndex];
    }
}
