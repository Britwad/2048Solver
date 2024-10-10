package _2048;

import GameEngineV3.Game;
import GameEngineV3.GameObject;
import GameEngineV3.Tools.Methods;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Board extends GameObject {

    public static final int BOARD_X = 25, BOARD_Y = 125, BOARD_SIZE = 400, TILE_GAP = 5, TILE_SIZE = (BOARD_SIZE-TILE_GAP*5)/4;

    private final int[] grid;
    private final Tile[] tileGraphics;
    private int score;
    boolean tileAdded;

    public Board() {
        this.grid = new int[16];
        this.tileGraphics = new Tile[16];
        this.score = 0;
        this.tileAdded = true;
    }

    public void tick(Game game) {
        if (tilesInPosition() & !tileAdded) {
            addTile();
        }

        //Update Tiles
        for (int i = 0;i<16;i++) {
            if (tileGraphics[i]!=null) {
                tileGraphics[i].updateTile(i);
            }
        }
    }
    public void render(Graphics g) {
        //BOARD
        g.setColor(_2048.boardColor);
        g.fillRoundRect(BOARD_X,BOARD_Y, BOARD_SIZE, BOARD_SIZE,11,11);
        g.fillRoundRect(BOARD_X+450,BOARD_Y, BOARD_SIZE, BOARD_SIZE,11,11);

        //EMPTY TIlES
        g.setColor(_2048.boardTileColor);
        for (int y = 0;y<4;y++) {
            for (int x = 0;x<4;x++) {
                g.fillRoundRect(getTilePx(x),getTilePy(y),TILE_SIZE,TILE_SIZE,8,8);
                g.fillRoundRect(getTilePx(x)+450,getTilePy(y),TILE_SIZE,TILE_SIZE,8,8);
            }
        }

        //GAME TILES
        for (int i = 0;i<16;i++) {
            if (grid[i]!=0) {
                g.setColor(Tile.getTileColor(grid[i]));
                g.fillRoundRect(getTilePx(i%4),getTilePy(i/4),TILE_SIZE,TILE_SIZE,8,8);

                g.setColor(new Color(249,246,242));
                g.setFont(new Font("Century Gothic", Font.BOLD, Tile.getFontSize(grid[i])));
                Methods.drawCenteredString(g,Integer.toString(grid[i]),new Rectangle(getTilePx(i%4), getTilePy(i/4),TILE_SIZE,TILE_SIZE));

                tileGraphics[i].render(g,grid[i]);
            }
        }

        //Stats
        g.setFont(new Font("Century Gothic", Font.BOLD, 10));
        g.drawString(toString(),10,540);
        g.drawString(tilePositionsToString(),10,550);
        g.drawString("tileAdded: " + tileAdded,10,560);
        g.drawString("tilesInPosition: " + tilesInPosition(),10,570);
        g.drawString("availableMoves: " + getAvailableMoves(grid),10,580);
    }

    //MUTATORS
    public void addTile() { //Adds a tile to an empty board position
        addTile(Math.random()<.9?2:4);
    }
    public void addTile(int value) { //Adds a tile to an empty board position
        ArrayList<Integer> emptyPositions = getAvailableSpots(grid);
        if (emptyPositions.size()!=0) addTile(value,emptyPositions.get((int)(Math.random()*emptyPositions.size())));
    }
    public void addTile(int value, int position) { //Inserts a tile of value at position
        grid[position] = value;
        tileGraphics[position] = new Tile(getTilePx(position%4),getTilePy(position/4),(int)(TILE_SIZE*.5));
        tileAdded = true;
    }
    //HELPERS
    public void move(int dir) {
        forceMoveTileGraphics();
        score+=move(grid, tileGraphics, dir);
    }
    private static int move(int[] grid, Tile[] tileGraphics, int dir) { //To isolated grid effects, keep tileGraphics null
        int moveScore = 0;
        int[] starting;
        boolean[] mergeActive = new boolean[16];
        //1 = right, -1 =left, 4 = down, -4 = up
        if (dir==1) starting = new int[]{2,6,10,14};
        else if (dir==-1) starting = new int[]{1,5,9,13};
        else if (dir==4) starting = new int[]{8,9,10,11};
        else starting = new int[]{4,5,6,7};
        for (int start: starting) {
            for (int i = 0;i<3;i++) {
                int index = start-i*dir;

                int value = grid[index];
                if (value!=0) {
                    for (int pos = index + dir; (dir < 0) ? pos >= start + dir : pos <= start + dir; pos += dir) {
                        if (grid[pos] != 0) {
                            if (grid[pos]==value&!mergeActive[pos]) { //MERGE
                                grid[pos] *= 2;
                                grid[index] = 0;
                                if (tileGraphics!=null) {
                                    tileGraphics[pos].merger = tileGraphics[index];
                                    tileGraphics[index] = null;
                                }
                                mergeActive[pos] = true;
                                moveScore+=grid[pos];
                            }
                            else if (pos!=index+dir){ //HIT ANOTHER TILE
                                grid[pos-dir] = grid[index];
                                grid[index] = 0;
                                if (tileGraphics!=null) {
                                    tileGraphics[pos - dir] = tileGraphics[index];
                                    tileGraphics[index] = null;
                                }
                            }
                            break;
                        }
                        else if (pos==start+dir) { //TILE REACHED THE END
                            grid[pos] = grid[index];
                            grid[index] = 0;
                            if (tileGraphics!=null) {
                                tileGraphics[pos] = tileGraphics[index];
                                tileGraphics[index] = null;
                            }
                        }
                    }
                }
            }

        }
        return moveScore;
    }
    public boolean ableToMove() {
        return ableToMove(4) | ableToMove(1) | ableToMove(-1) | ableToMove(-4);
    }
    public boolean ableToMove(int dir) {
        if (dir==4|dir==1|dir==-1|dir==-4) return ableToMove(grid,dir);
        return false;
    }
    private static boolean ableToMove(int[] grid, int dir) {
        int[] starting;
        //1 = right, -1 =left, 4 = down, -4 = up
        if (dir==1) starting = new int[]{2,6,10,14};
        else if (dir==-1) starting = new int[]{1,5,9,13};
        else if (dir==4) starting = new int[]{8,9,10,11};
        else starting = new int[]{4,5,6,7};
        for (int start: starting) {
            for (int i = 0;i<3;i++) {
                int index = start-i*dir;
                int value = grid[index];
                if (value!=0) {
                    for (int pos = index + dir; (dir < 0) ? pos >= start + dir : pos <= start + dir; pos += dir) {
                        if (grid[pos] != 0) {
                            if (grid[pos]==value) { //MERGE
                                return true;
                            }
                            else if (pos!=index+dir){ //HIT ANOTHER TILE
                                return true;
                            }
                            break;
                        }
                        else if (pos==start+dir) { //TILE REACHED THE END
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }
    public static boolean ableToMove(int[] grid) {
        return ableToMove(grid,4) | ableToMove(grid,1) | ableToMove(grid,-1) | ableToMove(grid,-4);
    }
    private static ArrayList<Integer> getAvailableMoves(int[] grid) {
        ArrayList<Integer> moves = new ArrayList<>();
        if (ableToMove(grid,-4)) moves.add(-4);
        if (ableToMove(grid,-1)) moves.add(-1);
        if (ableToMove(grid,1)) moves.add(1);
        if (ableToMove(grid,4)) moves.add(4);
        return moves;
    }


    void forceMoveTileGraphics() {
        for (int i = 0;i<16;i++)
            if (tileGraphics[i]!=null)
                tileGraphics[i].forceIntoPosition(i);
    }
    boolean tilesInPosition() {
        for (int i = 0;i<16;i++)
            if (tileGraphics[i]!=null)
                if (!tileGraphics[i].tileInPosition(i))
                    return false;
        return true;
    }
    private static ArrayList<Integer> getAvailableSpots(int[] grid) {
        ArrayList<Integer> spots = new ArrayList<>(grid.length);
        for (int i = 0;i<grid.length;i++) if (grid[i]==0)spots.add(i);
        return spots;
    }

    //ACCESSORS
    public static int getTilePx(int x) {
        return BOARD_X+TILE_GAP + (TILE_GAP+TILE_SIZE)*x;
    }
    public static int getTilePy(int y) {
        return BOARD_Y+TILE_GAP + (TILE_GAP+TILE_SIZE)*y;
    }
    public int tileCount() {
        int count = 0;
        for (int n: grid) {
            if (n!=0) count++;
        }
        return count;
    }

    public String toString() {
        return Arrays.toString(grid);
    }
    public String tilePositionsToString() {
        return Arrays.toString(tileGraphics);
    }
    public int getScore() {
        return this.score;
    }
    public int[] getGrid() {
        return this.grid;
    }

    //SOLVER CODE-------------------------------------------------------------------------------------------------------

    //TEST RUNS
    public int bestMove(int trialMeasure, boolean timeBased) { //Returns best out of four move options
        ArrayList<Integer> moves = getAvailableMoves(grid);
        long[] moveScores = new long[moves.size()];

        long startTime = System.currentTimeMillis();
        int trialCount = 0;

        while (timeBased?startTime+trialMeasure>System.currentTimeMillis():trialCount<trialMeasure) {
            for (int m = 0;m<moves.size();m++) {
                moveScores[m] += scoreRandomMoves(Arrays.copyOf(grid, 16), moves.get(m));
            }
            trialCount+=4;
        }

        //WEIGHTS
        for (int i = 0;i<moves.size();i++) {
            if (moves.get(i)==-4) moveScores[i] *=1; //UP
            if (moves.get(i)==-1) moveScores[i] *=1; //LEFT
            if (moves.get(i)==4) moveScores[i] *=1; //DOWN
            if (moves.get(i)==1) moveScores[i] *=1; //RIGHT
        }

        System.out.println(trialCount + " games | " + Arrays.toString(moveScores) + " | Possible Moves: " + moves + " | Decision: " + moves.get(maxIndex(moveScores)) + " | Time Taken: " + (System.currentTimeMillis()-startTime));

        return moves.get(maxIndex(moveScores));
    }
    /*
    private void bestMove(int[] grid, int dir, int depth, long[] moveScores) { //Gets score from all four moves O(4^N)
        if (Board.ableToMove(grid)) {
            for (int m: MOVES) {
                if (Board.ableToMove(grid, m)) {
                    for (int p : getAvailableSpots(grid)) {
                        if (depth > 0) {
                            for (int i = 0; i < 9; i++) {
                                bestMove(testMoveGrid(grid,m,2,p),dir,depth-1,moveScores);
                            }
                            bestMove(testMoveGrid(grid,m,4,p),dir,depth-1,moveScores);
                        }
                        else {
                            for (int i = 0; i < 9; i++) {
                                moveScores[dir]+= scoreRandomMoves(testMoveGrid(grid,m,2,p),dir);
                            }
                            moveScores[dir]+= scoreRandomMoves(testMoveGrid(grid,m,4,p),dir);
                        }
                    }
                }
            }
        }
        else {
            moveScores[dir] += Board.calculateScore(grid);
        }
    }
     */
    private int scoreRandomMoves(int[] grid, int dir) { //Plays game out with random moves and returns score when finished
        while (Board.ableToMove(grid,dir)) {
            Board.move(grid,null,dir);
            addTileToGrid(grid);

            ArrayList<Integer> moves = getAvailableMoves(grid);
            if (moves.size()>0) {
                dir = moves.get((int)(Math.random()*moves.size()));
            }
        }
        return Board.calculateScore(grid);
    }

    //HELPERS
    private int[] testMoveGrid(int[] grid, int dir, int value, int p) {
        int[] test = Arrays.copyOf(grid,16);
        Board.move(test,null,dir);
        addTileToGrid(grid,value,p);
        return test;
    }

    public static int calculateScore(int[] grid) { //Returns approximation of score ||||||MAKE PRIVATE
        int score = 0;
        for (int i: grid) {
            if (i>2) {
                int n = (int)(Math.log(i) / Math.log(2));
                score+= (n-1) * Math.pow(2,n);
            }
        }
        return score + rateTilePositions(grid);
    }
    private static int rateTilePositions(int[] grid) {
        int score = 0;
//        int lastValue = grid[0];
//        for (int i = 1;i<16;i++) {
//            if (i>=lastValue) score +=100;
//        }
        //if (maxValue(grid)==grid[15]) score+=20000;
        return score;
    }

    private static int maxIndex(long[] a) {
        long maxValue = 0;
        int maxIndex = 0;
        for (int i = 0;i<a.length;i++) {
            if (a[i]>maxValue) {
                maxIndex = i;
                maxValue = a[i];
            }
        }
        return maxIndex;
    }
    private static int maxValue(int[] a) {
        int maxValue = a[0];
        for (int i = 1;i<a.length;i++) {
            maxValue = Math.max(maxValue,a[i]);
        }
        return maxValue;
    }

    private static void addTileToGrid(int[] grid) {
        addTileToGrid(grid,Math.random()<.9?2:4);
    }
    private static void addTileToGrid(int[] grid, int value) {
        ArrayList<Integer> emptyPositions = getAvailableSpots(grid);
        if (emptyPositions.size()!=0) {
            addTileToGrid(grid,value,emptyPositions.get((int)(Math.random()*emptyPositions.size())));
        }
    }
    private static void addTileToGrid(int[] grid, int value, int position) {
        grid[position] = value;
    }
}
