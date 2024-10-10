package _2048;

import GameEngineV3.Tools.Methods;

import java.awt.*;

public class Tile {
    /*
    The tile class is ONLY used for pleasant graphical representation of the board
    Tiles are not even aware of their index on the board besides literal coordinates
    Also used to handle merging in a pleasant animation
     */
    public static final int totalMoveTime = 75;

    public int x, y;
    double size;

    public Tile merger;

    private boolean changingX; //True for changingX, False for changingY
    private int startingPosition; //Refers to starting x or y position based on what needs to change
    private long moveTime; //Time that the movement began

    //CONSTRUCTORS
    public Tile(Point p) {
        this(p.x,p.y);
    }
    public Tile(int x, int y) {
        this(x,y,Board.TILE_SIZE);
    }
    public Tile(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;

        this.moveTime = -1;
    }

    //MUTATORS
    public void updateTile(int p) {// https://www.desmos.com/calculator/kn9tpwdan5
        if (moveTime!=-1) { //If movement is already in progress
            if (System.currentTimeMillis()-moveTime>totalMoveTime) {
                if (changingX) x = Board.getTilePx(p % 4);
                else y = Board.getTilePy(p/4);
                moveTime = -1;
            }
            else {
                int totalDistance;
                if (changingX) totalDistance = Board.getTilePx(p%4)-startingPosition;
                else totalDistance = Board.getTilePy(p/4)-startingPosition;

                double progress = 100*((System.currentTimeMillis() - moveTime) / (double) totalMoveTime); //Value between 0 and 1
                int distance = (int) (totalDistance / (1 + Math.pow(Math.E, 5 + (-0.1 * progress))));

                if (changingX) x = startingPosition + distance;
                else y = startingPosition + distance;
            }
        }
        else if (!tileInPosition(p)) { //If tile is not in position, initiate move
            moveTime = System.currentTimeMillis();
            changingX = !xInPosition(p);
            if (changingX) startingPosition = x;
            else startingPosition = y;
        }
        //UPDATE SIZE
        if (Math.abs(size-Board.TILE_SIZE)<5) size = Board.TILE_SIZE;
        if (size>Board.TILE_SIZE) size/=1.015;
        if (size<Board.TILE_SIZE) size*=1.1;
        //UPDATE MERGER
        if (merger!=null) {
            if (!merger.tileInPosition(p)) merger.updateTile(p);
            else {
                merger = null;
                size = (int) (Board.TILE_SIZE * 1.15);
            }
        }
    }
    public void forceIntoPosition(int p) {
        if (merger!=null) {
            merger = null;
            size = (int) (Board.TILE_SIZE * 1.15);
        }
        x = Board.getTilePx(p % 4);
        y = Board.getTilePy(p/4);
        moveTime = -1;
    }
    public void render(Graphics g, int value) {
        if (merger!=null) {
            value/=2;
            merger.render(g,value);
        }
        Rectangle tile = getCenteredRectInSquare(new Rectangle(x+450,y,Board.TILE_SIZE,Board.TILE_SIZE),(int)size);

        g.setColor(getTileColor(value));
        g.fillRoundRect(tile.x,tile.y,tile.width,tile.height,8,8);

        g.setColor(new Color(249,246,242));
        g.setFont(new Font("Century Gothic", Font.BOLD, (int)(getFontSize(value)*(size/(double)Board.TILE_SIZE))));
        Methods.drawCenteredString(g,Integer.toString(value),tile);
    }

    //HELPERS
    private boolean xInPosition(int p) {
        return Board.getTilePx(p % 4) == x;
    }
    private boolean yInPosition(int p) {
        return Board.getTilePy(p / 4) == y;
    }
    private static Rectangle getCenteredRectInSquare(Rectangle rect, int size) {
        return new Rectangle(rect.x+(rect.width-size)/2,rect.y+(rect.height-size)/2,size,size);
    }

    //STATIC ACCESSORS
    public static Color getTileColor(int value) { //https://coolors.co/305252-373e40-77878b-488286-b7d5d4-ff715b-fa7e61-fac8cd-edcbb1-fee1c7
        if (value==2) return new Color(77, 126, 168);
        else if (value==4) return new Color(77, 148, 153);
        else if (value==8) return new Color(121, 73, 125);
        else if (value==16) return new Color(81, 52, 77);
        else if (value==32) return new Color(109, 47, 144);
        else if (value==64) return new Color(138, 28, 124);
        else if (value==128) return new Color(255, 89, 94);
        else if (value==256) return new Color(250, 126, 97);
        else if (value==512) return new Color(250, 105, 95);
        else if (value==1024) return new Color(255, 74, 48);
        else if (value==2048) return new Color(231, 172, 48);
        else  return new Color(199, 145, 15);
    }
    public static int getFontSize(int value) {
        if (Methods.getLength(value)==1) return 50;
        else if (Methods.getLength(value)==2) return 45;
        else if (Methods.getLength(value)==3) return 40;
        else if (Methods.getLength(value)==4) return 35;
        else return 30;
    }
    //ACCESSORS
    public boolean tileInPosition(int p) {
        return xInPosition(p) & yInPosition(p) & merger==null;
    }
    public String toString() {
        return "Tile:[x:"+ x + ", y:" + y + ", size:" + size + ", startingP: " + startingPosition + ", moveTime: " + moveTime + "]";
    }
}
