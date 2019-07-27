package general_logic;

public class CollisionDetector 
{
	static public class Rect
	{
		public final double x1, y1, x2, y2;
		
		public Rect(double ax, double ay, double w, double h)
		{
			x1 = ax; 
			y1 = ay;
			x2 = x1 + w;
			y2 = y1 + h;			
		}
		
		public boolean pointInside(double x, double y) {
			return (x >= x1 && x <= x2) && (y >= y1 && y <= y2);			
		}		
		
		public boolean intersect(Rect rt2)
		{			
			if(pointInside(rt2.x1, rt2.y1) || pointInside(rt2.x2, rt2.y1) || 
				pointInside(rt2.x1, rt2.y2) || pointInside(rt2.x2, rt2.y2)) 
			{
				return true;
			}
			
			return false;			
		}		
	}
	
	// Warning: Check both rectangles if they intersect each other, not just one of them.
	// The occasional ignore pattern has been finally fixed. (While debugging Spaceship-Moon collisions)
	public static boolean check (
			Rect rt1, Rect rt2
	)
	{		
		if(rt1.intersect(rt2) || rt2.intersect(rt1)) { return true; }
		
		/*
			// Debugging data
			Spaceship: 
			x1 = 760 - y1 = 416 - x2 = 796 - y2 = 455
			Moon 3: 
			x1 = 757 - y1 = 412 - x2 = 816 - y2 = 469
		*/
				
		// No collisions
		return false;		
	}
}
