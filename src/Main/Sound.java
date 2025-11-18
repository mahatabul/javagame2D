package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];
    FloatControl volumeControl;

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/bgm.wav");
        soundURL[1] = getClass().getResource("/sound/select.wav");
        soundURL[2] = getClass().getResource("/sound/swordCut.wav");
        soundURL[3] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[4] = getClass().getResource("/sound/woosh.wav");
        soundURL[5] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[6] = getClass().getResource("/sound/gameover.wav");
        soundURL[7] = getClass().getResource("/sound/rezero-respawn-sound-effect(2).wav");
        soundURL[8] = getClass().getResource("/sound/levelUp.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            // Get volume control
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    //  Set volume (0.0 = mute, 1.0 = full volume)
    public void setVolume(float value) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum(); // usually -80.0f
            float max = volumeControl.getMaximum(); // usually 6.0f
            float dB = (float) (Math.log10(value <= 0.0 ? 0.0001 : value) * 20.0);
            if (dB < min) dB = min;
            if (dB > max) dB = max;
            volumeControl.setValue(dB);
        }
    }
}
