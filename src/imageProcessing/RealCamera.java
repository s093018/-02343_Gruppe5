package imageProcessing;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
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

	private VideoCapture capture;
	private Configuration settings = new Configuration("src/imgInOut/settings.cfg");
	private Mat testImage;

	private double pixelSize;
	private double floorColor[];


	private boolean compareColors(double[] a, double[] b)
	{
		for(int i = 0; i < 3; ++i)
			if(a[i] != b[i])
				return false;
		return true;
	}
	private Mat replaceColor(Mat image, double[] source, double[] dest)
	{
		Mat result = new Mat(image.size(), image.type());
		result.setTo(image);
		for(int j = 0; j < image.rows(); ++j)
			for(int i = 0; i < image.cols(); ++i)
				if(compareColors(image.get(j, i), source))
					result.put(j, i, dest);
		return result;
	}
	private void saveImage(String filename, Mat image, double scaling)
	{
		if(image.get(0, 0).length == 1)
		{
			Mat scaledImage = image.mul(Mat.ones(image.size(), image.type()), scaling);
			List<Mat> layers = new ArrayList<Mat>();
			layers.add(scaledImage.clone());
			layers.add(scaledImage.clone());
			layers.add(scaledImage.clone());
			Mat result = new Mat();
			Core.merge(layers, result);
			Highgui.imwrite("src/imgInOut/" + filename, result);
		}
		else
		{
			Mat scaler = new Mat(image.size(), image.type(), new Scalar(scaling, scaling, scaling, 255));
			Highgui.imwrite("src/imgInOut/" + filename, scaler.mul(image));
		}
	}
	private void showStep(String filename, Mat image, double scaling)
	{
		if(settings.showSteps)
			saveImage(filename, image, scaling);
	}
	private Mat getImage()
	{
		if(settings.testMode) return testImage;
		else
		{
			Mat frame = new Mat();
			capture.retrieve(frame);
			return frame;
		}
	}
	private List<Point> cornerSearch(Mat image)
	{
		double[] red = {0.0, 0.0, 255.0};//Apparently, OpenCV format is BGRA
		Mat NE = replaceColor(Highgui.imread("src/imgInOut/NE.PNG"), red, floorColor);
		Mat NW = replaceColor(Highgui.imread("src/imgInOut/NW.PNG"), red, floorColor);
		Mat SE = replaceColor(Highgui.imread("src/imgInOut/SE.PNG"), red, floorColor);
		Mat SW = replaceColor(Highgui.imread("src/imgInOut/SW.PNG"), red, floorColor);

		Highgui.imwrite("src/imgInOut/NETest.png", NE);
		Highgui.imwrite("src/imgInOut/NWTest.png", NW);
		Highgui.imwrite("src/imgInOut/SETest.png", SE);
		Highgui.imwrite("src/imgInOut/SWTest.png", SW);
		return null;
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
	private Mat norm(Mat image)
	{
		List<Mat> layers = new ArrayList<Mat>();
		layers.add(new Mat());
		layers.add(new Mat());
		layers.add(new Mat());
		Core.split(image, layers);

		List<Mat> newLayers = new ArrayList<Mat>();
		newLayers.add(new Mat());
		newLayers.add(new Mat());
		newLayers.add(new Mat());

		Mat divisor = brightness(layers);
		Core.divide(layers.get(0), divisor, newLayers.get(0), 64);
		Core.divide(layers.get(1), divisor, newLayers.get(1), 64);
		Core.divide(layers.get(2), divisor, newLayers.get(2), 64);
		Mat result = new Mat();
		Core.merge(newLayers, result);

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
		return mask;
	}
	private Mat canny(Mat image)
	{
		Mat UMat = new Mat();
		image.convertTo(image, CvType.CV_8U);
		Mat edges = new Mat();
		Imgproc.Canny(image, edges, 200, 50);
		return show(edges);
	}
	private Mat show(Mat mask)
	{
		List<Mat> layers = new ArrayList<Mat>();
		layers.add(new Mat());
		layers.add(new Mat());
		layers.add(new Mat());
		for(Mat m : layers)
			mask.copyTo(m);
		Mat result = new Mat();
		Core.merge(layers, result);
		return result;
	}

	private Mat floodFillDetection(Mat image)
	{
		//TODO: Get rid of holes/goals
		return floodFill(smooth(image), 2.0, 0.2);
	}
	private Mat prototypeBoundsDetection(Mat image)
	{
		Mat template = Highgui.imread(settings.obstaclePrototype);
		Mat match = new Mat();
		System.out.println(CvType.CV_8U);
		System.out.println(CvType.CV_32F);
		System.out.println(image.depth());
		System.out.println(template.depth());
		Imgproc.matchTemplate(image, template, match, Imgproc.TM_CCORR);
		Mat result = new Mat();
		Imgproc.threshold(match, result, settings.woodTreshold, 1, Imgproc.THRESH_BINARY);
		return result;
	}
	private org.opencv.core.Point findCorner(Mat image, int quadrant)
	{
		//Build prototype
		double[] blue = {255.0, 0.0, 0.0};
		Mat prototype = replaceColor(Highgui.imread(settings.cornerPrototypes[quadrant-1]), blue, floorColor);

		//Select quadrant
		int width = image.width()/2;
		int height = image.height()/2;
		int xOff = (quadrant == 1 || quadrant == 4) ? width : 0;
		int yOff = (quadrant > 2) ? height / 2 : 0;
		Mat Q = image.submat(yOff, yOff + width, xOff, xOff + height);

		//maxloc on Q
		Mat match = new Mat();
		Imgproc.matchTemplate(image, prototype, match, Imgproc.TM_SQDIFF);
		return Core.minMaxLoc(match).maxLoc;
	}
	private Mat cornerBasedDetection(Mat image)
	{
		Mat course = Mat.ones(image.size(), CvType.CV_8U);
		List<MatOfPoint> points = new ArrayList<MatOfPoint>();
		points.add(new MatOfPoint(findCorner(image, 1), findCorner(image, 2), findCorner(image, 3), findCorner(image, 4)));
		Imgproc.drawContours(course, points, -1, new Scalar(0, 0, 0, 255), Core.FILLED);
		return null;
	}
	private Mat detectBounds(Mat image, int strategy)
	{
		//TODO: Floodfill to select outermost structure to avoid robot being seen as obstacle
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
	private Mat detectCentralObstacle(Mat image)
	{
		int size = settings.centralObstacleSize;
		Scalar tolerance = settings.centralObstacleTolerance;
		Scalar white = new Scalar(255, 255, 255, 255);
		//Look for whitest area
		Mat detector = new Mat(new Size(size, size), image.type());
		detector.setTo(white);
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
		Imgproc.floodFill(image, obstacleMask, maximum, white, null, tolerance, white, Imgproc.FLOODFILL_FIXED_RANGE | Imgproc.FLOODFILL_MASK_ONLY);

		//TODO: Maybe dilate mask as well?

		Mat result = new Mat();
		Core.subtract(Mat.ones(obstacleMask.size(), CvType.CV_8U), obstacleMask, result);
		return result;
	}
	private double estimatePixelSize(Mat image, Mat bounds)
	{
		//TODO: Find red+green dots, divide 10 by pixel distance
		//Alternatively, use size of non-blocked area (ignoring central obstacle)
		return 0.58;
	}
	public RealCamera()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if(settings.testMode)
		{
			testImage = Highgui.imread(settings.testImageFile);
			testImage.convertTo(testImage, CvType.CV_32F);
			System.out.println("Converted to " + testImage.depth());
		}
		else capture = new VideoCapture(0);

		Mat image = getImage();
		showStep("input.png", image, 1.0);

		floorColor = estimateFloorColor(image);

		//Find obstacles
		Mat bounds = detectBounds(image, settings.boundsStrategy);
		pixelSize = estimatePixelSize(image, bounds);

		Mat blocked = bounds.mul(detectCentralObstacle(image), 255);//642x482

		char obstacle[][] = new char[blocked.width()][blocked.height()];
		for(int y = 0; y < blocked.height(); ++y)
			for(int x = 0; x < blocked.width(); ++x)
				obstacle[x][y] = blocked.get(y, x)[0] == 0.0 ? '\0' : '\1';
		map = new Map(obstacle, pixelSize);

		showStep("obstacleMask.png", blocked, 255);

		//Find initial position of balls + robot
		update();
	}
	public void update()
	{
		balls = new ArrayList<Point>();

		//Ensure image and template have the same type (converTo() doesn't work).
		Highgui.imwrite("frame.png", getImage());
		Mat image = Highgui.imread("frame.png");

		Mat templ = Highgui.imread("src/imgInOut/Template.png");

		int result_cols = image.cols() - templ.cols() + 1;
		int result_rows = image.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32F);

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
				balls.add(new Point((int)(matchLoc.x + (templ.cols()/2)), (int)(matchLoc.y + (templ.rows()/2)), pixelSize));
				Core.circle(image, new org.opencv.core.Point(matchLoc.x + (templ.cols()/2),
						matchLoc.y + (templ.rows()/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
			} else {
				break;
			}
		}
		Highgui.imwrite("src/imgInOut/Result.jpg", image);
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
