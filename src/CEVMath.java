
import java.awt.Color;
import java.util.ArrayList; import java.util.Arrays;

public class CEVMath {
    
    int points;
        
    public CEVMath() { ; }
    
    public ArrayList<CEVInfo> getRandomPoints(int points, double xMax, double yMax, double zMax, int extra){
        this.points = points;
        ArrayList<CEVInfo> list = new ArrayList<>();
        for (int i = 0; points > i; i++) { list.add(new CEVInfo(Math.random() * (xMax + extra * 2) - extra, Math.random() * (yMax + extra * 2) - extra, Math.random() * (zMax + extra * 2) - extra)); } 
        return list;
    }
    
    public double[] getClosestPointsDist(CEVInfo currentPoint, ArrayList<CEVInfo> pointsList){
        double[] distList = new double[points];
        for (int i = 0; points > i; i++){
            distList[i] = currentPoint.getDistance(pointsList.get(i));
        } Arrays.sort(distList); return distList;
    } 
    
    public double getOscilator(double n){ return (Math.cos(n) / -2) + 0.50; }   

    public int getColor(int r, int g, int b, double angle){
        double[] hsl = getHSL(r, g, b); hsl[0] = shiftHue(hsl[0], angle);
        int[] rgb = getRGB(hsl); 
        
        return new Color(Math.max(Math.min(rgb[0], 255), 0), Math.max(Math.min(rgb[1], 255), 0), Math.max(Math.min(rgb[2], 255), 0)).getRGB();
    }
    
    private double[] getHSL(int r, int g, int b){
        double[] hsl = new double[3];
        
        double rPrime = r / 255.0, gPrime = g / 255.0, bPrime = b / 255.0;
 
        double max = Math.max(rPrime, Math.max(gPrime, bPrime));
        double min = Math.min(rPrime, Math.min(gPrime, bPrime));
        double delta = max - min;
        
        double lum = (max + min) / 2.0;
        
        double hue = 0.0;
             if (max == rPrime) { hue = 60 * (((gPrime - bPrime) / delta) % 6); }
        else if (max == gPrime) { hue = 60 * (((bPrime - rPrime) / delta) + 2); }
        else if (max == bPrime) { hue = 60 * (((rPrime - gPrime) / delta) + 4); }
             
        double saturation = (delta == 0) ? 0 : (delta / (1 - Math.abs(2 * lum - 1)));

        hsl[0] = hue; hsl[1] = saturation; hsl[2] = lum;
        return hsl;
        }
    
    private double shiftHue(double h, double angle){
        h += angle; 
        while (0 > h) { h += 360; } while (h >= 360) { h += -360; }
        return h;
    }
    
    private int[] getRGB(double[] hsl){
        double h = hsl[0], s = hsl[1], l = hsl[2];
        
        double c = (1 - Math.abs((2 * l) - 1)) * s;
        double x = c * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = l - (c / 2.0);
        
        double rPercent = 0, gPercent = 0, bPercent = 0;
        
             if (  0 <= h && h <  60) { rPercent = c; gPercent = x; bPercent = 0; }
        else if ( 60 <= h && h < 120) { rPercent = x; gPercent = c; bPercent = 0; }
        else if (120 <= h && h < 180) { rPercent = 0; gPercent = c; bPercent = x; }
        else if (180 <= h && h < 240) { rPercent = 0; gPercent = x; bPercent = c; }
        else if (240 <= h && h < 300) { rPercent = x; gPercent = 0; bPercent = c; }
        else if (300 <= h && h < 360) { rPercent = c; gPercent = 0; bPercent = x; }
             
        int[] rgb = new int[3]; 
        rgb[0] = (int)Math.round((rPercent + m) * 255);
        rgb[1] = (int)Math.round((gPercent + m) * 255);
        rgb[2] = (int)Math.round((bPercent + m) * 255);
        return rgb;
         
    }
}
