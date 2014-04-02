package imageProcessing;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class RealCamera implements Camera
{
	private List<Goal> goals;
	private Map map;
	private List<Point> balls;
	private Robot robot;
	private VideoCapture capture;

	private boolean testMode = true;
	private Mat testImage;

	private Mat getImage()
	{
		if(testMode) return testImage;
		else
		{
			Mat frame = new Mat();
			capture.retrieve(frame);
			return frame;
		}
	}
	
	public RealCamera()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if(testMode) testImage = Highgui.imread("src/imgInOut/TESTDATA.JPG");
		else capture = new VideoCapture(0);


		getImage();
		//find forhindringer
		//Highgui.imwrite("src/imgInOut/RealCamTest.jpg", getImage());

		update();//Find bolde + robot
	}
	
	public void update()
	{
		getImage();
		//opdater balls, robot
	}

	//optimer de her senere hvis det bliver nødvendigt
	public void updateRobot(Point expectedPosition, double searchRadius){update();}
	public void updateBalls(){update();}
	public void shutDown(){if(!testMode) capture.release();}

	public Robot getRobot(){return robot;}
	public List<Point> getBalls(){return balls;}
	public List<Goal> getGoals(){return goals;}
	public Map getMap(){return map;}
}
