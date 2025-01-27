public class CEVInfo {
    
    public double x; public double y; public double z; 
    
    public CEVInfo(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }

    public double getDistance(CEVInfo other) { 
        return Math.sqrt(Math.pow((other.x - this.x), 2) + Math.pow((other.y - this.y), 2) + Math.pow((other.z - this.z), 2)); 
    }
    
    @Override public String toString() { return "(" + this.x + ", " + this.y + ", " + this.z + ")"; }
}
