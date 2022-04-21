package fr.legrain.data;

import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.QueryHint;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import fr.legrain.lib.data.ConnectionJDBC;
import fr.legrain.lib.data.LibConversion;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author Le Grain SA
 * @version 1.0
 */

@Entity
//@NamedNativeQueries(value = { 
//		@NamedNativeQuery(name = GestionModif.QNN.AUTORISE_MODIF_GENERE_VERIF, /*resultClass = Person.class, */
//					query = "{? = call " + "AUTORISE_MODIF_GENERE" +"(?,?,?,?)}",    
//					hints = {    
//						@QueryHint(name = "org.hibernate.callable", value = "true")  
//					}    
//		),
//		@NamedNativeQuery(name = GestionModif.QNN.AUTORISE_MODIF_GENERE, /*resultClass = Person.class, */
//					query = "{? = call " + "AUTORISE_MODIF_GENERE" +"(?,?,?)}",    
//					hints = {    
//						@QueryHint(name = "org.hibernate.callable", value = "true")  
//					}    
//		),
//		@NamedNativeQuery(name = GestionModif.QNN.ANNULE_MODIFICATION, /*resultClass = Person.class, */
//					query = "{call " + "ANNULE_MODIFICATION" +"(?,?,?)}",    
//					hints = {    
//						@QueryHint(name = "org.hibernate.callable", value = "true")  
//					}    
//		),
//		@NamedNativeQuery(name = GestionModif.QNN.RENTRE_EN_MODIFICATION, /*resultClass = Person.class, */
//					query = "{call " + "RENTRE_EN_MODIFICATION" +"(?,?,?)}",    
//					hints = {    
//						@QueryHint(name = "org.hibernate.callable", value = "true")  
//					}    
//		)
//})
public class GestionModif extends JPABdLgrServer {
	
	public static class QNN {
		public static final String AUTORISE_MODIF_GENERE_VERIF = "GestionModif.AUTORISE_MODIF_GENERE_VERIF";
		public static final String AUTORISE_MODIF_GENERE = "GestionModif.AUTORISE_MODIF_GENERE";
		public static final String ANNULE_MODIFICATION = "GestionModif.ANNULE_MODIFICATION";
		public static final String RENTRE_EN_MODIFICATION = "GestionModif.RENTRE_EN_MODIFICATION";
	}

	static private PropertiesConfiguration listeGestionModif = null;
	static Logger logger = Logger.getLogger(GestionModif.class.getName());
	protected void ecritFileName(String value) {};
	static protected String ipConnection = null;
	
	@Inject
    private EntityManager entityManager;

	protected String urlComplet =null;
	protected String user =null;
	protected String pass =null;
	protected String driver =null;
	//TODO supprimer l'objet base et tout ce qui s'y rapporte dès que possible (surement a la fin de la migration vers jpa)
	//private static Database base ;
	//private Connection cnx = null;

	public GestionModif(){

	}

	public GestionModif(Connection cnx){
		this.cnx = cnx;
	}

	public GestionModif(String url,String user,String pass,String driver) {
		this.urlComplet=url;
		this.user=user;
		this.pass=pass;
		this.driver=driver;
		if(cnx==null) {
			connection(url,user,pass,driver);
		}
		try {
			cnx.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("",e);
		}
		logger.info("connection gestion modif");
	}

	public boolean connection(String url,String user,String pass,String driver) {
		try{
//			if (base==null) 
//				base = new Database();
//			base.setConnection(new com.borland.dx.sql.dataset.ConnectionDescriptor(
//					url,user,pass , false, driver));
//			base.setAutoCommit(false);
//			cnx=base.getJdbcConnection();
			if(cnx==null)
				cnx = ConnectionJDBC.getConnection(url, user, pass, driver);
			return true;
		}catch(Exception e){
			logger.error("Erreur : getDatabase", e);
			return false;
		}
	}
	public static boolean deConnection() {
		try{
			if(cnx!=null)
				cnx.close();
			//base=null;
			return true;
		}catch(Exception e){
			logger.error("Erreur : getDatabase", e);
			return false;
		}
	}


	public void initIp(String ip){
		ipConnection=ip;
	}

	public boolean initQuery(){
//		if(fIBQuery.getQuery()==null){
//			fIBQuery.setQuery(new QueryDescriptor(fIBBase,"select * from ta_modif", true));
//			fIBQuery.open();
//		}
		return true;
	}

	public void setListeGestionModif(String fileName){
		try {
			if (!new File(fileName).exists()) {
//				MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "ATTENTION",
//						"Le fichier .properties "+ fileName + " est inexistant");
				throw new Exception("Le fichier .properties "+ fileName + " est inexistant");
			} else {
				FileInputStream file = new FileInputStream(fileName);
				listeGestionModif=new PropertiesConfiguration();
				listeGestionModif.load(file);
				file.close();
			}
		}
		catch (Exception e) {
			logger.error("Erreur : setListeGestionModif", e);
		}
	}


	public String recupChampGenere(String nomTable)throws Exception{
		org.apache.commons.configuration.SubsetConfiguration propertie = null;
		propertie = new org.apache.commons.configuration.SubsetConfiguration(listeGestionModif,nomTable,".");
		String res ="";
		if (!propertie.isEmpty()){
			List<Object> liste = listeGestionModif.getList(nomTable+".GENERE");
			if (liste!=null && !liste.isEmpty()){
				for (Object f : liste) {
					if (((String)f).startsWith("CODE_"))
						res=(String)f;
				}
			}
		}		
		return res;
	}

	public String recupChampVerifModification(String nomTable)throws Exception{
		org.apache.commons.configuration.SubsetConfiguration propertie = null;
		propertie = new org.apache.commons.configuration.SubsetConfiguration(listeGestionModif,nomTable,".");
		String res =null;
		if (!propertie.isEmpty()){
			List<Object> liste = listeGestionModif.getList(nomTable);
			if (liste!=null && !liste.isEmpty()){
				for (Object f : liste) {
					if (((String)f).contains("ID_"))
						res=(String)f;
				}
			}
		}		
		return res;
	}

	/**
	 * permet de savoir si un enregistrement est en cours de modification
	 * @param nomTable
	 * @param nomChamp
	 * @param valeur
	 * @return
	 * @throws Exception 
	 */
	public boolean autoriseModif(String nomTable,String nomChamp,String valeurChamp) throws Exception{
		Query query = entityManager.createNamedQuery(QNN.AUTORISE_MODIF_GENERE_VERIF);
		//query.setParameter(1, arg1);
		query.setParameter(2, nomTable);
		query.setParameter(3, nomChamp);
		query.setParameter(4, valeurChamp);
		Object o = query.getSingleResult();    
		return true;
//		boolean res = true;
//		CallableStatement cs;
//		if (valeurChamp==null && nomChamp!=null)return res;
//		//cs=baseModif.createCallableStatement("{? = call " + "AUTORISE_MODIF" +"(?,?,?)}");
//		cs = cnx.prepareCall("{? = call " + "AUTORISE_MODIF" +"(?,?,?)}");
//		cs.registerOutParameter(1,Types.INTEGER);
//		cs.setString(2,nomTable);
//		cs.setString(3,nomChamp);
//		cs.setString(4,valeurChamp);
//		cs.execute();
//		if (cs.getInt(1)>0)
//			res=false;
////		if (res)
////		logger.info("Autorise modif RESULT : "+cs.getInt(1)+" ip_acces = "+ipConnection
////		+" table : "+nomTable+" champ : "+nomChamp+" valeur : "+valeurChamp);
//		return res;
	}

	/**
	 * permet de savoir si un enregistrement est en cours de modification
	 * @param nomTable
	 * @param nomChamp
	 * @param valeurChamp
	 * @param verif_Connection
	 * @return
	 * @throws Exception
	 */
	public boolean autoriseModifCodeGenere(String nomTable,String nomChamp,String valeurChamp,Boolean verif_Connection) throws Exception{
		
		Query query = entityManager.createNamedQuery(QNN.AUTORISE_MODIF_GENERE_VERIF);
		//query.setParameter(1, arg1);
		query.setParameter(2, nomTable);
		query.setParameter(3, nomChamp);
		query.setParameter(4, valeurChamp);
		query.setParameter(5, LibConversion.booleanToInt(verif_Connection));
		Object o = query.getSingleResult();    
		return true;
//		boolean res = true;
//		CallableStatement cs;
//		if (valeurChamp==null && nomChamp!=null)return res;
//		//cs=baseModif.createCallableStatement("{? = call " + "AUTORISE_MODIF_GENERE" +"(?,?,?)}");
//		cs = cnx.prepareCall("{? = call " + "AUTORISE_MODIF_GENERE" +"(?,?,?,?)}");
//		cs.registerOutParameter(1,Types.INTEGER);
//		cs.setString(2,nomTable);
//		cs.setString(3,nomChamp);
//		cs.setString(4,valeurChamp);	
//		cs.setInt(5,LibConversion.booleanToInt(verif_Connection));
//		cs.execute();
//		if (cs.getInt(1)>0)
//			res=false;
//		if (res){//vérifier s'il n'existe pas déjà
//
//		}
//		return res;
	}
	
	public void annuleModif(String nomTable,String nomChamp,String valeurChamp,boolean commited) throws Exception{
		try{
			CallableStatement cs;
			if (valeurChamp!=null && nomChamp!=null){
				//cs=baseModif.getJdbcConnection().prepareCall("{call " + "ANNULE_MODIFICATION" +"(?,?,?)}");
//				cs = cnx.prepareCall("{call " + "ANNULE_MODIFICATION" +"(?,?,?)}");
//				cs.setString(1,nomTable);
//				cs.setString(2,nomChamp);
//				cs.setString(3,valeurChamp);
//				cs.execute();
//				//if (commited) 
//				cnx.commit();
				Query query = entityManager.createNamedQuery(QNN.ANNULE_MODIFICATION);
				query.setParameter(1, nomTable);
				query.setParameter(2, nomChamp);
				query.setParameter(3, valeurChamp);
				query.executeUpdate();    
				logger.info("commit - annuleModif");
			}
		}catch(Exception e){
			logger.debug("",e);
			throw new Exception(e.getMessage());
		}
	}
	
	public void annuleModif(String nomTable,String nomChamp,Integer valeurChamp,boolean commited) throws Exception{
		if(valeurChamp!=null)
			annuleModif(nomTable, nomChamp, valeurChamp.toString(), commited);
	}

	public void rentreEnModif(String nomTable,String nomChamp,String valeurChamp) throws Exception{
		try{
			CallableStatement cs;
			if (valeurChamp!=null && nomChamp!=null){
//				cs = cnx.prepareCall("{call " + "RENTRE_EN_MODIFICATION" +"(?,?,?)}");
//				cs.setString(1,nomTable);
//				cs.setString(2,nomChamp);
//				cs.setString(3,valeurChamp);
//
//				cs.execute();
//				cnx.commit();
				Query query = entityManager.createNamedQuery(QNN.RENTRE_EN_MODIFICATION);
				query.setParameter(1, nomTable);
				query.setParameter(2, nomChamp);
				query.setParameter(3, valeurChamp);
				query.executeUpdate();    
				logger.info("commit - rentreEnModif");
			}
		}catch(Exception e){
			logger.debug("",e);
			throw new Exception(e.getMessage());
		}
	}
	
	public void rentreEnModif(String nomTable,String nomChamp,Integer valeurChamp) throws Exception{
		if(valeurChamp!=null)
			rentreEnModif(nomTable, nomChamp, valeurChamp.toString());
	}


	public static PropertiesConfiguration getListeGestionModif() {
		return listeGestionModif;
	}

	public void setCnx(Connection cnx) {
		this.cnx = cnx;
	}

	public static String getIpConnection() {
		return ipConnection;
	}

}
