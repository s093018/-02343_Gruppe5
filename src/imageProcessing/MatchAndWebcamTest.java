package imageProcessing;

import java.io.File;
import java.net.URL;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class MatchAndWebcamTest
{
	public static void main( String[] args )
	{
		new MatchAndWebcamTest().find("Original2.jpg", "Template2.jpg", "Result2_0.jpg", 0);
		new MatchAndWebcamTest().find("Original2.jpg", "Template2.jpg", "Result2_1.jpg", 1);
		new MatchAndWebcamTest().find("Original2.jpg", "Template2.jpg", "Result2_2.jpg", 2);
		new MatchAndWebcamTest().find("Original2.jpg", "Template2.jpg", "Result2_3.jpg", 3);
		new MatchAndWebcamTest().find("Original2.jpg", "Template2.jpg", "Result2_4.jpg", 4);
		new MatchAndWebcamTest().find("Original2.jpg", "Template2.jpg", "Result2_5.jpg", 5);
	}
	public void find(String imageFileName, String templFileName, String outputName, int matchingMethod){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		URL origURL = getClass().getResource(imageFileName);
		if(origURL == null){
			throw new NullPointerException(imageFileName + " doesn't exist.");
		}
		File origFile = new File(origURL.getPath());
		String origPath = origFile.toString();

		URL templURL = getClass().getResource(templFileName);
		if(templURL == null){
			throw new NullPointerException(templFileName + " doesn't exist.");
		}
		File templFile = new File(templURL.getPath());
		String templPath = templFile.toString();

		Mat image = Highgui.imread(origPath);
		Mat templ = Highgui.imread(templPath);

		int result_cols = image.cols() - templ.cols() + 1;
		int result_rows = image.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		for(int i=1; i<=15; i++){
			Imgproc.matchTemplate(image, templ, result, matchingMethod);

			Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());

			MinMaxLocResult mmlr = Core.minMaxLoc(result);
			Point matchLoc;
			if( matchingMethod  == Imgproc.TM_SQDIFF || matchingMethod == Imgproc.TM_SQDIFF_NORMED ){
				matchLoc = mmlr.minLoc; }
			else{ 
				matchLoc = mmlr.maxLoc; }
//			Core.rectangle(image, matchLoc, new Point(matchLoc.x + templ.cols(),
//					matchLoc.y + templ.rows()), new Scalar(0, 255, 0)); // -1 = fill.
			Core.circle(image, new Point(matchLoc.x + (templ.cols()/2),
					matchLoc.y + (templ.rows()/2)), 5, new Scalar(0, 255, 0), -1); // thickness)


			Highgui.imwrite(outputName, image);
		}
		System.out.println("Writing "+ outputName);
	}
}

///////////////////////////////////////////////////
// Webcam-eksempel
///////////////////////////////////////////////////
//		System.loadLibrary("opencv_java248");
//
//		VideoCapture cap = new VideoCapture(0);
//
//		if(!cap.isOpened()){
//			System.out.println("Ikke forbundet til kamera");
//		}else System.out.println("Fandt webcam: " + cap.toString());
//		Mat frame = new Mat();
//		cap.retrieve(frame);
//		
//		Highgui.imwrite("pic1.jpg", frame);
//		Mat frameBlur = new Mat();
//		Imgproc.blur(frame, frameBlur, new Size(5,5));
//		Highgui.imwrite("pic2-blurred.jpg", frameBlur);
//		
//		Imgproc.GaussianBlur(frame, frameBlur, new Size(25,25), 20);
//		Highgui.imwrite("pic3-blurred.jpg", frameBlur);
//		cap.release();
//}
