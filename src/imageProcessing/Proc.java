package imageProcessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Proc
{
	public static Mat norm(Mat image)
	{
		double mul;
		double[] color;
		Mat result = new Mat(image.size(), image.type());
		for(int i = 0; i < image.height(); ++i)
		{
			for(int j = 0; j < image.width(); ++j)
			{
				color = image.get(i, j);
				mul = 1.0/(255.0*(color[0]+color[1]+color[2]));
				image.put(i, j, color[0]*mul, color[1]*mul, color[2]*mul);
			}
		}
		return result;
	}
	public static Mat canny(Mat image)
	{
		image.convertTo(image, CvType.CV_8U);
		Mat edges = new Mat();
		Imgproc.Canny(image, edges, 200, 50);
		return edges;
	}
	public static void saveImage(String filename, Mat image, double scaling)
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
	private static boolean compareColors(double[] a, double[] b)
	{
		for(int i = 0; i < 3; ++i)
			if(a[i] != b[i])
				return false;
		return true;
	}
	public static Mat replaceColor(Mat image, double[] source, double[] dest)
	{
		Mat result = image.clone();
		for(int j = 0; j < image.rows(); ++j)
			for(int i = 0; i < image.cols(); ++i)
				if(compareColors(image.get(j, i), source))
					result.put(j, i, dest);
		return result;
	}
}
