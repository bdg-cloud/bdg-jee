package fr.legrain.import_agrigest.sqlserver.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.legrain.import_agrigest.model.ParametreImport;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//import fr.legrain.agrifact.local.model.Parametre;
//import fr.legrain.agrifact.sqlserver.dao.PersonneDAO;
//import fr.legrain.agrifact.sqlserver.model.Personne;


/**
 * Created by isabelle on 07/09/18.
 * http://ucanaccess.sourceforge.net/site.html#examples
 */

public class SQLServerUtil {

//    private String driverName = "net.sourceforge.jtds.jdbc.Driver";
//    private String cnxString = "jdbc:jtds:sqlserver://192.168.1.232/AGRIFACT;instance=SQLEXPRESS";
//    private String login = "sa";
//    private String password = "ludo";
  public static ParametreImport params;
  private static String driverName = "net.ucanaccess.jdbc.UcanaccessDriver";
  
  
  private static String dossier="A00000";
  private static String cheminFichierExport="C:/LGRDOSS/BureauDeGestion/E2-Impor.txt";

//  private static String bdDossier="C:/Agrigest²/Dossiers/A00000/dbDonneesA00000.mdb";
  private static String bdDossier="C:/Agrigest²/Dossiers/T00074/dbDonneesT00074.mdb";
  
  private static String bdApplication="C:/Agrigest²/Data/dbPlantypeA.mdb";
  
  private static String cnxStringDossier = "jdbc:ucanaccess://"+bdDossier;
  private static String cnxStringApplication = "jdbc:ucanaccess://"+bdApplication;

    private static Connection connDossier;
    private static Connection connApplication;
    
//    private static String fichierParametreXml = "../fichierParametreXml.xml";
    private static String workingDir = System.getProperty("user.home");
    public static String fichierParametreXml = workingDir+"/fichierParametreImportAgrigest.xml";
    public static String fichierDocumentExporteXml = workingDir+"/fichierDocumentExporte.xml";

    public void init() {
    	init(false);
    }
    public void init(boolean forcer) {
    	//    	params = new Parametre();
    	//    	params.setCheminAccesBase(bdDossier);
    	//    	params.setCheminAccesBasePlanType(bdApplication);
    	//    	params.setCheminFichierExport("C:/LGRDOSS/EPICEA/E2-Impor.txt");
    	//    	params.setDossier("A00000");
    	//////    	ecritParametres(params);
    	File f =new File(fichierParametreXml);

    	if(f.exists() && !f.isDirectory() && !forcer) { 
    		params = litParametres();
    		if(params!=null) {
//    			bdDossier=params.getCheminAccesBase();
//    			bdApplication=params.getCheminAccesBasePlanType();
//    			cheminFichierExport=params.getCheminFichierExport();
//    			dossier=params.getDossier();
    		}
    	}
    	if(params==null) {
    		params = new ParametreImport();
    		params.setCheminAccesBase(bdDossier);
    		params.setCheminAccesBasePlanType(bdApplication);
    		params.setCheminFichierExport(cheminFichierExport);
    		params.setDossier(dossier);
//    		params.setTvaAEncaissement(true);
    	}

    	cnxStringDossier = "jdbc:ucanaccess://"+params.getCheminAccesBase();
    	cnxStringApplication = "jdbc:ucanaccess://"+params.getCheminAccesBasePlanType();

    }

    public static ParametreImport getInstance() {
    	return params;
    }
    
    public void closeConnection() throws SQLException {
        if(connDossier!=null) {
//        	connDossier.commit();
        	connDossier.close();
        	connDossier=null;
        }
        if(connApplication!=null ) {
//        	connApplication.commit();
        	connApplication.close();
        	connApplication=null;
        }
    }
    
    public Connection connection() {
    	return connection(false);
    }
    public Connection connection(boolean forcer) {
        try {
        	closeConnection();
//            Log.e("MSSQL", "Attempting to connect -- penser au firewall");
           init(forcer);
            
            Class.forName(driverName);
//            Connection conn=DriverManager.getConnection("jdbc:ucanaccess://<mdb or accdb file path>",user, password); 
            // for example: 
            if(connDossier==null || forcer)
            	connDossier=DriverManager.getConnection(cnxStringDossier);
            if(connApplication==null || forcer)
            	connApplication=DriverManager.getConnection(cnxStringApplication);
            
            Statement st= getStatement();
//            Class.forName(driverName);
//            conn = DriverManager.getConnection(cnxString, login, password);

//            Log.e("MSSQL", "Connected");
            
//            ResultSet rs= st.executeQuery("select * from Dossiers");
//           while (rs.next()) {
//            rs.getString(1);
//           }
            connDossier.setAutoCommit(false); 

        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("MSSQL", e.toString());
        }
        return connDossier;
    }

    public Statement getStatement() {
        if(connDossier==null) {
            connDossier = connection(true);
        }
        return getStatement(connDossier);
    }

    public Statement getStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement prepareStatement(String sql) {
        try {
            if(connDossier==null) {
                connDossier = connection(true);
            }
            return connDossier.prepareStatement(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    
    public Statement getStatementApplication() {
        if(connApplication==null) {
        	connApplication = connection(true);
        }
        return getStatement(connApplication);
    }

    public Statement getStatementApplication(Connection connection) {
        try {
            return connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement prepareStatementApplication(String sql) {
        try {
            if(connApplication==null) {
            	connApplication = connection(true);
            }
            return connApplication.prepareStatement(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void ecritParametres(ParametreImport params) {
		  JAXBContext context;
		try {
			context = JAXBContext.newInstance(ParametreImport.class);

		  Marshaller marshaller = context.createMarshaller();
           marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
           marshaller.marshal(params, new FileWriter(fichierParametreXml)); 
           params= litParametres();
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur d'écriture");
			alert.setHeaderText(null);
			alert.setContentText(SQLServerUtil.fichierParametreXml);
			alert.showAndWait();

		}
    }
    
    
    public ParametreImport litParametres() {
    	JAXBContext context;
    	try {
    		context = JAXBContext.newInstance(ParametreImport.class);
    		Unmarshaller unmarshaller = context.createUnmarshaller();
//    		unmarshaller.setValidating(true);
			FileReader reader = new FileReader(fichierParametreXml);
    		params = (ParametreImport) unmarshaller.unmarshal(reader);
//    	    System.out.println("Paramètres ");
//            System.out.println("Dossier   : " + params.getDossier());
//            System.out.println("CheminAccesBase   : " + params.getCheminAccesBase());
//            System.out.println("CheminAccesBasePlanType   : " + params.getCheminAccesBasePlanType());
//            System.out.println("CheminFichierExport   : " + params.getCheminFichierExport());
            

		
    		return params ;  
    	} catch (JAXBException | FileNotFoundException  e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur de lecture");
			alert.setHeaderText(null);
			alert.setContentText(SQLServerUtil.fichierParametreXml);
			alert.showAndWait();
    		e.printStackTrace();
    		return null;
    	} 
//    	return null;
    }
	public static Connection getConnDossier() {
		return connDossier;
	}
	public static Connection getConnApplication() {
		return connApplication;
	}

    
}