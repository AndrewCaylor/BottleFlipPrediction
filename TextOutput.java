import java.io.BufferedWriter;
import java.io.FileWriter;

public class TextOutput {

	public static void newFile(String destination, String Data)
	{
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(destination));
	    writer.write(Data);
	    writer.close();
		}
		catch(Exception e)
		{
			System.out.println("frick");
		}
	}
	
}
