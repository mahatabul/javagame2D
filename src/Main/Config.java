package Main;

import java.io.*;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("res/Config.txt"))) {
            // Save volume
            bw.write("musicVolume=" + gp.ui.musicVolume);
            bw.newLine();

            // Save SE enabled
            bw.write("seEnabled=" + gp.ui.seEnabled);
            bw.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() {
        // First, try local file (user overrides)
        File localFile = new File("res/Config.txt");
        if (localFile.exists()) {
            loadFromFile(localFile);
            return;
        }

        // Fallback to resources (default config)
        InputStream resourceStream = getClass().getResourceAsStream("res/Config.txt");
        if (resourceStream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceStream))) {
                loadFromReader(br);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // If no file at all, use defaults (musicVolume=0.5F, seEnabled=true)
    }

    // Helper: Load from a File
    private void loadFromFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            loadFromReader(br);
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
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
}