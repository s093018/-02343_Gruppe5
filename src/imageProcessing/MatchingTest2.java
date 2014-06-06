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

public class MatchingTest2 {
	//	static String imageFileName = "../imgInOut/Original.jpg";
	//	static String templFileName = "../imgInOut/Template.jpg";
	public static void main( String[] args )
	{

		System.out.println("1");
		new MatchingTest2().find("../imgInOut/robottestinput.png", "../imgInOut/Template.png", "src/imgInOut/k1.png", 1, 0.25);
		System.out.println("2");
		new MatchingTest2().find("../imgInOut/k1.png", "../imgInOut/Front.png", "src/imgInOut/k2.png", 0, 0.25);
		System.out.println("3");
		new MatchingTest2().find("../imgInOut/k2.png", "../imgInOut/Back.png", "src/imgInOut/k3.png", 0, 0.25);
	}

	public void find(String imageFileName, String templFileName, String outputName, int matchingMethod, double thresholdMatch) {
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
