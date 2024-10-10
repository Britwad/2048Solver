package _2048;

import GameEngineV3.Game;
import GameEngineV3.GameInterface;
import GameEngineV3.Tools.OptionBox;

import java.awt.*;
import java.awt.event.KeyEvent;

public class _2048 implements GameInterface {

    /*
    TO-DO:
    - UI
    - Score Display
    - RECURSIVE
    NOTES:
     - Consider Solving for depth rather than score
     - Consider Solving with comparison of score sums with non-random attempts
     - Consider adding an additional component on scores, grading how close the grid is to being sorted (with emphasis on the largest tile being in slot 15)
     */

    private Board board;
    private int lastKey;
    private boolean solverOn;

    public static final Color backgroundColor = new Color(34, 34, 34),
            boardColor = new Color(231, 236, 239),
            boardTileColor = new Color(153, 156, 158);

    public static void main(String[] args) {
        new Game(new _2048(),900,600,"2048");
    }

    public void initialize(Game game) {
        game.setBackgroundColor(backgroundColor);

        board = new Board();
        game.addObject(board);

        board.addTile();
        board.addTile();

//        board.addTile(2,0);
//        board.addTile(4,1);
//        board.addTile(8,2);
//        board.addTile(16,3);
//        board.addTile(32,4);
//        board.addTile(64,5);
//        board.addTile(128,6);
//        board.addTile(256,7);
//        board.addTile(512,7);
//        board.addTile(1024,8);
//        board.addTile(2048,9);
//        board.addTile(4096,10);

        OptionBox mouseCoords = new OptionBox(new Rectangle(1,1,40,20));
        mouseCoords.setTickBlock(() -> mouseCoords.setText(game.getMouseX() + ", " + game.getMouseY()));
        mouseCoords.setFont(new Font("Century Gothic", Font.BOLD, 10));
        mouseCoords.setFontColor(Color.white);
        game.addObject(mouseCoords);

        solverOn = true;
    }

    public void tick(Game game) {
        if (solverOn) {
            //System.out.println("AbleToMove: " + board.ableToMove() + ", TileAdded: " + board.tileAdded + ", TilesInPosition: " + board.tilesInPosition());
            if (board.ableToMove()&board.tileAdded&board.tilesInPosition()) {
                board.move(board.bestMove(5,true));//(int)(0.015*Math.pow(board.tileCount(),4)),true)); //Time spent increases exponentially with risk
                board.tileAdded = false;
            }
        }
        else {
            if (lastKey == -1 & board.ableToMove(keyEventToDirection(arrowKeyDown(game)))) {
                board.forceMoveTileGraphics();
                if (!board.tileAdded) board.addTile();

                board.move(keyEventToDirection(arrowKeyDown(game)));

                board.tileAdded = false;
                lastKey = arrowKeyDown(game);
            }
            if (lastKey != -1) {
                if (!game.keyDown(lastKey)) lastKey = -1;
            }
        }
    }
    public void renderBackground(Graphics g) {
        g.setColor(boardColor);
        g.setFont(new Font("century gothic",Font.BOLD,60));
        g.drawString("2048 Solver V2",Board.BOARD_X,60);

        g.setFont(new Font("century gothic",Font.BOLD,30));
        g.drawString("Score: " + board.getScore() + " | CalculatedScore: " + Board.calculateScore(board.getGrid()),Board.BOARD_X,100);
    }

    public void render(Graphics g) {

    }

    private int arrowKeyDown(Game game) {
        if (game.keyDown(KeyEvent.VK_UP))    return KeyEvent.VK_UP;
        if (game.keyDown(KeyEvent.VK_RIGHT)) return KeyEvent.VK_RIGHT;
        if (game.keyDown(KeyEvent.VK_DOWN))  return KeyEvent.VK_DOWN;
        if (game.keyDown(KeyEvent.VK_LEFT))  return KeyEvent.VK_LEFT;
        return -1;
    }
    private int keyEventToDirection(int i) {
        if (i==KeyEvent.VK_UP) return -4;
        if (i==KeyEvent.VK_RIGHT) return 1;
        if (i==KeyEvent.VK_DOWN) return 4;
        if (i==KeyEvent.VK_LEFT) return -1;
        return 0;
    }
}
