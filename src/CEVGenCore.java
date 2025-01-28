
import java.awt.Color;
import java.awt.Graphics;import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 import java.awt.image.BufferedImage;
import java.util.ArrayList; import javax.swing.JPanel;
import javax.swing.Timer;

public class CEVGenCore extends JPanel {
    
    //Image stuff
    private BufferedImage image;
    private final int width = 150, height = width, depth = width / 5;
    
    //Objects
    private CEVMath w; 
    private ArrayList<CEVInfo> pointsList, directionList;
    public  Music m = new Music();
    //private Exporter export = new Exporter();
    
    //Variables
    private int points, n;
    private double radius, pointSpeed, shiftSpeed, audioShiftIntensity, audioRadiusIntensity;
    private boolean symetrical, grayScale, hueShift, playMusic, animate;
    
    public CEVGenCore() { w = new CEVMath(); }                                                                           //Constructor.
    
    public Timer timer; private int frame = 0;                                                                           
    public void initTime(){                                                                                              //Timer, tries to run at 60fps
         timer = new Timer(1000 / 60, new ActionListener() {
            public void actionPerformed(ActionEvent e){
               moveNoise(); frame++;
            }
        });
    }
    
    public void initNoise(){ 
        //if (playMusic) { m.playAudio(2); }  
        pointsList = w.getRandomPoints(points, width, height, depth, (int)Math.ceil(radius));                            //Sets up global list & plays the audio.
    }
    
    public void initMove(){
        directionList = w.getRandomPoints(points, pointSpeed, pointSpeed, pointSpeed, 0);                                //Creates list of directions for each point(i).
    }
    
    private double radiusPrev = radius;
    public void printNoise(){ double[] distList; int color = 0;
        //Gets a % based off of the volume.
        double volume = playMusic && animate ? getVolume() : 1; 
        
        //Adjusts radius based off of volume %.
        double radiusTemp = (radius + (radius * audioRadiusIntensity * (volume - 1)));
        double radiusAdj  = (radiusTemp + radiusPrev) / 2.0; radiusPrev = radiusAdj;
        
        double shiftIntense = (audioShiftIntensity * 360); double angleA = (shiftIntense * volume) - shiftIntense;
        
        double angle = angleA + w.getOscilator((frame * shiftSpeed) / 1000.0) * 360;
        
        for (int x = 0; width > x; x++) { for (int y = 0; height > y; y++){ image.setRGB(x, y, Color.BLACK.getRGB()); }} //Fill screen with black.
        
        int widthAdj = !symetrical ? width : width / 2; int heightAdj = !symetrical ? height : height / 2;
        for (int x = 0; widthAdj > x; x++){ for (int y = 0; heightAdj > y; y++){                                         //Plot entire screen.
            
             distList = w.getClosestPointsDist(new CEVInfo(x, y, 0), pointsList);                                        //Gets list of distances from each point.
              
            if (grayScale){                                                                                              //Gets gray scale based off of distance of the closest point.
                double percent = distList[n] / radiusAdj; int gradi = (int)Math.round(255 - (255.0 * percent));
                if (radiusAdj >= distList[n]) { color = new Color(gradi, gradi, gradi).getRGB(); } 
            } else {                                                                                                     
                int r = (radiusAdj >= distList[n + 0]) ? (int)Math.round(255 - (distList[n + 0] / radiusAdj) * 255) : 0; //Gets   red based off of        closest point.
                int b = (radiusAdj >= distList[n + 1]) ? (int)Math.round(255 - (distList[n + 1] / radiusAdj) * 255) : 0; //Gets  blue based off of second closest point.
                int g = (radiusAdj >= distList[n + 2]) ? (int)Math.round(255 - (distList[n + 2] / radiusAdj) * 255) : 0; //Gets green based off of  third closest point.
                if (radiusAdj >= distList[n]) { color = hueShift ? w.getColor(r, g, b, angle) : new Color(r, g, b).getRGB(); }
            } 
            if (radiusAdj >= distList[n] && !symetrical) { image.setRGB(x, y, color); }                                    //Finally prints color gathered on (x, y).
            else if (radiusAdj >= distList[n]) {  
                image.setRGB(x, y, color);               image.setRGB(width - 1 - x, y, color);
                image.setRGB(x,  height - 1 - y, color); image.setRGB(width - 1 - x, height - 1 - y, color);
            }
        }} repaint();                                              
    } 
    
    public void moveNoise(){                                                                                             //Applies the move that was made in initMove(maxMove);
        CEVInfo point; CEVInfo direction;                                                                                //If point hits edge of display, bounce back.
        for (int i = 0; points > i; i++){
            point = pointsList.get(i); direction = directionList.get(i);
            
            point.x = point.x + direction.x; direction.x = (point.x > width  + radius || 0 - radius > point.x) ? -direction.x : direction.x;
            point.y = point.y + direction.y; direction.y = (point.y > height + radius || 0 - radius > point.y) ? -direction.y : direction.y;
            point.z = point.z + direction.z; direction.z = (point.z > depth  + radius || 0 - radius > point.z) ? -direction.z : direction.z;
            
        } printNoise();
    }
    
    public void changeRadius(double n) { radius += n; }
    
    private double getVolume(){ return m.getVolume(m.getFrame()); }
    
    //Setters
    public void setRadius    (double radius)     { this.radius = radius; }
    public void setPointSpeed(double pointSpeed) { this.pointSpeed = pointSpeed; }
    public void setShiftSpeed(double shiftSpeed) { this.shiftSpeed = shiftSpeed; }
    public void setAudioShiftIntensity(double audioShiftIntensity) { this.audioShiftIntensity = audioShiftIntensity; }
    public void setAudioRadiusIntensity(double audioRadiusIntensity) { this.audioRadiusIntensity = audioRadiusIntensity; }
    
    public void setN     (int n)      { this.n = n; }
    public void setPoints(int points) { this.points = points; }
     
    public void setGrayScale (boolean grayScale)  { this.grayScale  = grayScale;  }
    public void setHueShift  (boolean hueShift)   { this.hueShift   = hueShift;   }
    public void setSymetrical(boolean symetrical) { this.symetrical = symetrical; }
    public void setPlayMusic (boolean playMusic)  { this.playMusic  = playMusic;  }
    public void setAnimate   (boolean animate)    { this.animate    = animate;    }
    
    //Image stuff
    public void imageSizer() { image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); }
    @Override public void paintComponent(Graphics g) { super.paintComponent(g); g.drawImage(image, 0, 0, getWidth(), getHeight(), null); }
} 