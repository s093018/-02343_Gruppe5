package routing;

import java.util.ArrayList;
import java.util.Arrays;

public class Driver {

	private static FindingSequence fs;
	
	public static void main(String[] args) {
			Controller ctrl = new Controller();
			fs = ctrl.getFs();
			ArrayList<Integer> intArray = new ArrayList<Integer>(Arrays.asList(0,180,90,270,0,0,0,0,180,180,90,270,180,180,180));
			ArrayList<DriverInstructions> result = new ArrayList<DriverInstructions>();
			result = fs.sequence(intArray);
			for(DriverInstructions i : result) {
				System.out.println(i.getHeading()+": "+i.getLength());
			}
			
			fs.drive(result);
			
	}

	
}
