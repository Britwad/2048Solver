package GameEngineV3;

import javax.swing.*;
import java.awt.*;

public class Window extends Canvas {
    public JFrame frame;
    public Window(int width, int height, String title, Game game, int window, boolean centered) {
        frame = new JFrame(title);

        showOnScreen(window,frame);

        frame.setSize(width+14,height+37);
        frame.setResizable(false);

        if (centered) frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(game);
        frame.setVisible(true);

        game.start();
    }
    public void setResizeable(boolean b) {
        frame.setResizable(b);
    }

    public static void showOnScreen( int screen, JFrame frame ) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if( screen > -1 && screen < gd.length ) {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
        } else if( gd.length > 0 ) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
        } else {
            throw new RuntimeException( "No Screens Found" );
        }
    }

}
