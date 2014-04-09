package routing;

public class TurnDegree {
	public int turnDegree(int algoHeading, int robotHeading) {
		int dif = algoHeading-robotHeading;
		if(-180<=dif && dif<=180)
			return dif;
		else if(dif<-180)
			return dif + 360;
		else if(dif>180)
			return dif - 360; // return antal grader roboten skal dreje (negativ venstre / positiv højre) MAX 180grader
		return dif;
	}

	public static void main(String[] args) {
		TurnDegree td = new TurnDegree();
		System.out.println(td.turnDegree(225, 45));
		System.out.println(td.turnDegree(270, 0));
		System.out.println(td.turnDegree(225, 225));
		System.out.println(td.turnDegree(45, 225));
		System.out.println(td.turnDegree(90, 225));
		System.out.println(td.turnDegree(0, 225));
	}

}

