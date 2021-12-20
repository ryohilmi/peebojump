package State;

import Main.GamePanel;
import TileMap.*;
import Utility.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class DeathState extends State {

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "Retry",
            "Main Menu",
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public DeathState(StateManager stateManager) {
        this.stateManager = stateManager;

        try {

            bg = new Background("/Backgrounds/inverted-background.png");
            bg.setVector(-4, 0);

            Font zero4b = Font.createFont(Font.TRUETYPE_FONT, new File(Objects.requireNonNull(getClass().getResource("/Font/04b.TTF")).getPath()));
            Font minecraft = Font.createFont(Font.TRUETYPE_FONT, new File(Objects.requireNonNull(getClass().getResource("/Font/Minecraft.ttf")).getPath()));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(zero4b);
            ge.registerFont(minecraft);

            titleColor = new Color(255, 0,0 );
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

        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0,0, GamePanel.WIDTH, GamePanel.HEIGHT);

        Utility.horizontalCenteredText(g, "YOU DIED", GamePanel.WIDTH, 60, titleFont, Color.black);

        Utility.horizontalCenteredText(g, "YOU DIED", GamePanel.WIDTH, 58, titleFont, titleColor);

        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) {
                g.setColor(Color.WHITE);
                g.setFont(g.getFont().deriveFont(18.0f));
            } else {
                g.setColor(new Color(239, 71, 111));
                g.setFont(g.getFont().deriveFont(16.0f));
            }

            Utility.horizontalCenteredText(g, options[i], GamePanel.WIDTH, 110 + i * 32, g.getFont(), g.getColor());
        }
    }

    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }

        if (k == KeyEvent.VK_UP) {
            currentChoice = currentChoice - 1 == -1 ? options.length - 1 : currentChoice - 1;
        }

        if (k == KeyEvent.VK_DOWN) {
            currentChoice = currentChoice + 1 == options.length ? 0 : currentChoice + 1;
        }
    }

    public void keyReleased(int k) {

    }

    private void select() {
        if (currentChoice == 0) {
            stateManager.setState(StateManager.LEVEL1STATE);
        }
        if (currentChoice == 2) {
            System.exit(0);
        }

    }
}
