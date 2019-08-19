import java.io.File;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.Tika;

import org.xml.sax.SAXException;

public class Test {
    public static void main(final String[] args) {
        //Your way
        try {
            File file = new File("/Users/Drew/Desktop/ex.pdf");
            String content = new Tika().parseToString(file);
            
            String [] splitted = content.split(" ");
            
            ArrayList<String> listString = new ArrayList<String>();
            ArrayList<String> listCount = new ArrayList<String>();

            
            for (int i = 0; i < splitted.length; i ++)
            {
            		if (listString.contains(splitted[i]));
            	
            }
            
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public static String tikaPdfTest(final String fileName) throws IOException, SAXException, TikaException {
        try(final FileInputStream inputstream = new FileInputStream(new File(fileName))) {
            final BodyContentHandler handler = new BodyContentHandler();
            new PDFParser().parse(inputstream, handler, new Metadata(), new ParseContext());
            return handler.toString().trim();
        }
    }
}