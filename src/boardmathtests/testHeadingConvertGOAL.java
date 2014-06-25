package boardmathtests;

/**
 * @author Julian Villadsen - s123641
 *
 */
public class testHeadingConvertGOAL {

	public static void main(String[] args) {
		for(int degree = 0; degree < 400; degree+=5){
			System.out.println("Degree = " + degree + " => "
					+ "Direction = " + degreeToDirection(degree));
		}
		
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("");
		
		for(double i = -3; i < 3; i = i + 0.1){
			System.out.println("PI * " + i + " => "
					+ radianToDegreePositive(i*Math.PI) + " degrees");
		}
	}
	

	public static String degreeToDirection(int degree){
		String directions[] = {"N", "E", "S", "W"};
		return directions[(((degree + 45) % 360) / 90)];
	}	
	
	public static int radianToDegreePositive(double radian) {
		int result = (int) Math.toDegrees(radian);
		while (result < 0) 
			result = result + 360;
		return result;
	}

}
