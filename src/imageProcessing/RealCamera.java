package imageProcessing;

import java.util.ArrayList;
import java.util.Collections;
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
	private Point obstacleCenter;

	private VideoCapture capture;
	private Configuration settings = new Configuration("settings.cfg");
	private Mat testImage;

	private double pixelSize;//cm/pixel

	private long startTime = System.currentTimeMillis();
	private int updates = 0;

	private void showStep(String filename, Mat image, double scaling)
	{
		if(settings.showSteps)
		{
			Core.flip(image, image, 0);
			Proc.saveImage("src/imgDump/" + filename, image, scaling);
			Core.flip(image, image, 0);
		}
	}
	private void sleep(long millis)
	{
		long current, start;
		while(millis > 0)
		{
			start = System.currentTimeMillis();
			try{Thread.sleep(millis);}
			catch(InterruptedException e){}
			current = System.currentTimeMillis();
			millis -= current - start;
		}
	}
	private Mat getImage()
	{
		Mat frame;
		if(settings.testMode) frame = testImage.clone();
		else
		{
			frame = new Mat();
			if(!capture.grab())
				System.out.println("WARNING: IMAGE GRAB FAILED!");
			if(!(capture.retrieve(frame) && capture.retrieve(frame)))//Double retrieve to ensure fresh data
				System.out.println("WARNING: IMAGE RETRIEVE FAILED!");
		}
		//Ensure image and loaded templates have the same type (convertTo() doesn't work).
		Highgui.imwrite("src/imgDump/input.png", frame);
		frame = Highgui.imread("src/imgDump/input.png");
		Core.flip(frame, frame, 0);
		return frame;
	}
	private Mat smooth(Mat image)
	{
		Mat result = new Mat();//
//		Imgproc.GaussianBlur(image, result, new Size(5, 5), 1);
//		Imgproc.medianBlur(image, result, 5);
		Imgproc.bilateralFilter(image, result, -1, 180, 4);
		return result;
	}
/*	private Mat floodFill(Mat image, double lodiff, double updiff)
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
	}*/
	private org.opencv.core.Point findCorner(Mat image, int quadrant, double[] floorColor)
	{
		//Build prototype
		double[] blue = {255.0, 0.0, 0.0};
		Mat prototype = Proc.replaceColor(Highgui.imread(settings.cornerPrototypes[quadrant-1]), blue, floorColor);
		showStep("adjustedprototype" + quadrant + ".png", prototype, 1.0);

		//Select quadrant
		int width = image.width()/2;
		int height = image.height()/2;
		int xOff = (quadrant == 1 || quadrant == 4) ? width : 0;
		int yOff = (quadrant > 2) ? 0 : height;
		Mat Q = image.submat(yOff, yOff + height, xOff, xOff + width);

		//Find best match
		Mat match = new Mat();
		Imgproc.matchTemplate(Q, prototype, match, Imgproc.TM_SQDIFF_NORMED);
		showStep("cornerintensity" + quadrant + ".png", match, 255.0);
		org.opencv.core.Point origin = Core.minMaxLoc(match).minLoc;
		return new org.opencv.core.Point(xOff + origin.x + prototype.width()/2, yOff + origin.y + prototype.height()/2);
	}
	private double ratio(double a, double b)
	{
		if(a > b) return (b/a)*settings.rectangleRatio;
		else return (a/b)*settings.rectangleRatio;
	}
	private double dev(org.opencv.core.Point a, org.opencv.core.Point b, org.opencv.core.Point c)
	{
		double q = Proc.distance(a, b);
		double w = Proc.distance(b, c);
		double dot = (a.x - b.x)*(b.x - c.x) + (a.y - b.y)*(b.y - c.y);
		return Math.abs(ratio(q, w)-1) + 10*Math.abs(dot)/(q*w);
	}
	private void antiSymmetrify(List<org.opencv.core.Point> points)
	{
		double[] deviation = new double[4];
		double max = 0.0;
		double min = 10000;
		double d;
		for(int i = 0; i < 4; ++i)
		{
			d = dev(points.get((i + 3) % 4), points.get(i), points.get((i + 1) % 4));
			if(d > max) max = d;
			if(d < min) min = d;
			deviation[i] = d;
		}
		System.out.println("Deviation from rectangle:");
		System.out.println("" + deviation[1] + "\t" + deviation[0]);
		System.out.println("" + deviation[2] + "\t" + deviation[3]);
		System.out.println("Max deviation: " + max + " (limit: " + settings.deviationTolerance + ")");

		if(max > settings.deviationTolerance)
		{
			for(int i = 0; i < 4; ++i)
			{
				//Alternative: Add deviations from neighboring corners, the wrong one will have bad neighbors.
				if(deviation[i] == min)
				{
					org.opencv.core.Point p = Proc.add(points.get(i), Proc.add(
							Proc.sub(points.get((i+3) % 4), points.get(i)),
							Proc.sub(points.get((i+1) % 4), points.get(i))));
					System.out.println("Correcting corner " + ((i+2) % 4 + 1) + " to (" + p.x + ", " + p.y + ").");
					points.set((i + 2) % 4, p);
					return;
				}
				/*
				if(deviation[i] == max)
				{
					org.opencv.core.Point p = Proc.add(points.get((i+2) % 4), Proc.add(
							Proc.sub(points.get((i+1) % 4), points.get((i+2) % 4)),
							Proc.sub(points.get((i+3) % 4), points.get((i+2) % 4))));
					System.out.println("Correcting corner " + (i+1) + " to (" + p.x + ", " + p.y + ").");
					points.set(i, p);
					return;
				}*/
			}
		}
	}
	private List<org.opencv.core.Point> adjustedPrototypeSearch(Mat image)
	{
		Mat processed = Proc.norm(smooth(image));
		showStep("processed.png", processed, 1);

		//New central obstacle is same color as bounds, so black it out while looking for corners
		Core.circle(processed, settings.center, 100, new Scalar(0, 0, 0), -1);

		//Find corners
		double[] floorColor = Proc.estimateFloorColor(processed, settings.NW, settings.SE);
		List<org.opencv.core.Point> points = new ArrayList<org.opencv.core.Point>();
		for(int i = 1; i < 5; ++i)
			points.add(findCorner(processed, i, floorColor));

		return points;
	}
	private Mat detectCentralObstacle(Mat image)
	{
		Mat norm = Proc.norm(image);
		int size = settings.centralObstacleSize;
		Scalar tolerance = settings.centralObstacleTolerance;
		//Look for whitest (or whatever) point in central area
		Mat detector = new Mat(new Size(size, size), norm.type());
		detector.setTo(settings.centralObstacleColor);
		Mat centralRect = new Mat(norm.size(), norm.type(), new Scalar(0, 0, 0, 0));
		Core.rectangle(centralRect, settings.NW, settings.SE, new Scalar(1, 1, 1, 1), Core.FILLED);
		Mat intensity = new Mat();
		Imgproc.matchTemplate(norm.mul(centralRect), detector, intensity, Imgproc.TM_SQDIFF);
		MinMaxLocResult extrema = Core.minMaxLoc(intensity);
		org.opencv.core.Point maximum = new org.opencv.core.Point(extrema.minLoc.x + size / 2, extrema.minLoc.y + size / 2);

		showStep("centralObstacleIntensity.png", intensity, 1.0 / (size*size*255*3));

		Mat redPoint = image.clone();
		Core.circle(redPoint, maximum, 6, new Scalar(0, 0, 255, 255));
		showStep("obstacleorigin.png", redPoint, 1);

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
	private Map buildMap(Mat image, List<org.opencv.core.Point> corners, Mat centralObstacle)
	{
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		contours.add(new MatOfPoint(corners.get(0), corners.get(1), corners.get(2), corners.get(3)));
		Mat blocked = Mat.zeros(image.size(), CvType.CV_8U);
		Imgproc.drawContours(blocked, contours, -1, new Scalar(1, 1, 1, 255), Core.FILLED);

		if(!settings.ignoreCentralObstacle)
			blocked = blocked.mul(centralObstacle);

		showStep("obstacleMask.png", blocked, 255);

		char obstacle[][] = new char[blocked.width()][blocked.height()];
		for(int y = 0; y < blocked.height(); ++y)
			for(int x = 0; x < blocked.width(); ++x)
				obstacle[x][y] = blocked.get(y, x)[0] == 0.0 ? 'O' : ' ';
		return new Map(obstacle, pixelSize);
	}
	private List<org.opencv.core.Point> getCorners(Mat image, int strategy)
	{
		switch(strategy)
		{
		case 0:
			return adjustedPrototypeSearch(image);
		default:
			System.out.println("Unknown corner strategy: " + settings.boundsStrategy + "\nUsing adjustedPrototypeSearch.");
			return adjustedPrototypeSearch(image);
		}

	}
	private Goal makeGoal(org.opencv.core.Point left, org.opencv.core.Point right, double width)
	{
		int x = (int)(left.x+right.x)/2;
		int y = (int)(left.y+right.y)/2;
		double heading = Math.atan2(right.x-left.x, left.y-right.y);
		return new Goal(new Point(x, y, pixelSize), width, heading);
	}
	private Point locateObstacleCenter(Mat obstacle, int detectorSize)
	{
		Mat intensity = new Mat();
		Mat template = new Mat(new Size(detectorSize, detectorSize), obstacle.type());
		Imgproc.matchTemplate(obstacle, template, intensity, Imgproc.TM_SQDIFF);
		MinMaxLocResult extrema = Core.minMaxLoc(intensity);
		return new Point((int)(extrema.minLoc.x + detectorSize/2), (int)(extrema.minLoc.y + detectorSize/2), pixelSize);
	}
	public void updateMap()
	{
		Mat image = getImage();

		List<org.opencv.core.Point> corners = getCorners(image, settings.boundsStrategy);

		Mat marked = image.clone();
		for(org.opencv.core.Point c : corners)
			Core.circle(marked, c, 6, new Scalar(255, 255, 0), -1);

		//Recover from malplaced corner by mirroring
		if(settings.rectangleIntuition) antiSymmetrify(corners);

		for(org.opencv.core.Point c : corners)
			Core.circle(marked, c, 6, new Scalar(0, 0, 255), -1);

		//Estimate pixel size
		double diagonal = (Proc.distance(corners.get(0), corners.get(2)) + Proc.distance(corners.get(1), corners.get(3)))/2;
		pixelSize = 216.33 / diagonal;
		System.out.println("Estimated pixel size: " + pixelSize + " cm/pixel");

		goals = new ArrayList<Goal>();
		goals.add(makeGoal(corners.get(2), corners.get(1), 20));
		goals.add(makeGoal(corners.get(0), corners.get(3), 10));

		for(Goal g : goals)
			Core.circle(marked, new org.opencv.core.Point(g.center.pixel_x, g.center.pixel_y), 4, new Scalar(0, 255, 255), -1);
		showStep("corners.png", marked, 1.0);

		Mat centralObstacle = detectCentralObstacle(image);
		obstacleCenter = locateObstacleCenter(centralObstacle, 20);
		map = buildMap(image, corners, centralObstacle);
	}
	public RealCamera()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if(settings.testMode) testImage = Highgui.imread(settings.testImageFile);
		else
		{
			capture = new VideoCapture(0);
			capture.read(new Mat());
		}
		updateMap();
		update();
		Mat image = Proc.norm(getImage());
		double floor[] = Proc.estimateFloorColor(image, settings.NW, settings.SE);
		showStep("bisect.png", Proc.classify(image, new Scalar(floor[0], floor[1], floor[2], 255), settings.centralObstacleColor), 255);
	}
	public void update()
	{
		this.balls = new ArrayList<Point>();

		//Ensure image and template have the same type (converTo() doesn't work).
		Mat image = getImage();

		String update = String.format("%05d", updates++);
		System.out.println("Update " + update + " at " + (System.currentTimeMillis() - startTime) + " ms.");
		showStep("update" + update +".png", image, 1);

		Mat ballImg = findBalls(image.clone(), "src/imgInOut/Template.png");
		showStep("balls" + update +".png", ballImg, 1);
		findRobot(image.clone(), "src/imgInOut/greenfront.png", "src/imgInOut/Back.png");
	}

	private Mat findBalls(Mat image, String templFileName)
	{
		Mat templ = Highgui.imread(templFileName);

		int result_cols = image.cols() - templ.cols() + 1;
		int result_rows = image.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32F);

		int matchingMethod = 1;
		while(true) {
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
				this.balls.add(new Point((int)(matchLoc.x + (templ.cols()/2)), (int)(matchLoc.y + (templ.rows()/2)), pixelSize));
			} else {

				break;
			}
		}
		System.out.println("Found " + balls.size() + " balls.");
		return image;
	}

	private void findRobot(Mat image, String front, String back)
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
		//x1, y1 back
		//x2, y2 front
		frontPoint = new Point((int)x2,(int)y2, pixelSize);
		backPoint = new Point((int)x1,(int)y1, pixelSize);
		
		System.out.println("Robot front: " + (int)x2 + ", " + (int)y2 + " px");
		System.out.println("Robot back:  " + (int)x1 + ", " + (int)y1 + " px");
		robot = new Robot(new Point((int)((x1+x2)/2), (int)((y1+y2)/2), pixelSize), Math.atan2(y2-y1, x2-x1), 37/pixelSize, 24/pixelSize);
	}

	//optimer de her senere hvis det bliver nødvendigt
	public void updateRobot(Point expectedPosition, double searchRadius){update();}
	public void updateBalls(){update();}
	public void shutDown(){if(!settings.testMode) capture.release();}

	public Robot getRobot(){return robot;}
	public List<Point> getBalls(){return balls;}
	public List<Goal> getGoals(){return goals;}
	public Map getMap(){return map;}
	public Point getObstacleCenter(){return obstacleCenter;}
}
