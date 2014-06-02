package imageProcessing;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.opencv.core.Scalar;


//Uses fully qualified OpenCV Point classes since we already have a Point class.
public class Configuration
{
	public final org.opencv.core.Point NW;
	public final org.opencv.core.Point SE;
	public final org.opencv.core.Point floodFillOrigin;
	public final boolean testMode;
	public final String testImageFile;
	public final boolean showSteps;
	public final int boundsStrategy;
	public final int pixelSizeStrategy;
	public final String obstaclePrototype;
	public final int centralObstacleSize;
	public final org.opencv.core.Scalar centralObstacleTolerance;

	public Configuration(String filename)
	{
		Properties props = load(filename);
		NW = getPoint(props, "NW", new org.opencv.core.Point(160, 120));
		SE = getPoint(props, "NW", new org.opencv.core.Point(480, 360));
		testMode = getInt(props, "testMode", 0) != 0;
		testImageFile = testMode ? getString(props, "testImageFile", "src/imgInOut/TESTDATA.JPG") : null;
		showSteps = getInt(props, "showSteps", 1) != 0;
		boundsStrategy = getInt(props, "boundsStrategy", 0);
		pixelSizeStrategy = getInt(props, "pixelSizeStrategy", 0);
		floodFillOrigin = getPoint(props, "floodFillOrigin", new org.opencv.core.Point(320, 240));
		obstaclePrototype = getString(props, "obstaclePrototype", "src/imgInOut/woodPrototype.png");
		centralObstacleSize = getInt(props, "centralObstacleSize", 20);
		centralObstacleTolerance = getScalar(props, "centralObstacleTolerance", new Scalar(20, 20, 20, 255));
	}
	private Properties load(String filename)
	{
		Properties props = new Properties();
		try
		{
			//TODO: Convert keys to lowercase (also when extracting)
			FileInputStream configFile = new FileInputStream(filename);
			props.load(configFile);
			configFile.close();
		}
		catch(IOException e){System.out.println("Cannot open " + filename + "!");}
		return props;
	}

	private static int []extractInts(String value, int count) throws NullPointerException, NumberFormatException
	{
		if(value == null) throw new NullPointerException();
		String []fragments = value.split(",", count);
		int []ints = new int[fragments.length];
		for(int i = 0; i < ints.length; ++i)
		{
			ints[i] = Integer.valueOf(fragments[i].trim());
		}
		return ints;
	}
	private static int getInt(Properties props, String key, int defaultValue)
	{
		try
		{
			return Integer.valueOf(props.getProperty(key));
		}
		catch(NumberFormatException nfe){System.out.println("Bad value for " + key + ", using default (" + defaultValue + ").");}
		catch (NullPointerException npe){System.out.println("Missing value for " + key + ", using default (" + defaultValue + ").");}
		return defaultValue;
	}
	private static double getDouble(Properties props, String key, double defaultValue)
	{
		try
		{
			try
			{
				return Double.valueOf(props.getProperty(key));
			}
			catch(NumberFormatException nfe)//Allow for omitting periods
			{
				return Integer.valueOf(props.getProperty(key));
			}
		}
		catch(NumberFormatException nfe){System.out.println("Bad value for " + key + ", using default (" + defaultValue + ").");}
		catch (NullPointerException npe){System.out.println("Missing value for " + key + ", using default (" + defaultValue + ").");}
		return defaultValue;
	}
	private static String getString(Properties props, String key, String defaultValue)
	{
		String s = props.getProperty(key);
		if(s == null)
		{
			System.out.println("Bad value for " + key + ", using default (" + defaultValue + ").");
			return defaultValue;
		}
		else return s;
	}
	private static org.opencv.core.Point getPoint(Properties props, String key, org.opencv.core.Point defaultValue)
	{
		try
		{
			int []coordinates = extractInts(props.getProperty(key), 2);
			if(coordinates[0] < 0 || coordinates[1] < 0) throw new NumberFormatException();
			return new org.opencv.core.Point(coordinates[0], coordinates[1]);
		}
		catch(NumberFormatException nfe){System.out.println("Bad value for " + key + ", using default (" + defaultValue + ").");}
		catch (NullPointerException npe){System.out.println("Missing value for " + key + ", using default (" + defaultValue + ").");}
		return defaultValue;
	}
	private org.opencv.core.Scalar getScalar(Properties props, String key, org.opencv.core.Scalar defaultValue)
	{
		try
		{
			int []colors = extractInts(props.getProperty(key), 3);
			return new org.opencv.core.Scalar(colors[0], colors[1], colors[2], 255);
		}
		catch(NumberFormatException nfe){System.out.println("Bad value for " + key + ", using default (" + defaultValue + ").");}
		catch (NullPointerException npe){System.out.println("Missing value for " + key + ", using default (" + defaultValue + ").");}
		return defaultValue;
	}
}
