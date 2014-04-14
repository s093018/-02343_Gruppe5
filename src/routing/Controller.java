package routing;

import imageProcessing.*;

public class Controller {

	private Camera camera;
	private FindingSequence fs;
	private robot.Control robot;
	
	public Controller () {
		camera = new TestCamera();
		fs = new FindingSequence(this);
		try {
			robot = new robot.Control();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
