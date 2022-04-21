package fr.legrain.bdg.webapp.edition.birt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;

public class WebViewerUtil {
	
	static Logger logger = Logger.getLogger(WebViewerUtil.class.getName());
	public static final String C_FORMAT_HTML = "html";
	public static final String C_FORMAT_PDF = "pdf";
	public static final String C_NOM_WEBAPP = "viewer";
	
	
	static public void runReport(String reportFile, String outputFile, String format, Map<String,String> reportParam) {
		try {
		//Engine Configuration - set and get temp dir, BIRT home, Servlet context
		EngineConfig config = new EngineConfig();
		//config.setEngineHome( "C:/birt-runtime-2_0_1/birt-runtime-2_0_1/Report Engine" );
		
//		System.setProperty( "RUN_UNDER_ECLIPSE", "true" );
		//config.setConfigurationVariable("RUN_UNDER_ECLIPSE", "true");
		config.setProperty("RUN_UNDER_ECLIPSE", "true");
		//DtpManifestExplorer.getInstance( ).getExtensionManifests( );
		
		//Create the report engine
		ReportEngine engine = new ReportEngine( config );
		
		//Open a report design - use design to modify design, retrieve embedded images etc. 
		IReportRunnable design = engine.openReportDesign(reportFile);
		
		//Create task to run the report - use the task to execute and run the report,
		IRunAndRenderTask task = engine.createRunAndRenderTask(design); 
		
		for (String param : reportParam.keySet()) {
			task.setParameterValue(param,reportParam.get(param));
		}
		
		//Set Render context to handle url and image locataions
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory("image");
		HashMap contextMap = new HashMap();
		contextMap.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext );
		task.setAppContext( contextMap );
		
		//Set rendering options - such as file or stream output, 
		//output format, whether it is embeddable, etc
		HTMLRenderOption options = new HTMLRenderOption();
		
		options.setOutputFileName(outputFile);
		options.setOutputFormat(format);
		
		task.setRenderOption(options);
		
		//run the report and destroy the engine
		task.run();
		
		engine.destroy();
		adobeAcrobatPrint(outputFile);
		} catch(EngineException e) {
			logger.error("",e);
		}
	}
	
	static public void adobeAcrobatPrint(String pdfFile) {
		try {
			String osName = System.getProperty("os.name" );
			//FOR WINDOWS 95 AND 98 USE COMMAND.COM
			if(osName.equals("Windows 95") || osName.equals("Windows 98")){		
				Runtime.getRuntime().exec(
						"command.com /C start acrord32 /p /h" + pdfFile);
			}
			//FOR WINDOWS NT/XP/2000 USE CMD.EXE
			else {
				Runtime.getRuntime().exec("cmd.exe start /C acrord32 /p /h" + pdfFile);
			}
		} catch (IOException e) {
			logger.error("",e);
		}
	}
	
}
