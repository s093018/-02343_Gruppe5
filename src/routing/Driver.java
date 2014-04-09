package routing;

import java.util.ArrayList;
import java.util.Arrays;

public class Driver {

	private static FindingSequence fs;
	
	public static void main(String[] args) {
			Controller ctrl = new Controller();
			fs = ctrl.getFs();
			ArrayList<String> stringArray = new ArrayList<String>(Arrays.asList("N","S","E","W","N","N","N","N","S","S","E","W","S","S","S"));
			ArrayList<DriverInstructions> result = new ArrayList<DriverInstructions>();
			result = fs.sequence(stringArray);
			for(DriverInstructions i : result) {
				System.out.println(i.getHeading()+": "+i.getLength());
			}
			
			fs.drive(result);
			
			
			System.out.println(fs.radianToDegree(1.66*Math.PI));
			
	}

	
}
