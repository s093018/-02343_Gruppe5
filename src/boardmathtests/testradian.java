package boardmathtests;
/**
 * @author Julian Villadsen - s123641
 *
 */
public class testradian {

	public static void main(String[] args) {
		int algoHeading = 360-3, robotHeading = 3;
		//System.out.println(differenceAngle(algoHeading, robotHeading));
		
	//	System.out.println(mirrorTurnDif(turnDegree(algoHeading, robotHeading)));
		
		
		System.out.println(mirrorTurnDif(turnDegree (0, 270))); // -90
		System.out.println(mirrorTurnDif(turnDegree (270, 0))); // 90
		System.out.println(mirrorTurnDif(turnDegree (0, 90))); // 90
		System.out.println(mirrorTurnDif(turnDegree (90, 0))); // -90
		System.out.println(mirrorTurnDif(turnDegree (135, 270))); // 135
		System.out.println(mirrorTurnDif(turnDegree (270, 135))); // -135

		System.out.println(mirrorTurnDif(turnDegree (3, 359))); // -4
		System.out.println(mirrorTurnDif(turnDegree (359, 3))); // 4
		
		System.out.println(mirrorTurnDif(turnDegree (177, 182))); // -5
		System.out.println(mirrorTurnDif(turnDegree (182, 177))); // 5

		System.out.println(mirrorTurnDif(turnDegree (87, 95))); // 8
		System.out.println(mirrorTurnDif(turnDegree (95, 87))); // -8
		
		System.out.println(mirrorTurnDif(turnDegree (266, 273))); // 7
		System.out.println(mirrorTurnDif(turnDegree (273, 266))); // -7

	}
	public static int differenceAngle(int theta1, int theta2)
	{
		int dif = ((theta2 - theta1)%(360)); // in range 
		if (theta1>theta2) {
			dif += 360;
		}
		if (dif >= 180) {
			dif = -(dif - 360);
		}
		return dif;
	}

	public static int turnDegree(int algoHeading, int robotHeading) {
		int dif = robotHeading-algoHeading;
		if(-180 <= dif && dif <= 180)
			return dif;
		else if(dif < 180)
			return dif + 360;
		else if(dif > 180)
			return dif - 360; // return antal grader roboten skal dreje (negativ venstre / positiv hoejre) MAX 180grader
		return dif;
	}
	
	public static int mirrorTurnDif(int dif){
		return dif;
	}
}
