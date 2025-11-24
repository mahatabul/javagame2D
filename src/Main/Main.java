package Main;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.util.Objects;


public class Main {
    public static JFrame window;

    public static void main(String[] args) {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        BufferedImage icon;
        try {
            icon =  ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        gamePanel.config.loadConfig();
        window.setTitle("Re:Zero RPG");
        window.setIconImage(icon);
        window.setUndecorated(true);


        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}