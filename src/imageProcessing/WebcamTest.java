package imageProcessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class WebcamTest {

	public static void main(String[] args) {
		
		// Connects to the PC webcam and takes a picture.
		// Also blurs the picture twice and saves them all in the imgInOut package.
		// Refresh project folder after running program if files do not appear.
		
		System.loadLibrary("opencv_java248");

		VideoCapture cap = new VideoCapture(0);

		if(!cap.isOpened()){
			System.out.println("Not connected to webcam");
		}else System.out.println("Found webcam: " + cap.toString());
		Mat frame = new Mat();
		cap.retrieve(frame);
		
		Highgui.imwrite("src/imgInOut/WebcamPic.jpg", frame);
		Mat frameBlur = new Mat();
		Imgproc.blur(frame, frameBlur, new Size(5,5));
		Highgui.imwrite("src/imgInOut/WebcamPicBlur.jpg", frameBlur);
		
		Imgproc.GaussianBlur(frame, frameBlur, new Size(25,25), 20);
		Highgui.imwrite("src/imgInOut/WebcamPicBlur2.jpg", frameBlur);
		cap.release();
	}

}
