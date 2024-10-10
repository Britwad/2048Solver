package GameEngineV3.Tools;

import GameEngineV3.Game;
import GameEngineV3.GameObject;

import java.awt.*;

public class OptionSlider extends GameObject {

    private int x, y, width, height, textX, textY, lineThickness, round;
    private Runnable dragBlock, tickBlock;
    private String text;
    private Color color, lineColor, fontColor;
    private Font font;
    private boolean isClickable, isVisible, textCentered, mouseOver, setValues, sliding;
    private double tempValue, value, min, max;
    private int[] values;

    private OptionSlider(int x, int y, int width, int height, double initValue, int round) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = "";
        this.textCentered = true;
        this.textX = 0;
        this.textY = 0;
        this.lineColor = Color.black;
        this.lineThickness = 1;
        this.fontColor = Color.black;
        this.font = new Font("Century Gothic",Font.BOLD,20);
        this.mouseOver = false;
        this.isClickable = true;
        this.isVisible = true;
        this.value = initValue;
        this.round = round;
    }
    public OptionSlider(int x, int y, int width, int height, double initValue, double min, double max, int round) {
        this(x,y,width,height,initValue,round);

        this.tempValue = min-5000;
        this.min = min;
        this.max = max;

    }
    public OptionSlider(int x, int y, int width, int height, int initValue, int[] values, int round) {
        this(x,y,width,height, initValue, round);

        this.max = Methods.arrayMax(values);
        this.min = Methods.arrayMin(values);
        this.tempValue = min-5000;
        setValues = true;
        this.values = values;
    }

    public void tick(Game game) {
        if (tickBlock!=null) tickBlock.run();
        if (setValues) {
            if ((game.mouseDown(0) & mouseOver) | game.mouseDown(0) & sliding) {
                tempValue = Math.max(Math.min(max, (max-min)*(game.getMouseX()-min)/width + min), min);
                sliding = true;
            } else if (sliding) {
                if (setValues) value = Methods.minimumDifferenceInArray((int) tempValue, values);
                else value = tempValue;
                tempValue = min - 5000;
                sliding = false;
                if (dragBlock!=null&isClickable) dragBlock.run();
            }
        }
        else {
            if ((game.mouseDown(0)&mouseOver)|(game.mouseDown(0)&sliding)) {
                sliding = true;
                value = round(Math.max(Math.min(max,(((max - min) * ((double)((game.getMouseX()-height/2) - x) / width)) + min)), min));
                if (dragBlock!=null&isClickable) dragBlock.run();
            }
            if (!game.mouseDown(0)) sliding = false;
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(lineThickness));
        Rectangle rect = getRect();
        if (isVisible) {
            g.setColor(this.color);
            if (setValues) {
                for (int n: values) {
                    g.setColor(color);
                    g.drawLine((int) (x+(width*n)/(max-min)),y,(int) (x+(width*n)/(max-min)),y);
                }
            }
            g.drawLine(x,y,x+width,y);

            g.fillOval(rect.x,rect.y,rect.width,rect.height);

            g.setFont(font);
            g.setColor(fontColor);
            Methods.drawCenteredString(g,Double.toString(value),rect);

        }
        g2d.setStroke(new BasicStroke(1));
    }

    //HELPERS
    private double round(double n) {
        return Math.round(n*Math.pow(10,round))/Math.pow(10,round);
    }
    public Rectangle getRect() {return new Rectangle((int) (x+((value-min)/(max-min))*width),y-height/2,height,height);}
    //MUTATORS
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setWidth(int w) {this.width = w;}
    public void setHeight(int h) {this.height = h;}
    public void setTextX(int x) {this.textX = x;}
    public void setTextY(int y) {this.textY = y;}
    public void setLineThickness(int t) {this.lineThickness = t;}

    public void setTickBlock(Runnable r) {this.tickBlock = r; }
    public void setDragBlock(Runnable r) {this.dragBlock = r;}

    public void setText(String s) {this.text = s;}

    public void setTextCentered(boolean b) {this.textCentered = b;}

    public void setClickable(boolean b) {this.isClickable = b;}
    public void setVisible(boolean b) {this.isVisible = b;}
    public void setMouseOver(Boolean b) {mouseOver = b;}

    public void setColor(Color c) {this.color = c;}

    public void setFontColor(Color c) {this.fontColor = c;}

    public void setFont(Font f) {this.font = f;}

    //ACCESSORS
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getWidth() {return this.width;}
    public int getHeight() {return this.height;}

    public Color getColor() {return this.color;}
    public Color getFontColor() {return this.fontColor;}

    public boolean getMouseOver() {return this.mouseOver;}
    public boolean getIsClickable() {return this.isClickable;}
    public boolean getIsVisible() {return this.isVisible;}


}
