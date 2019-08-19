import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoCap {
	
	
	String num;
	public VideoCap(int num)
	{
		this.num = Integer.toString(num);
	}
	
	public Mat [] getFrames()
	{		
		VideoCapture cap = new VideoCapture();
		
	    String input = "/Users/Drew/Desktop/Flips/flip" + num + ".mp4";
	
	    cap.open(input);

	    ArrayList<Mat> mats = new ArrayList<Mat>();
	   	 
	    Mat frame = new Mat();
	   
	    cap.open(input);
	   
	    if (cap.isOpened())
		{
		    while(cap.read(frame))
		    {
		    		mats.add(frame.clone());
		    }
		    cap.release();
		}
	  
	    return mats.toArray(new Mat [mats.size()]);
	}
	
	public Mat [] getFilteredFrames()
	{		
		Mat [] frames = getFrames();
		
		for (int i = 0; i < frames.length; i++)
		{
			frames[i] = filter(frames[i]);
			System.out.println(i);
		}	
		return frames;
	}
	
	private Mat filter(Mat in)
	{
		int xsize = (int) in.size().width;
		int ysize = (int) in.size().height;
		
		Mat out = new Mat(xsize, ysize, CvType.CV_8UC1);
		
		for (int x = 0; x < xsize - 1; x ++)
		{
			for (int y = 0; y < ysize - 1; y ++)
			{
				double [] rgb = in.get(y, x);
				
				if (rgb[2] - 20 > 2 * rgb[1] & rgb[2] - 20 > 2 * rgb[0])
				{
					out.put(x, y, (rgb[0] + 255) / 2 );
				}
				else
				{
					out.put(x, y, 0);
				}
			}
		}
		return out;
	}	
}