package fr.legrain.general.service.local;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

//@Local
@Remote
public interface IDatabaseServiceLocal{
	public File backupDB(String nomDump) throws IOException;
	public File backupDB(String nomDump,String schema) throws IOException;
	public File backupDB(String tagDump, String schema, String dbName) throws IOException;
	public File backupDB(String cheminDump, String dbName, String tagDump, String schema) throws IOException;
	
	public void restaureDB(String fichierDump) throws IOException;
	public void restaureDB(String fichierDump, String schema) throws IOException;
	
	public void renameSchema(String dbName, String nomSchema, String nouveauNomSchema) throws IOException;
	public File majBdd(String fichierSQL) throws IOException;
	
	public List<String> listeBdd();
	public List<Object[]> listeSchemaTailleConnection();
	public void ajoutDossier(String nomDossier);
	public void ajoutDossier(String nomDossier, String login, String password);
	public void supprimerDossier(String nomDossier);
	public void rebootServeur();
	
	public List<String> listeFichierDump(String nomDossier, String nomBase);
	
}
