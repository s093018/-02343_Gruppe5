package routing;

import robot.Control;
import imageProcessing.*;


public class Controller {

	private Camera camera;
	private FindingSequence fs;
	private Control robot;
	
	public Controller () {
		camera = new TestCamera();
		try {
			robot = new robot.Control();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fs = new FindingSequence(this, robot);
	}

	public Camera getCamera() {
		return camera;
	}

	public FindingSequence getFs() {
		return fs;
	}
	
	public robot.Control getrobot() {
		return robot;
	}
	
	
}
