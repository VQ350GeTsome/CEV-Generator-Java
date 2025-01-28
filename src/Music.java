
import java.io.File;
import javax.sound.sampled.*;
import javax.sound.sampled.Clip;

public class Music {
    
    private static String filePath = "audio\\audio ";
    private static Clip clip;
    public  static double vol;
    public  static int track;
    
    public void setTrack(int track) { this.track = track; }
    
    public static void playAudio(){
        try {
            File path = new File(filePath + track + ".wav");
            if (path.exists()){ 
                AudioInputStream input = AudioSystem.getAudioInputStream(path);
                
                clip = AudioSystem.getClip();
                clip.open(input); 
                clip.start();
                
            }
            else { System.out.println("Audio missing."); }
        } catch(Exception e) { System.out.println(e); }
    }
    
    public static long getFrame(){ return clip.getLongFramePosition(); }
    
    public static double getVolume(long frame){
        try {
            File path = new File(filePath + track + ".wav");
            if (path.exists()) {
                AudioInputStream input = AudioSystem.getAudioInputStream(path);
                AudioFormat format = input.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                int frameSize = format.getFrameSize();
                long framePosition = (long) frame * frameSize;
                input.skip(framePosition);

                byte[] buffer = new byte[frameSize];
                int bytesRead = input.read(buffer, 0, buffer.length);

                long sum = 0;
                for (int i = 0; i < bytesRead; i += 2) {
                    int sample = buffer[i + 1] << 8 | buffer[i] & 0xFF;
                    sum += sample * sample;
                }
                

                    double rms = bytesRead > 0 ? Math.sqrt(sum / (bytesRead / 2)) : 0.0;
                    double volume = rms > 0 ? 20 * Math.log10(rms / 32768.0) : 0.0;
                    
                    volume = (Double.isFinite(volume)) ? 0.50 + (100 + volume) / 100.0 : 0.0;

                    //System.out.println("Variables: Sum: " + sum + " rms: " + rms + " sample: " + sample);
                    //System.out.println("Current Volume at frame " + frame + ": " + volume);
                    
                    return volume;
                    
            } else {
                System.out.println("Audio file is missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
