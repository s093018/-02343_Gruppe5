package routing;

import imageProcessing.*;

public class Controller {

	private Camera camera;
	private FindingSequence fs,ph;
	
	public Controller () {
		camera = new TestCamera();
		fs = new FindingSequence(this);
	}

	public Camera getCamera() {
		return camera;
	}

	public FindingSequence getFs() {
		return fs;
	}
	
	
	
}
