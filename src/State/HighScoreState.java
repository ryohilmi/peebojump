package State;

import Main.GamePanel;
import TileMap.*;
import Utility.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class HighScoreState extends State {

    private Background bg;

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public HighScoreState(StateManager stateManager) {
        this.stateManager = stateManager;

        try {

            bg = new Background("/Backgrounds/background.png");
            bg.setVector(-0.3, 0);

            Font zero4b = Font.createFont(Font.TRUETYPE_FONT, new File(Objects.requireNonNull(getClass().getResource("/Font/04b.TTF")).getPath()));
            Font minecraft = Font.createFont(Font.TRUETYPE_FONT, new File(Objects.requireNonNull(getClass().getResource("/Font/Minecraft.ttf")).getPath()));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(zero4b);
            ge.registerFont(minecraft);

            titleColor = new Color(255, 235, 72);
            titleFont = new Font(
                    "04b 30",
                    Font.PLAIN,
                    25);

            font = new Font("Minecraft", Font.PLAIN, 17);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
    }

    public void update() {
        bg.update();
    }

    public void draw(Graphics2D g) {

        bg.draw(g);

        Utility.horizontalCenteredText(g, "High Score", GamePanel.WIDTH, 60, titleFont, Color.black);

        Utility.horizontalCenteredText(g, "High Score", GamePanel.WIDTH, 58, titleFont, titleColor);

        g.setFont(font);

        String level1 = "Level 1: " + SaveData.readHighScore(SaveData.LEVEL1);
        String level2 = "Level 2: " + SaveData.readHighScore(SaveData.LEVEL2);
        String level3 = "Level 3: " + SaveData.readHighScore(SaveData.LEVEL3);


        g.setColor(Color.BLACK);

        Utility.horizontalCenteredText(g, level1, GamePanel.WIDTH, 112, g.getFont(), g.getColor());
        Utility.horizontalCenteredText(g, level2, GamePanel.WIDTH, 144, g.getFont(), g.getColor());
        Utility.horizontalCenteredText(g, level3, GamePanel.WIDTH, 178, g.getFont(), g.getColor());

        g.setColor(Color.WHITE);

        Utility.horizontalCenteredText(g, level1, GamePanel.WIDTH, 110, g.getFont(), g.getColor());
        Utility.horizontalCenteredText(g, level2, GamePanel.WIDTH, 142, g.getFont(), g.getColor());
        Utility.horizontalCenteredText(g, level3, GamePanel.WIDTH, 176, g.getFont(), g.getColor());

    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            stateManager.setState(StateManager.MENUSTATE);
        }
    }

    public void keyReleased(int k) {

    }
}
