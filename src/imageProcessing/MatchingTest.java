package imageProcessing;

import java.io.File;
import java.net.URL;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class MatchingTest
{
	static String imageFileName = "../imgInOut/Original.jpg";
	static String templFileName = "../imgInOut/Template.jpg";
	public static void main( String[] args )
	{
		// Tries to match Template.jpg on top of Original.jpg by using all available matching-methods.
		// Creates six images in the imgInOut package and overwrites them if the program is run again.
		// Refresh project folder after running program if files do not appear.

		Thread m0 = new Thread() {
			public void run() {
				new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method0.jpg", 0);
			}
		};

		Thread m1 = new Thread() {
			public void run() {
				new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method1.jpg", 1);
			}
		};

		Thread m2 = new Thread() {
			public void run() {
				new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method2.jpg", 2);
			}
		};

		Thread m3 = new Thread() {
			public void run() {
				new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method3.jpg", 3);
			}
		};

		Thread m4 = new Thread() {
			public void run() {
				new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method4.jpg", 4);
			}
		};

		Thread m5 = new Thread() {
			public void run() {
				new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method5.jpg", 5);
			}
		};

		m0.start();
		m1.start();
		m2.start();
		m3.start();
		m4.start();
		m5.start();

		//		new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method0.jpg", 0);
		//		new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method1.jpg", 1);
		//		new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method2.jpg", 2);
		//		new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method3.jpg", 3);
		//		new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method4.jpg", 4);
		//		new MatchingTest().find(imageFileName, templFileName, "src/imgInOut/Result_Method5.jpg", 5);
	}

	public void find(String imageFileName, String templFileName, String outputName, int matchingMethod) {
		System.out.println("Matcher: "+ outputName);

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		URL origURL = getClass().getResource(imageFileName);
		if(origURL == null) {
			throw new NullPointerException(imageFileName + " doesn't exist.");
		}
		File origFile = new File(origURL.getPath());
		String origPath = origFile.toString();

		URL templURL = getClass().getResource(templFileName);
		if(templURL == null) {
			throw new NullPointerException(templFileName + " doesn't exist.");
		}
		File templFile = new File(templURL.getPath());
		String templPath = templFile.toString();

		Mat image = Highgui.imread(origPath);
		Mat templ = Highgui.imread(templPath);

		int result_cols = image.cols() - templ.cols() + 1;
		int result_rows = image.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		while(true) {

			Imgproc.matchTemplate(image, templ, result, matchingMethod);

			MinMaxLocResult mmlr = Core.minMaxLoc(result);

			Point matchLoc;
			if(matchingMethod  == Imgproc.TM_SQDIFF || matchingMethod == Imgproc.TM_SQDIFF_NORMED ) {
				matchLoc = mmlr.minLoc; 
			} else { 
				matchLoc = mmlr.maxLoc; 
			}

			double thresholdMatch = 0.25;
			if(mmlr.minVal < thresholdMatch) {
				Core.circle(image, new Point(matchLoc.x + (templ.cols()/2),
						matchLoc.y + (templ.rows()/2)), 6, new Scalar(0, 0, 255), -1); // -1 = fill)
			} else{
				System.out.println(outputName + " done");
				break;
			}
			Highgui.imwrite(outputName, image);
		}
	}
}