package fr.legrain.bdg.webapp.app;
//package fr.legrain.bdg.webapp.app;
//
//import java.util.Date;
//import java.util.Map;
//import org.eclipse.birt.report.utility.filename.*;
//
///*
// * 
// * 	<!-- Filename generator class/factory to use -->
// *	<context-param>
// *		<param-name>BIRT_FILENAME_GENERATOR_CLASS</param-name>
// *      <param-value>fr.legrain.bdg.webapp.app.MyFilenameGenerator</param-value>
// *  </context-param>
// * 
// * 
// * http://birtworld.blogspot.fr/2008/09/naming-exported-files-from-birt.html
// */
//public class MyFilenameGenerator implements IFilenameGenerator{
//
//	public static final String DEFAULT_FILENAME = "BIRTReport"; 
//
//	public String getFilename( String baseName, String extension, String outputType, Map options ) {
//		return makeFileName( baseName, extension );
//	}
//	
//	public static String makeFileName( String fileName, String extensionName ) {
//		String baseName = fileName;
//		if (baseName == null || baseName.trim().length() <= 0) {
//			baseName = DEFAULT_FILENAME;
//		}
//
//		// check whether the file name contains non US-ASCII characters
//		for (int i = 0; i < baseName.length(); i++) {
//			char c = baseName.charAt(i);
//
//			// char is from 0-127
//			if (c < 0x00 || c >= 0x80) {
//				baseName = DEFAULT_FILENAME;
//				break;
//			}
//		}
//
//		// append extension name
//		if (extensionName != null && extensionName.length() > 0) {
//			baseName += (new Date()).toString() + "." + extensionName; 
//		}
//		return baseName;
//	}
//}