package State;

import Main.GamePanel;
import TileMap.*;
import Utility.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class MenuState extends State {

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "New Game",
            "Continue",
            "High Score",
            "About",
            "Quit"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public MenuState(StateManager stateManager) {
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

        Utility.horizontalCenteredText(g, "PeeboJump", GamePanel.WIDTH, 60, titleFont, Color.black);

        Utility.horizontalCenteredText(g, "PeeboJump", GamePanel.WIDTH, 58, titleFont, titleColor);

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
        if (currentChoice == 1) {
            int currentLevel = StateManager.LEVEL1STATE;
            int latestLevel = SaveData.readLatestLevel();

            if (latestLevel == 1) {
                currentLevel = StateManager.LEVEL1STATE;
            }
            else if (latestLevel == 2) {
                currentLevel = StateManager.LEVEL2STATE;
            }
            else if (latestLevel == 3) {
                currentLevel = stateManager.LEVEL3STATE;
            }

            stateManager.setState(currentLevel);
        }
        if (currentChoice == 2) {
            stateManager.setState(StateManager.HIGHSCORESTATE);
        }
        if (currentChoice == 3) {
            stateManager.setState(StateManager.ABOUTSTATE);
        }
        if (currentChoice == 4) {
            System.exit(0);
        }

    }
}
