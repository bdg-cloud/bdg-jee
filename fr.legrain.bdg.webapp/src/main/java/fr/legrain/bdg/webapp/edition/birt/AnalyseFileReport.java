//package fr.legrain.bdg.webapp.edition.birt;
//
//import java.io.InputStream;
//
//import org.apache.log4j.Logger;
//import org.eclipse.birt.core.framework.Platform;
//import org.eclipse.birt.report.model.api.DesignConfig;
//import org.eclipse.birt.report.model.api.IDesignEngine;
//import org.eclipse.birt.report.model.api.IDesignEngineFactory;
//import org.eclipse.birt.report.model.api.ReportDesignHandle;
//import org.eclipse.birt.report.model.api.SessionHandle;
//
//import com.ibm.icu.util.ULocale;
//
///**
// * @author lee
// * Lecture des informations dans un fichier d'édition birt (.rptdesign)
// * 
// * ajouter les attributes pour l'edition 
// * 
// * 16/03/2010 Zhaolin
// */
//public class AnalyseFileReport {
//	
//	static Logger logger = Logger.getLogger(AnalyseFileReport.class.getName());
//
//	private ReportDesignHandle designHandle = null;
//	//Configure the Engine and start the Platform
//	private DesignConfig config = new DesignConfig( );
//	private IDesignEngine engine = null;
//	private SessionHandle session = null;
//	private IDesignEngineFactory factory = null;
//	private String path = null;
//	
//	/**
//	 * Prépare à la lecture du fichier
//	 * @param pathFileEdition - chemin du fichier d'édition
//	 */
//	public void initializeBuildDesignReportConfig(String pathFileEdition, InputStream is){
//		try{
//			
//			Platform.startup( config );
//			factory = (IDesignEngineFactory)Platform.createFactoryObject( IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY );
//			engine = factory.createDesignEngine(config);
//			
//			session = engine.newSessionHandle( ULocale.FRENCH ) ;
//			if(is!=null) {
//				designHandle = session.openDesign(pathFileEdition,is);
//			} else {
//				designHandle = session.openDesign(pathFileEdition);
//			}
//			path = pathFileEdition;
//			
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//	}
//	
//	public void ajouteLogo() {
//		try {
//			String chemin = path;
//			
////			designHandle
////			
////			
////			designHandle.saveAs(chemin);
////			InputStream targetStream = new FileInputStream(new File(chemin));
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void initializeBuildDesignReportConfig(String pathFileEdition){
//		initializeBuildDesignReportConfig(pathFileEdition,null);
//	}
//	
//	public void closeDesignReportConfig(){
//		if(designHandle!=null) {
//			designHandle.close();
//		}
//	}
//	
//	/**
//	 * @return - la propriété commentaire de l'edition
//	 */
//	public String findCommentsReport(){
//		return (String)designHandle.getProperty("comments");
//	}
//	
//	public String findDescription(){
//		return (String)designHandle.getDisplayDescription();
//	}
//	
//	public String findOrientationReport(){
//		return (String)designHandle.getMasterPages().get(0).getProperty("orientation");
//	}
//	
//	public String findSubjectReport(){
//		return (String)designHandle.getProperty("subject");
//	}
//	
//	public String findTitleReport(){
//		return (String)designHandle.getProperty("title");
//	}
//	public String findDisplayNameReport(){
//		return (String)designHandle.getProperty("displayName");
//	}
//	
//}
