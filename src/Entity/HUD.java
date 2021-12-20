package Entity;

import Entity.PlayerObject.Player;
import Main.GamePanel;
import Utility.Time;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class HUD {
	
	private final Player player;
	
	private BufferedImage healthImage;
	private BufferedImage energyImage;
	private Font font;

	private Time time;
	
	public HUD(Player p, Time time) {
		this.time = time;
		player = p;
		try {
			healthImage = ImageIO.read(
					Objects.requireNonNull(getClass().getResourceAsStream(
							"/HUD/heart.png"
					))
			);
			energyImage = ImageIO.read(
					Objects.requireNonNull(getClass().getResourceAsStream(
							"/HUD/energy.png"
					))
			);

			Font minecraft = Font.createFont(Font.TRUETYPE_FONT, new File(Objects.requireNonNull(getClass().getResource("/Font/Minecraft.ttf")).getPath()));

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(minecraft);

			font = new Font("Minecraft", Font.PLAIN, 11);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {

		g.setFont(font);

		g.setColor(Color.BLACK);

		String health = "HP: " + player.getHealth() + " / " + player.getMaxHealth();
		String energy = "MP: " + Math.round((int)(player.getFire() / 100.0) * 100)  + " / " + player.getMaxFire();

		g.drawString(health, 16, 16);
		g.drawString(energy, GamePanel.WIDTH - g.getFontMetrics().stringWidth(energy) - 14, 16);

		g.setColor(Color.WHITE);

		g.drawString(health, 15, 15);
		g.drawString(energy, GamePanel.WIDTH - g.getFontMetrics().stringWidth(energy) - 15, 15);

		String timestamp = time.getSecond() + "." +time.getMilisecond();
		g.setColor(Color.BLACK);
		g.drawString(timestamp, 180, 15);
		g.setColor(Color.WHITE);
		g.drawString(timestamp, 180, 16);

		for (int i = 0; i < player.getHealth(); i++) {
			g.drawImage(healthImage, i * 30 + 10, 25, null);
		}

		for (int i = 0; i < player.getFire() / 1000; i++) {
			g.drawImage(energyImage, GamePanel.WIDTH - (i * 30 + 30), 25, null);
		}
	}
	
}













