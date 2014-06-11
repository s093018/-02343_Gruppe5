package imageProcessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class RealCamera implements Camera
{
	private List<Goal> goals;
	private Map map;
	private List<Point> balls;
	private Robot robot;
	public Point frontPoint, backPoint;

	private VideoCapture capture;
	private Configuration settings = new Configuration("src/imgInOut/settings.cfg");
	private Mat testImage;

	private double pixelSize = 0.35;//cm/pixel
	private double floorColor[];

	private void showStep(String filename, Mat image, double scaling)
	{
		if(settings.showSteps)
		{
			Core.flip(image, image, 0);
			Proc.saveImage(filename, image, scaling);
			Core.flip(image, image, 0);
		}
	}
	private Mat getImage()
	{
		if(settings.testMode) return testImage;
		else
		{
			Mat frame = new Mat();
			capture.retrieve(frame);

			//Ensure image and loaded templates have the same type (convertTo() doesn't work).
			Highgui.imwrite("frame.png", frame);
			frame = Highgui.imread("frame.png");
			Core.flip(frame, frame, 0);
			return frame;
		}
	}
	private double[] estimateFloorColor(Mat image)
	{
		Mat rect = new Mat(image.size(), image.type(), new Scalar(0, 0, 0, 0));
		Core.rectangle(rect, settings.NW, settings.SE, new Scalar(1, 1, 1, 1));
		double floorRectSize = Core.sumElems(rect).val[0];
		double sumColor[] = Core.sumElems(image.mul(rect)).val;
		double averageColor[] = {sumColor[0] / floorRectSize, sumColor[1] / floorRectSize, sumColor[2] / floorRectSize};
		return averageColor;
	}
	private Mat colorDistance(List<Mat> layers)
	{
		List<Mat> newLayers = new ArrayList<Mat>();
		for(int i = 0; i < 3; ++i)
		{
			newLayers.add(new Mat());
			org.opencv.core.Core.multiply(layers.get(i), layers.get(i), newLayers.get(i));
		}
		Mat temp = new Mat();
		Mat sum = new Mat();
		org.opencv.core.Core.add(newLayers.get(0), newLayers.get(1), temp);
		org.opencv.core.Core.add(newLayers.get(2), temp, sum);
		Mat result = new Mat();
		org.opencv.core.Core.sqrt(sum, result);
		return result;
	}
	private Mat flatDivisor(List <Mat> layers)
	{
		Mat temp = new Mat();
		Mat sum = new Mat();
		Core.add(layers.get(0), layers.get(1), temp);
		Core.add(layers.get(2), temp, sum);
		return sum;
	}
	private Mat brightness(List <Mat> layers)
	{
		Mat result = new Mat();
		Mat temp = new Mat();
		Core.merge(layers, temp);
		Imgproc.cvtColor(temp, result, Imgproc.COLOR_RGB2GRAY);
		return result;
	}
	Mat merge(Mat b, Mat g, Mat r)
	{
		List<Mat> newLayers = new ArrayList<Mat>();
		newLayers.add(b);
		newLayers.add(g);
		newLayers.add(r);
		Mat result = new Mat();
		Core.merge(newLayers, result);
		return result;
	}
	List<Mat> split(Mat image)
	{
		List<Mat> layers = new ArrayList<Mat>();
		layers.add(new Mat());
		layers.add(new Mat());
		layers.add(new Mat());
		Core.split(image, layers);
		return layers;
	}
	Mat select(Mat image, Mat mask)
	{
		List<Mat> layers = split(image);
		for(Mat m : layers)
			m.mul(mask);
		Mat result = new Mat();
		Core.merge(layers, result);
		return result;
	}
	private Mat smooth(Mat image)
	{
		Mat result = new Mat();
//		Imgproc.GaussianBlur(image, result, new Size(5, 5), 1);
//		Imgproc.medianBlur(image, result, 5);
		Imgproc.bilateralFilter(image, result, -1, 180, 4);
		return result;
	}
	private Mat floodFill(Mat image, double lodiff, double updiff)
	{
		//Remove central obstacle
		Mat cleared = new Mat();
		image.copyTo(cleared);
		Core.rectangle(cleared, settings.NW, settings.SE, new Scalar(floorColor[0], floorColor[1], floorColor[2], 255), Core.FILLED);

		Mat mask = new Mat(new Size(image.width()+2, image.height()+2), CvType.CV_8U);
		mask.setTo(new Scalar(0, 0, 0, 255));
		Scalar min = new Scalar(floorColor[0] * lodiff, floorColor[1] * lodiff, floorColor[2] * lodiff, 255);
		Scalar max = new Scalar(floorColor[0] * updiff, floorColor[1] * updiff, floorColor[2] * updiff, 255);
		Imgproc.floodFill(cleared, mask, settings.floodFillOrigin, new Scalar(255, 255, 255, 255), null, min, max, Imgproc.FLOODFILL_FIXED_RANGE | Imgproc.FLOODFILL_MASK_ONLY);
		return mask.submat(1, mask.height()-1, 1, mask.width()-1);
	}

	private Mat floodFillDetection(Mat image)
	{
		Mat filled = floodFill(smooth(image), settings.floodFillLodiff, settings.floodFillUpdiff);
		//TODO: Get rid of holes/goals
		//erode bumps, then dilate enough to remove robot, then erode again
		//Also, locate goals
		return filled;
	}
	private Mat prototypeBoundsDetection(Mat image)
	{
		Mat template = Highgui.imread(settings.obstaclePrototype);
		Mat match = new Mat();
		Imgproc.matchTemplate(image, template, match, Imgproc.TM_CCORR);
		Mat thresh = new Mat();
		Imgproc.threshold(match, thresh, settings.woodTreshold, 1, Imgproc.THRESH_BINARY);
		Mat result = Mat.ones(image.size(), image.type());
		result.submat(template.height()/2, template.height()/2 + thresh.height(), template.width()/2, template.width()/2 + thresh.width()).setTo(thresh);
		//TODO: Floodfill to select outermost structure to avoid robot being seen as obstacle
		return result;
	}
	private org.opencv.core.Point findCorner(Mat image, int quadrant)
	{
		//Build prototype
		double[] blue = {255.0, 0.0, 0.0};
		Mat prototype = Proc.replaceColor(Highgui.imread(settings.cornerPrototypes[quadrant-1]), blue, floorColor);
		showStep("adjustedprototype.png", prototype, 1.0);

		//Select quadrant
		int width = image.width()/2;
		int height = image.height()/2;
		int xOff = (quadrant == 1 || quadrant == 4) ? width : 0;
		int yOff = (quadrant > 2) ? height : 0;
		Mat Q = image.submat(yOff, yOff + height, xOff, xOff + width);

		//Find best match
		Mat match = new Mat();
		Imgproc.matchTemplate(Q, prototype, match, Imgproc.TM_SQDIFF_NORMED);
		showStep("cornerintensity" + quadrant + ".png", match, 255.0);
		org.opencv.core.Point origin = Core.minMaxLoc(match).minLoc;
		return new org.opencv.core.Point(xOff + origin.x + prototype.width()/2, yOff + origin.y + prototype.height()/2);
	}
	private Goal getGoal(org.opencv.core.Point left, org.opencv.core.Point right)
	{
		int xAvg = (int)(left.x+right.x);
		int yAvg = (int)(left.y+right.y);
		return new Goal(new Point(xAvg/2, yAvg/2, pixelSize), 10, Math.atan2(-(left.x-right.x), (left.y-right.y)));
	}
	private Mat cornerBasedDetection(Mat image)
	{
		Mat course = Mat.zeros(image.size(), CvType.CV_8U);
		Mat blurred = smooth(image);
		//New central obstacle is same color as bounds, so black it out while looking for corners
		Core.circle(blurred, settings.center, 100, new Scalar(0, 0, 0), -1);
		Mat marked = image.clone();

		List<org.opencv.core.Point> points = new ArrayList<org.opencv.core.Point>();
		for(int i = 1; i < 5; ++i)
		{
			points.add(findCorner(blurred, i));
			Core.circle(marked, points.get(i-1), 6, new Scalar(0, 0, 255), -1);
		}

		//TODO: Recover from malplaced corner by mirroring
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		contours.add(new MatOfPoint(points.get(0), points.get(1), points.get(2), points.get(3)));
		Imgproc.drawContours(course, contours, -1, new Scalar(1, 1, 1, 255), Core.FILLED);

		//TODO: Estimate pixel size

		goals = new ArrayList<Goal>();
		goals.add(getGoal(points.get(1), points.get(2)));
		goals.add(getGoal(points.get(3), points.get(0)));

		for(Goal g : goals)
			Core.circle(marked, new org.opencv.core.Point(g.center.pixel_x, g.center.pixel_y), 4, new Scalar(0, 255, 255), -1);
		showStep("corners.png", marked, 1.0);

		return course;
	}
	private Mat detectBounds(Mat image, int strategy)
	{
		switch(strategy)
		{
		case 0: return floodFillDetection(image);
		case 1: return cornerBasedDetection(image);
		case 2: return prototypeBoundsDetection(image);
		default:
			System.out.println("detectBounds(): Unknown strategy (" + strategy + "). Using floodFillDetection");
			return floodFillDetection(image);
		}
	}
	private Mat findCentralObstacle(Mat image)
	{
		return null;
	}
	private Mat detectCentralObstacle(Mat image)
	{
		int size = settings.centralObstacleSize;
		Scalar tolerance = settings.centralObstacleTolerance;
		//Look for reddest point in central area
		Mat detector = new Mat(new Size(size, size), image.type());
		detector.setTo(settings.centralObstacleColor);
		Mat centralRect = new Mat(image.size(), image.type(), new Scalar(0, 0, 0, 0));
		Core.rectangle(centralRect, settings.NW, settings.SE, new Scalar(1, 1, 1, 1), Core.FILLED);
		Mat intensity = new Mat();
		Imgproc.matchTemplate(image.mul(centralRect), detector, intensity, Imgproc.TM_CCORR);
		MinMaxLocResult extrema = Core.minMaxLoc(intensity);
		org.opencv.core.Point maximum = new org.opencv.core.Point(extrema.maxLoc.x + size / 2, extrema.maxLoc.y + size / 2);

		showStep("centralObstacleIntensity.png", intensity, 1.0 / (size*size*255*3));

		//Floodfill using original image, since intensity map has softened corners 
		Mat obstacleMask = new Mat(new Size(image.width() + 2, image.height() + 2), CvType.CV_8U);
		obstacleMask.setTo(new Scalar(0, 0, 0, 255));
		Imgproc.floodFill(image, obstacleMask, maximum, settings.centralObstacleColor, null, tolerance, tolerance, Imgproc.FLOODFILL_FIXED_RANGE | Imgproc.FLOODFILL_MASK_ONLY);

		//TODO: Maybe dilate mask as well?

		Mat result = new Mat();
		Core.subtract(Mat.ones(obstacleMask.size(), CvType.CV_8U), obstacleMask, result);
		Mat centralMask = result.submat(1, result.height()-1, 1, result.width()-1);
		showStep("centralObstacleMask.png", centralMask, 255);
		return centralMask;
	}
	public RealCamera()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if(settings.testMode)
		{
			testImage = Highgui.imread(settings.testImageFile);
			Core.flip(testImage,  testImage, 0);
		}
		else capture = new VideoCapture(0);

		Mat image = getImage();
		showStep("input.png", image, 1.0);

		floorColor = estimateFloorColor(image);

		//Find obstacles
		Mat bounds = detectBounds(image, settings.boundsStrategy);

		Mat blocked = bounds.mul(detectCentralObstacle(image), 255);
		blocked = bounds;

		char obstacle[][] = new char[blocked.width()][blocked.height()];
		for(int y = 0; y < blocked.height(); ++y)
			for(int x = 0; x < blocked.width(); ++x)
				obstacle[x][y] = blocked.get(y, x)[0] == 0.0 ? 'O' : ' ';
		map = new Map(obstacle, pixelSize);

		showStep("obstacleMask.png", blocked, 255);

		//Find initial position of balls + robot
		update();
	}
	public void update()
	{
		this.balls = new ArrayList<Point>();

		//Ensure image and template have the same type (converTo() doesn't work).
		Mat image = getImage();

		findBalls(image.clone(), "src/imgInOut/Template.png");
		findRobot(image, "src/imgInOut/greenfront.png", "src/imgInOut/Back.png");
	}

	public void findBalls(Mat image, String templFileName)
	{
		Mat templ = Highgui.imread(templFileName);

		int result_cols = image.cols() - templ.cols() + 1;
		int result_rows = image.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32F);

		int matchingMethod = 1;
		boolean derp = true;
		while(true) {
			Imgproc.matchTemplate(image, templ, result, matchingMethod);
			if(derp)
			{
				derp = false;
				showStep("ballintensity.png", result, 255.0/(templ.width()*templ.height()));
			}

			MinMaxLocResult mmlr = Core.minMaxLoc(result);

			org.opencv.core.Point matchLoc;
			if(matchingMethod  == Imgproc.TM_SQDIFF || matchingMethod == Imgproc.TM_SQDIFF_NORMED ) {
				matchLoc = mmlr.minLoc; 
			} else { 
				matchLoc = mmlr.maxLoc; 
			}

			double thresholdMatch = 0.25;
			System.out.println("TEST");
			if(mmlr.minVal < thresholdMatch) {
				Core.circle(image, new org.opencv.core.Point(matchLoc.x + (templ.cols()/2),
						matchLoc.y + (templ.rows()/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
				this.balls.add(new Point((int)(matchLoc.x + (templ.cols()/2)), (int)(matchLoc.y + (templ.rows()/2)), pixelSize));
			} else {

				break;
			}
		}
		showStep("billede3.png", image, 1);
	}

	public void findRobot(Mat image, String front, String back)
	{
		Mat image1 = image.clone();
		Mat image2 = image.clone();
		double x1 = 0, y1 = 0, x2 = 0, y2 = 0;

		ArrayList<String> templates = new ArrayList<String>();
		templates.add(back);
		templates.add(front);

		for(int i = 0; i<templates.size(); i++){

			Mat templ = Highgui.imread(templates.get(i));

			int result_cols = image.cols() - templ.cols() + 1;
			int result_rows = image.rows() - templ.rows() + 1;
			Mat result = new Mat(result_rows, result_cols, CvType.CV_32F);

			int matchingMethod = 0;

			Imgproc.matchTemplate(image, templ, result, matchingMethod);

			MinMaxLocResult mmlr = Core.minMaxLoc(result);

			org.opencv.core.Point matchLoc = mmlr.minLoc;

			System.out.println(i);
			if(i == 0){
				Core.circle(image1, new org.opencv.core.Point(matchLoc.x + (templ.cols()/2),
						matchLoc.y + (templ.rows()/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
				showStep("robotint0.png", result, 0.005 / (templ.width()*templ.height()));
				showStep("billede1.png", image1, 1);
				x1 = matchLoc.x + (templ.cols()/2);
				y1 = matchLoc.y + (templ.rows()/2);
			}
			else if(i == 1){
				Core.circle(image2, new org.opencv.core.Point(matchLoc.x + (templ.cols()/2),
						matchLoc.y + (templ.rows()/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
				showStep("robotint1.png", result, 0.005 / (templ.width()*templ.height()));
				showStep("billede2.png", image2, 1);
				x2 = matchLoc.x + (templ.cols()/2);
				y2 = matchLoc.y + (templ.rows()/2);
			}
		}
		//x1,y1 back
		//x2, y2 front
		frontPoint = new Point((int)x2,(int)y2, pixelSize);
		backPoint = new Point((int)x1,(int)y1, pixelSize);
		
		System.out.println("x1: "+x1+", x2: "+x2+", y1: "+y1+", y2: "+y2);
		System.out.println("gradtal: "+Math.atan2(y2-y1, x2-x1));
		//Spørg Rasmus om dette er korrekt Math.atan2(y1-y2, x2-x1)
/*		Mat rpos = image.clone();
		Core.circle(rpos, new org.opencv.core.Point((int)((x1+x2)/2), (int)((y1+y2)/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
		showStep("rpos.png", rpos, 1);
*/		robot = new Robot(new Point((int)((x1+x2)/2), (int)((y1+y2)/2), pixelSize), Math.atan2(y2-y1, x2-x1), 24/pixelSize, 38/pixelSize);
	}

	//optimer de her senere hvis det bliver nødvendigt
	public void updateRobot(Point expectedPosition, double searchRadius){update();}
	public void updateBalls(){update();}
	public void shutDown(){if(!settings.testMode) capture.release();}

	public Robot getRobot(){return robot;}
	public List<Point> getBalls(){return balls;}
	public List<Goal> getGoals(){return goals;}
	public Map getMap(){return map;}
}
