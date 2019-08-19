import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import com.github.sarxos.webcam.Webcam;


public class ImageIN {
	
	private Webcam webcam;
	
	public ImageIN()
	{
		
	}
	
	public void newPictureStream()
	{
		webcam = Webcam.getDefault();
		Dimension d = new Dimension();
		d.setSize(640, 480);
		webcam.setViewSize(d);		
		webcam.open();
	}
	
	public Mat newPicture()
	{
		BufferedImage image = webcam.getImage();
				
		Mat m = BufferedImage2Mat(image);
				
		return m;
	}
	
	public void closeWebcam()
	{
		webcam.close();
	}
	
	
	public Mat newSinglePicture()
	{
		webcam.close();
		webcam = Webcam.getDefault();
		Dimension d = new Dimension();
		d.setSize(640, 480);
		webcam.setViewSize(d);
		
		webcam.open();
		
		long time = System.currentTimeMillis();

		BufferedImage image = webcam.getImage();
		
		System.out.println(System.currentTimeMillis() - time);
		
		Mat m = BufferedImage2Mat(image);
		
		System.out.println(System.currentTimeMillis() - time);
		
		webcam.close();

		return m;
	}
	
	private static Mat BufferedImage2Mat(BufferedImage image) {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    
		Mat toReturn = new Mat();

	    try {
		    ImageIO.write(image, "jpg", byteArrayOutputStream);
		    byteArrayOutputStream.flush();
		    toReturn = Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_COLOR);
		    byteArrayOutputStream.close();
	    } catch (Exception e)
	    {
	    }
	    
	    return toReturn;
	}
	
	public static BufferedImage Mat2Buff(Mat matrix) {     
		try {
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".png", matrix, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
		} catch (Exception e) { return null;}
	}
	
	public static BufferedImage getImgPolar(Mat overLay, double [][] lines)
	{
		
        for (int i = 0; i < lines[0].length; i++) 
        {
            double rho = lines[0][i];
            double theta = lines[1][i];
            double a = Math.cos(theta);
            double b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));

            Imgproc.line(overLay, pt1, pt2, new Scalar(255, 255,255), 1, Imgproc.LINE_AA, 0);
        }
        
        return Mat2Buff(overLay);
	}
	
	public static Mat getImgXYPoints(Mat overLay, Mat lines)
	{
        for (int i = 0; i < lines.height(); i++) 
        {
            Point pt1 = new Point(lines.get(i, 0)[0], lines.get(i, 0)[1]);
            Point pt2 = new Point(lines.get(i, 0)[2], lines.get(i, 0)[3]);

            Imgproc.line(overLay, pt1, pt2, new Scalar(0, 255,0), 1, Imgproc.LINE_AA, 0);
        }
        
        return overLay;
	}
	
	public static Mat drawAll(double [][] angleLocations)
	{
		Mat out = new Mat(1080, 1920, CvType.CV_8UC3);
		
		 for (int i = 0; i < angleLocations.length; i++) 
	     {
			 double sin50 = Math.sin(angleLocations[i][0]) * 50;
			 double cos50 = Math.cos(angleLocations[i][0]) * 50;
			 
			 int x1 = (int) (angleLocations[i][1] - cos50);
			 int y1 = (int) (angleLocations[i][2] - sin50);
			 int x2 = (int) (angleLocations[i][1] + cos50);
			 int y2 = (int) (angleLocations[i][2] + sin50);
			 
			 
			 Point pt1 = new Point(x1, y1);
	         Point pt2 = new Point(x2,y2); 
	         
	         Imgproc.line(out, pt1, pt2, new Scalar(255, 255,255), 1, Imgproc.LINE_AA, 0);
	     }
		 return out;
	}
	
	public static Mat drawLine(Mat in, double [] angleLocations)
	{		
		
			 double sin50 = Math.sin(angleLocations[0]) * 100;
			 double cos50 = Math.cos(angleLocations[0]) * 100;
			 
			 int x1 = (int) (angleLocations[1] - cos50);
			 int y1 = (int) (angleLocations[2] - sin50);
			 int x2 = (int) (angleLocations[1] + cos50);
			 int y2 = (int) (angleLocations[2] + sin50);
			 
			 
			 Point pt1 = new Point(x1, y1);
	         Point pt2 = new Point(x2,y2); 
	         
	         Imgproc.line(in, pt1, pt2, new Scalar(255, 0,0), 5, Imgproc.LINE_AA, 0);
	     
		 return in;
	}
	
}