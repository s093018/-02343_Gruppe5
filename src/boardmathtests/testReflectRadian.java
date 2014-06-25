package boardmathtests;

/**
 * @author Julian Villadsen - s123641
 *
 */
public class testReflectRadian {
	
	public static void main(String[] args) {
		for(double i = -3; i < 3; i = i + 0.1){
			System.out.println("PI * " + i + " => PI * "
					+ reflectRadian(i*Math.PI)/Math.PI);
		}
	}

	public static double reflectRadian(double startRadian){
		double result = 0.5*Math.PI - (startRadian + Math.PI - 0.5*Math.PI);
		result = 2*Math.PI - startRadian; 
		result = -startRadian; //subtract 2*Pi
		return result;
	}
}
