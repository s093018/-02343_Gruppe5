package routing;

import imageProcessing.*;

public class Controller {

	private Camera camera;
	private FindingSequence fs,ph;
	private robot.Control robot;
	
	public Controller () {
		camera = new TestCamera();
		fs = new FindingSequence(this);
		robot = new robot.Control();
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
