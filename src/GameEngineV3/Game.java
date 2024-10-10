package GameEngineV3;

import GameEngineV3.Tools.OptionBox;
import GameEngineV3.Tools.OptionSlider;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Queue;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private final static int TPS = 60;

    public final int windowWidth, windowHeight;
    private final GameInterface gameInterface;
    private final Window window;
    private final Thread thread;
    
    private final LinkedList<GameObject> gameObjects;
    private final Queue<GameObject> gameObjectAddQueue;
    private final Queue<GameObject> gameObjectRemoveQueue;

    private boolean running;
    private Color backgroundColor;
    private int mouseX, mouseY;
    private final boolean[] mouseDown, keysDown;

    public Game(GameInterface g, int width, int height, String title) {
        this(g,width,height,title,0,false);
    }
    public Game(GameInterface g, int width, int height, String title, int monitor, boolean centered) {
        this.gameInterface = g;
        this.windowWidth = width;
        this.windowHeight = height;

        this.backgroundColor = Color.white;
        mouseDown = new boolean[5];
        keysDown = new boolean[255];

        gameObjects = new LinkedList<>();
        gameObjectRemoveQueue = new LinkedList<>();
        gameObjectAddQueue = new LinkedList<>();

        g.initialize(this);

        this.thread = new Thread(this);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.window = new Window(width,height,title,this,monitor,centered);
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double ns = 1000000000f/TPS;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta+= (now-lastTime)/ns;
            lastTime = now;
            while (delta>=1) {
                tick();
                delta--;
            }
            if (running) render();
        }
        stop();
    }

    private void tick() {
        for (GameObject o: gameObjects) o.tick(this);
        gameInterface.tick(this);

        while (!gameObjectRemoveQueue.isEmpty()) {
            gameObjects.remove(gameObjectRemoveQueue.remove());
        }
        while (!gameObjectAddQueue.isEmpty()) {
            GameObject o = gameObjectAddQueue.remove();
            if (o instanceof OptionBox | o instanceof OptionSlider) {
                gameObjects.addFirst(o);
            }
            else {
                gameObjects.addLast(o);
            }
        }
    }
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs==null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(backgroundColor);
        g.fillRect(0,0,windowWidth,windowHeight);

        gameInterface.renderBackground(g);

        for (GameObject o: gameObjects) o.render(g);
        gameInterface.render(g);

        g.dispose();
        bs.show();
    }

    void start() {
        thread.start();
        running = true;
    }
    private void stop() {
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        running = false;
    }

    //MUTATORS
    public void clearObjects() {
        gameObjects.clear();
    }
    public void addObject(GameObject o) {gameObjectAddQueue.add(o);}
    public void removeObject(GameObject o) {gameObjectRemoveQueue.add(o);}

    public void setBackgroundColor(Color color) {this.backgroundColor = color;}
    public void setCursortype(int cursor) {window.frame.setCursor(cursor);}
    //ACCESSORS
    public boolean keyDown(int e) {return keysDown[e];}
    public boolean mouseDown(int i) {
        return (i<mouseDown.length & mouseDown[i]);
    }
    public int getMouseX() {return this.mouseX;}
    public int getMouseY() {return this.mouseY;}
    public int getCursorType() {return window.frame.getCursor().getType();}
    public int getNumber() {
        if (keysDown[KeyEvent.VK_0]||keysDown[KeyEvent.VK_SPACE]||keysDown[KeyEvent.VK_NUMPAD0]) return 0;
        if (keysDown[KeyEvent.VK_1]||keysDown[KeyEvent.VK_NUMPAD1]) return 1;
        if (keysDown[KeyEvent.VK_2]||keysDown[KeyEvent.VK_NUMPAD2]) return 2;
        if (keysDown[KeyEvent.VK_3]||keysDown[KeyEvent.VK_NUMPAD3]) return 3;
        if (keysDown[KeyEvent.VK_4]||keysDown[KeyEvent.VK_NUMPAD4]) return 4;
        if (keysDown[KeyEvent.VK_5]||keysDown[KeyEvent.VK_NUMPAD5]) return 5;
        if (keysDown[KeyEvent.VK_6]||keysDown[KeyEvent.VK_NUMPAD6]) return 6;
        if (keysDown[KeyEvent.VK_7]||keysDown[KeyEvent.VK_NUMPAD7]) return 7;
        if (keysDown[KeyEvent.VK_8]||keysDown[KeyEvent.VK_NUMPAD8]) return 8;
        if (keysDown[KeyEvent.VK_9]||keysDown[KeyEvent.VK_NUMPAD9]) return 9;
        if (keysDown[KeyEvent.VK_BACK_SPACE]) return -2;
        return -1;
    }
    public String getLetter() {
        if (keysDown[KeyEvent.VK_0]||keysDown[KeyEvent.VK_NUMPAD0]) return "0";
        if (keysDown[KeyEvent.VK_1]||keysDown[KeyEvent.VK_NUMPAD1]) return "1";
        if (keysDown[KeyEvent.VK_2]||keysDown[KeyEvent.VK_NUMPAD2]) return "2";
        if (keysDown[KeyEvent.VK_3]||keysDown[KeyEvent.VK_NUMPAD3]) return "3";
        if (keysDown[KeyEvent.VK_4]||keysDown[KeyEvent.VK_NUMPAD4]) return "4";
        if (keysDown[KeyEvent.VK_5]||keysDown[KeyEvent.VK_NUMPAD5]) return "5";
        if (keysDown[KeyEvent.VK_6]||keysDown[KeyEvent.VK_NUMPAD6]) return "6";
        if (keysDown[KeyEvent.VK_7]||keysDown[KeyEvent.VK_NUMPAD7]) return "7";
        if (keysDown[KeyEvent.VK_8]||keysDown[KeyEvent.VK_NUMPAD8]) return "8";
        if (keysDown[KeyEvent.VK_9]||keysDown[KeyEvent.VK_NUMPAD9]) return "9";

        if (keysDown[KeyEvent.VK_A]&keysDown[KeyEvent.VK_SHIFT]) return "A";
        if (keysDown[KeyEvent.VK_A]) return "a";
        if (keysDown[KeyEvent.VK_B]&keysDown[KeyEvent.VK_SHIFT]) return "B";
        if (keysDown[KeyEvent.VK_B]) return "b";
        if (keysDown[KeyEvent.VK_C]&keysDown[KeyEvent.VK_SHIFT]) return "C";
        if (keysDown[KeyEvent.VK_C]) return "c";
        if (keysDown[KeyEvent.VK_D]&keysDown[KeyEvent.VK_SHIFT]) return "D";
        if (keysDown[KeyEvent.VK_D]) return "d";
        if (keysDown[KeyEvent.VK_E]&keysDown[KeyEvent.VK_SHIFT]) return "E";
        if (keysDown[KeyEvent.VK_E]) return "e";
        if (keysDown[KeyEvent.VK_F]&keysDown[KeyEvent.VK_SHIFT]) return "F";
        if (keysDown[KeyEvent.VK_F]) return "f";
        if (keysDown[KeyEvent.VK_G]&keysDown[KeyEvent.VK_SHIFT]) return "G";
        if (keysDown[KeyEvent.VK_G]) return "g";
        if (keysDown[KeyEvent.VK_H]&keysDown[KeyEvent.VK_SHIFT]) return "H";
        if (keysDown[KeyEvent.VK_H]) return "h";
        if (keysDown[KeyEvent.VK_I]&keysDown[KeyEvent.VK_SHIFT]) return "I";
        if (keysDown[KeyEvent.VK_I]) return "i";
        if (keysDown[KeyEvent.VK_J]&keysDown[KeyEvent.VK_SHIFT]) return "J";
        if (keysDown[KeyEvent.VK_J]) return "j";
        if (keysDown[KeyEvent.VK_K]&keysDown[KeyEvent.VK_SHIFT]) return "K";
        if (keysDown[KeyEvent.VK_K]) return "k";
        if (keysDown[KeyEvent.VK_L]&keysDown[KeyEvent.VK_SHIFT]) return "L";
        if (keysDown[KeyEvent.VK_L]) return "l";
        if (keysDown[KeyEvent.VK_M]&keysDown[KeyEvent.VK_SHIFT]) return "M";
        if (keysDown[KeyEvent.VK_M]) return "m";
        if (keysDown[KeyEvent.VK_N]&keysDown[KeyEvent.VK_SHIFT]) return "N";
        if (keysDown[KeyEvent.VK_N]) return "n";
        if (keysDown[KeyEvent.VK_O]&keysDown[KeyEvent.VK_SHIFT]) return "O";
        if (keysDown[KeyEvent.VK_O]) return "o";
        if (keysDown[KeyEvent.VK_P]&keysDown[KeyEvent.VK_SHIFT]) return "P";
        if (keysDown[KeyEvent.VK_P]) return "p";
        if (keysDown[KeyEvent.VK_Q]&keysDown[KeyEvent.VK_SHIFT]) return "Q";
        if (keysDown[KeyEvent.VK_Q]) return "q";
        if (keysDown[KeyEvent.VK_R]&keysDown[KeyEvent.VK_SHIFT]) return "R";
        if (keysDown[KeyEvent.VK_R]) return "r";
        if (keysDown[KeyEvent.VK_S]&keysDown[KeyEvent.VK_SHIFT]) return "S";
        if (keysDown[KeyEvent.VK_S]) return "s";
        if (keysDown[KeyEvent.VK_T]&keysDown[KeyEvent.VK_SHIFT]) return "T";
        if (keysDown[KeyEvent.VK_T]) return "t";
        if (keysDown[KeyEvent.VK_U]&keysDown[KeyEvent.VK_SHIFT]) return "U";
        if (keysDown[KeyEvent.VK_U]) return "u";
        if (keysDown[KeyEvent.VK_V]&keysDown[KeyEvent.VK_SHIFT]) return "V";
        if (keysDown[KeyEvent.VK_V]) return "v";
        if (keysDown[KeyEvent.VK_W]&keysDown[KeyEvent.VK_SHIFT]) return "W";
        if (keysDown[KeyEvent.VK_W]) return "w";
        if (keysDown[KeyEvent.VK_X]&keysDown[KeyEvent.VK_SHIFT]) return "X";
        if (keysDown[KeyEvent.VK_X]) return "x";
        if (keysDown[KeyEvent.VK_Y]&keysDown[KeyEvent.VK_SHIFT]) return "Y";
        if (keysDown[KeyEvent.VK_Y]) return "y";
        if (keysDown[KeyEvent.VK_Z]&keysDown[KeyEvent.VK_SHIFT]) return "Z";
        if (keysDown[KeyEvent.VK_Z]) return "z";
        if (keysDown[KeyEvent.VK_MINUS]&keysDown[KeyEvent.VK_SHIFT]) return "_";
        if (keysDown[KeyEvent.VK_SPACE]) return " ";

        return "";
    }
    //-------------------------------------------- Key and Mouse Input --------------------------------------------

    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key<=255) keysDown[key] = true;
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key<=255) keysDown[key] = false;
    }
    public void mouseClicked(MouseEvent e) {
        for (GameObject o : gameObjects) {
            if (o instanceof OptionBox) {
                ((OptionBox) o).clicked();
            } else break;
        }
    }
    public void mousePressed(MouseEvent e) {
        mouseDown[e.getButton()-1]=true;
    }
    public void mouseReleased(MouseEvent e) {
        mouseDown[e.getButton()-1]=false;
    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        for (GameObject o : gameObjects) {
            if (o instanceof OptionBox) {
                OptionBox b = (OptionBox) o;
                b.setMouseOver(mouseX > b.getX() & mouseX < b.getX() + b.getWidth() &
                        mouseY > b.getY() & mouseY < b.getY() + b.getHeight());
            }
            else if (o instanceof OptionSlider) {
                OptionSlider s = (OptionSlider) o;
                s.setMouseOver(mouseX > s.getRect().x & mouseX < s.getRect().x + s.getRect().width &
                        mouseY > s.getRect().y & mouseY < s.getRect().y + s.getRect().height);
            } else break;
        }
    }
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        for (GameObject o : gameObjects) {
            if (o instanceof OptionBox) {
                OptionBox b = (OptionBox) o;
                b.setMouseOver(mouseX > b.getX() & mouseX < b.getX() + b.getWidth() &
                        mouseY > b.getY() & mouseY < b.getY() + b.getHeight());
            }
            else if (o instanceof OptionSlider) {
                OptionSlider s = (OptionSlider) o;
                s.setMouseOver(mouseX > s.getRect().x & mouseX < s.getRect().x + s.getRect().width &
                        mouseY > s.getRect().y & mouseY < s.getRect().y + s.getRect().height);
            } else break;
        }
    }


}
