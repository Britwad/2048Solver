package GameEngineV3.Tools;

import GameEngineV3.Game;
import GameEngineV3.GameObject;

import java.awt.*;

public class OptionBox extends GameObject {

    private int x, y, width, height, textX, textY, borderThickness, borderRoundness;
    private Runnable clickBlock, tickBlock;
    private String text;
    private boolean textCentered, textCenteredVertically, fillBox, borderOn, isClickable, isVisible, mouseOver;
    private Color color, borderColor, fontColor;
    private Font font;

    public OptionBox(Rectangle rect) {
        this(rect,"");
    }
    public OptionBox(Rectangle rect, String text) {
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
        this.textX = 0;
        this.textY = 0;
        this.borderThickness = 1;
        this.borderRoundness = 0;

        this.text = text;
        this.textCentered = true;
        this.textCenteredVertically = false;
        this.fillBox = false;
        this.borderOn = true;
        this.isClickable = true;
        this.isVisible = true;
        this.mouseOver = false;

        this.color = Color.white;
        this.borderColor = Color.black;
        this.fontColor = Color.black;

        this.font = new Font("Century Gothic",Font.BOLD,20);
    }

    public void tick(Game game) {
        if (tickBlock!=null) tickBlock.run();
    }

    public void render(Graphics g) {
        if (isVisible) {
            if (fillBox) {
                g.setColor(this.color);
                if (borderRoundness!=0) g.fillRoundRect(x,y,width,height,borderRoundness,borderRoundness);
                else g.fillRect(x,y,width,height);
            }
            if (borderOn) {
                ((Graphics2D) g).setStroke(new BasicStroke(borderThickness));
                if (borderRoundness!=0) g.drawRoundRect(x,y,width,height,borderRoundness,borderRoundness);
                else g.drawRect(x,y,width,height);
            }
            g.setColor(fontColor);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();
            if (textCentered) Methods.drawCenteredString(g, text, new Rectangle(x, y, width, height));
            else if (textCenteredVertically) {
                Methods.drawVerticallyCenteredString(g, text, new Rectangle(x, y, width, height));
            } else g.drawString(text, x + textX, y + textY + metrics.getHeight());
        }
    }

    //MUTATORS
    public void clicked() {
        if (clickBlock!=null&isClickable) {
            clickBlock.run();
        }
    }

    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int w) {this.width = w;}
    public void setHeight(int h) {this.height = h;}
    public void setTextX(int x) {this.textX = x;}
    public void setTextY(int y) {this.textY = y;}
    public void setBorderThickness(int n) {this.borderThickness = n;}
    public void setBorderRoundness(int n) {this.borderRoundness = n;}

    public void setTickBlock(Runnable r) {
        this.tickBlock = r;
    }
    public void setClickBlock(Runnable r) {
        this.clickBlock = r;
    }

    public void setText(String s) {this.text = s;}

    public void setTextCentered(boolean b) {this.textCentered = b;}
    public void setTextCenteredVertically(boolean b) {this.textCenteredVertically = b;}
    public void setFillBox(boolean b) {this.fillBox = true;}
    public void setBorderOn(boolean b) {this.borderOn = b;}
    public void setClickable(boolean b) {this.isClickable = b;}
    public void setVisible(boolean b) {this.isVisible = b;}
    public void setMouseOver(Boolean b) {mouseOver = b;}

    public void setColor(Color c) {this.color = c;}
    public void setBorderColor(Color c) {this.borderColor = c;}
    public void setFontColor(Color c) {this.fontColor = c;}

    public void setFont(Font f) {this.font = f;}

    //ACCESSORS
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}

    public Color getColor() {return this.color;}
    public Color getBorderColor() {return this.borderColor;}
    public Color getFontColor() {return this.fontColor;}

    public boolean getMouseOver() {return this.mouseOver;}
    public boolean getIsClickable() {return this.isClickable;}
    public boolean getIsVisible() {return this.isVisible;}


}
