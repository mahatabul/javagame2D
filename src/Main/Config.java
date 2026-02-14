package Main;

import java.io.*;

public class Config {
    GamePanel gp;
    private static final String GAME_DIR = System.getProperty("user.home") + "/.rezero-game/";
    private final String configPath;

    public Config(GamePanel gp) {
        this.gp = gp;

        // ✅ Create game directory if it doesn't exist
        File gameDir = new File(GAME_DIR);
        if (!gameDir.exists()) {
            gameDir.mkdirs();
        }

        // ✅ Config file in user directory
        configPath = GAME_DIR + "config.txt";
    }

    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(configPath))) {
            // Save volume
            bw.write("musicVolume=" + gp.ui.musicVolume);
            bw.newLine();

            // Save SE enabled
            bw.write("seEnabled=" + gp.ui.seEnabled);
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving config: " + e.getMessage());
        }
    }

    public void loadConfig() {
        File configFile = new File(configPath);

        if (configFile.exists()) {
            // Load user's saved config
            loadFromFile(configFile);
        } else {
            // ✅ Try to load default config from JAR resources
            InputStream resourceStream = getClass().getResourceAsStream("/Config.txt");
            if (resourceStream != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceStream))) {
                    loadFromReader(br);
                    // Save defaults to user directory for next time
                    saveConfig();
                } catch (IOException e) {
                    System.err.println("Error loading default config: " + e.getMessage());
                    setDefaults();
                }
            } else {
                // No default config found, use hardcoded defaults
                setDefaults();
            }
        }
    }

    // Helper: Load from a File
    private void loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            loadFromReader(br);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading config file: " + e.getMessage());
            setDefaults();
        }
    }

    // Helper: Parse lines from a Reader
    private void loadFromReader(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("musicVolume=")) {
                float vol = Float.parseFloat(line.substring("musicVolume=".length()));
                gp.ui.musicVolume = vol;
                gp.music.setVolume(vol); // Apply to music
            }
            if (line.startsWith("seEnabled=")) {
                boolean se = Boolean.parseBoolean(line.substring("seEnabled=".length()));
                gp.ui.seEnabled = se;
            }
        }
    }

    // ✅ Set default values
    private void setDefaults() {
        gp.ui.musicVolume = 0.1f;
        gp.ui.seEnabled = true;
        gp.music.setVolume(gp.ui.musicVolume);
        // Save defaults to file
        saveConfig();
    }
}
