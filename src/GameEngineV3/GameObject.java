package GameEngineV3;

import java.awt.*;

public abstract class GameObject {
    public abstract void tick(Game game);
    public abstract void render(Graphics g);
}
