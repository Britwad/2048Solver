package GameEngineV3;

import java.awt.*;

public interface GameInterface {
    void initialize(Game game);
    void tick(Game game);
    void renderBackground(Graphics g);
    void render(Graphics g);
}
