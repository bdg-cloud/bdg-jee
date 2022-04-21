package fr.legrain.bdg.webapp.codebarre;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Constants;
import org.krysalis.barcode4j.impl.code128.Code128LogicImpl;
import org.krysalis.barcode4j.impl.code128.EAN128AI;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128LogicImpl;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class TestBarcode4J {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestBarcode4J d = new TestBarcode4J();
		try {
			d.testSupportRequests();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testSupportRequests() throws Exception {
        final char FNC1 = Code128LogicImpl.FNC_1;
        final char GS = EAN128Bean.DEFAULT_GROUP_SEPARATOR;
        final char CD = EAN128Bean.DEFAULT_CHECK_DIGIT_MARKER;
        
        EAN128Bean bean = new EAN128Bean();
        EAN128LogicImpl impl;
        //241 FAPC500 10 78979779
        
        impl = new EAN128LogicImpl(ChecksumMode.CP_AUTO, null);
        impl.addAI("241FAPC500",0,EAN128AI.getAI("241", EAN128AI.TYPEAlphaNum));
        impl.addAI("1078979779",0,EAN128AI.getAI("10", EAN128AI.TYPEAlphaNum));
        
       
        
        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getHumanReadableMsg());
        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getMessage());
        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getCode128Msg());
        System.out.println("TestBarcode4J.testSupportRequests() : "+impl.getEncodedMessage(impl.getCode128Msg()));
        
        final int dpi = 150;
        
        bean.setChecksumMode(ChecksumMode.CP_AUTO);
        bean.setOmitBrackets(false);
        bean.setCodeset(Code128Constants.CODESET_C);
        bean.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        bean.doQuietZone(true);
        bean.setQuietZone(5);
        bean.setFontSize(2d);

      //Configure the barcode generator
      bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
                                                       //width exactly one pixel
      //bean.setWideFactor(3);
      bean.doQuietZone(false);

      //Open output file
      File outputFile = new File("/home/nicolas/Téléchargements/out.png");
      OutputStream out = new FileOutputStream(outputFile);
      try {
          //Set up the canvas provider for monochrome PNG output 
          BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                  out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

       // bean.setTemplate("(241)n7+cd+(10)n10+cd");
          //Generate the barcode
          bean.generateBarcode(canvas, "021111111111116"+GS+"10LBR182_18/07/17_1");
          //bean.generateBarcode(canvas, "10AB-123");

          //Signal end of generation
          canvas.finish();
      } finally {
          out.close();
      }
         
    }

}
