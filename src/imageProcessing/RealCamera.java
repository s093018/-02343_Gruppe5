package imageProcessing;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class RealCamera implements Camera {
	private List<Goal> goals;
	private Map map;
	private List<Point> balls;
	private Robot robot;
	private VideoCapture capture;

	private boolean testMode = true;
	private Mat testImage;

	private Mat getImage(){
		if(testMode) { 
			return testImage;
		} else {
			Mat frame = new Mat();
			capture.retrieve(frame);
			return frame;
		}
	}

	public RealCamera() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if(testMode) {
			testImage = Highgui.imread("src/imgInOut/TESTDATA.JPG");
		} else {
			capture = new VideoCapture(0);
		}

		getImage();
		//find forhindringer
		//Highgui.imwrite("src/imgInOut/RealCamTest.jpg", getImage());

		update();//Find bolde + robot
	}

	public void update() {
		Mat image = getImage();
		Mat templ = Highgui.imread("../imgInOut/Template.jpg");

		int result_cols = image.cols() - templ.cols() + 1;
		int result_rows = image.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		while(true) {
			int matchingMethod = 1;
			Imgproc.matchTemplate(image, templ, result, matchingMethod);

			MinMaxLocResult mmlr = Core.minMaxLoc(result);

			org.opencv.core.Point matchLoc;
			if(matchingMethod  == Imgproc.TM_SQDIFF || matchingMethod == Imgproc.TM_SQDIFF_NORMED ) {
				matchLoc = mmlr.minLoc; 
			} else { 
				matchLoc = mmlr.maxLoc; 
			}

			double thresholdMatch = 0.25;
			if(mmlr.minVal < thresholdMatch) {
				Core.circle(image, new org.opencv.core.Point(matchLoc.x + (templ.cols()/2),
						matchLoc.y + (templ.rows()/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
			} else {
				break;
			}
		}
		Highgui.imwrite("../imgInOut/Result.jpg", image);
	}

	//Optimize these later if necessary.
	public void updateRobot(Point expectedPosition, double searchRadius) {
		update();
	}

	public void updateBalls() {
		update();
	}
	public void shutDown() {
		if(!testMode) capture.release();
	}

	public Robot getRobot() {
		return robot;
	}

	public List<Point> getBalls() { 
		return balls;
	}

	public List<Goal> getGoals()
	{
		return goals;
	}

	public Map getMap() {
		return map;
	}
}
