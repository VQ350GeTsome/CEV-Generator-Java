

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Exporter {
    private static String directory = "";
    private static String finalDirectory = "";
    
    private static int frame = 0;
    
    public Exporter(){
        getCurrentDirectory();
    }
    
    public void getCurrentDirectory(){
        directory = System.getProperty("user.dir");
        int i = directory.length();
        String slashCheck = "";
        int slashIndex = 0;
        while (i > 0){
            slashCheck = directory.substring(i - 1, i);
            if (slashCheck.equals("\\")){
               slashIndex = i;
               i = 0;
               finalDirectory = directory.substring(0, slashIndex);
               //System.out.println("Saved Image At: " + finalDirectory);
            }
            i--;
        } 
    }
    
    public void exportCurrentImage(BufferedImage image){
        try{
            BufferedImage bi = image;
            File outputfile = new File("currentFrame.png");
            ImageIO.write(bi, "png", outputfile);
            outputfile.renameTo(new File(directory + "\\" + "images" + "\\" +frame + ".png"));
            frame++;
            }
        catch (IOException e) {
            System.out.println(directory + "is broken / not a valid path.");
        }
    }
}
