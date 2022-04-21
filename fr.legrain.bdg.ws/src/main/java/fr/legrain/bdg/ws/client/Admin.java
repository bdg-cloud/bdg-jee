package fr.legrain.bdg.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jws.WebMethod;

import fr.legrain.bdg.ws.AnyTypeArray;
import fr.legrain.bdg.ws.IOException_Exception;


public class Admin {

	private AdministrationWebServiceClientCXF ws;
	
	public Admin() {
		try {
			ws = new AdministrationWebServiceClientCXF();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void backupDB(String tagDump) throws IOException {
		try {
			ws.getDatabaseService().backupDBDefaultAll(tagDump);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void backupDB(String tagDump, String schema) throws IOException {
		try {
			ws.getDatabaseService().backupDBSchema(tagDump,schema);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void backupDB(String tagDump, String schema, String dbName) throws IOException {
		try {
			ws.getDatabaseService().backupDBSchemaBase(tagDump,dbName,schema);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void backupDB(String cheminDump, String dbName, String tagDump, String schema) throws IOException {
		try {
			ws.getDatabaseService().backupDB(cheminDump, dbName, tagDump, schema);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void restaureDB(String fichierDump) throws IOException {
		try {
			ws.getDatabaseService().restaureDBDump(fichierDump);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void restaureDB(String fichierDump, String schema) throws IOException {
		try {
			ws.getDatabaseService().restaureDBDumpSchema(fichierDump,schema);
		} catch (IOException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> listeBdd() {
		
		return ws.getDatabaseService().listeBdd();
//		return ws.listeBdd();
	}

	public List<Object[]> listeSchemaTailleConnection() {
		List<Object[]> result = new ArrayList<Object[]>();
		List<AnyTypeArray> a = ws.getDatabaseService().listeSchemaTailleConnection();
		for (AnyTypeArray anyTypeArray : a) {
			result.add(anyTypeArray.getItem().toArray());
		}
		return result;
	}

	public void ajoutDossier(String nomDossier) {
		ws.getDatabaseService().ajoutDossier(nomDossier);
	}

	public void supprimerDossier(String nomDossier) {
		ws.getDatabaseService().supprimerDossier(nomDossier);
	}
	
	public void rebootServeur() {
		ws.getDatabaseService().rebootServeur();
	}
	
	public List<String> listeFichierDump(String nomDossier, String nomBase) {
		return ws.getDatabaseService().listeFichierDump(nomDossier,nomBase);
//		return ws.listeFichierDump(nomDossier,nomBase);
	}
	
}
