package fr.legrain.general.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
//import org.apache.tools.ant.DefaultLogger;
//import org.apache.tools.ant.Project;
//import org.apache.tools.ant.ProjectHelper;


import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.dao.IDatabaseDAO;
import fr.legrain.general.service.local.IDatabaseServiceLocal;
import fr.legrain.gwihostingfr.api.ws.client.GwiWebServiceClientCXF;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibExecLinux;
import fr.legrain.moncompte.ws.client.GandiAPIWebServiceClientCXF;


/**
 * Session Bean implementation class AnnuaireBean
 */
@Stateless
@WebService
@HandlerChain(file="/fr/legrain/general/service/handler-chain.xml")
@Interceptors(ServerTenantInterceptor.class)
public class DatabaseService implements IDatabaseServiceLocal {

	static Logger logger = Logger.getLogger(DatabaseService.class);

	//@EJB IUserServiceLocal userService;
	
	//@PersistenceContext(unitName = "bdg")
	//private EntityManager entityManager;

	@Inject private IDatabaseDAO dao;
	
	private BdgProperties p = new BdgProperties();

	/**
	 * Default constructor. 
	 */
	public DatabaseService() {

	}
	
	@WebMethod(operationName="backupDBDefaultAll")
	public File backupDB(String tagDump) throws IOException {
		return backupDB(tagDump,null);
	}
	
	@WebMethod(operationName="backupDBSchema")
	public File backupDB(String tagDump, String schema) throws IOException {
		BdgProperties p = new BdgProperties();
		return backupDB(p.getProperty(BdgProperties.KEY_BDG_FILESYSTEM_PATH)+"/bdg/system/db_dumps","bdg", tagDump, schema);
	}
	
	@WebMethod(operationName="backupDBSchemaBase")
	public File backupDB(String tagDump, String schema, String dbName) throws IOException {
		BdgProperties p = new BdgProperties();
		return backupDB(p.getProperty(BdgProperties.KEY_BDG_FILESYSTEM_PATH)+"/bdg/system/db_dumps",dbName, tagDump, schema);
	}

	@WebMethod(operationName="backupDB")
	public File backupDB(String cheminDump, String dbName, String tagDump, String schema) throws IOException {

		File file = null;
		String dbUserName = null;
		String dbPassword = null; 
		String nomFinalDump = null;

		BdgProperties p = new BdgProperties();
		try {

			dbUserName = p.getProperty(BdgProperties.KEY_JDBC_USER_NAME);
			dbPassword = p.getProperty(BdgProperties.KEY_JDBC_PASSWORD);
				
			if(tagDump!=null && !tagDump.equals("")) {
				tagDump = tagDump.trim();
				tagDump += "_";
			} else {
				tagDump = "";
			}

			File f = new File(cheminDump);
			f.mkdirs();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String dateSysteme = formatter.format(new Date());
			
			String executeCmd1 = null;
			if(schema==null) {
				nomFinalDump = tagDump+dbName+"_alldb_"+dateSysteme+".backup";
				executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -f "+cheminDump+"/"+nomFinalDump+" "+ dbName;
//				String executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -v -f "+cheminDump+"/"+tagDump+dbName1+"_$(date +%Y-%m-%d_%H-%M-%S).backup "+ dbName1;
			} else {
				nomFinalDump = tagDump+dbName+"_schema_"+schema+"_"+dateSysteme+".backup";
				executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -n "+schema+" -f "+cheminDump+"/"+nomFinalDump+" "+ dbName;
			}
			
			
			Process runtimeProcess1;

			ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			Map<String, String> env = pb.environment();
			env.put("PGUSER", dbUserName);
			env.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb.start();

			int processComplete1 = runtimeProcess1.waitFor();

			//nettoyage
			if (processComplete1 == 0) {
				System.out.println("Backup created successfully");
				runtimeProcess1.destroy();
				return new File(cheminDump+"/"+nomFinalDump);
			} else {
				System.out.println("Could not create the backup");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}


		//return backupDBMySQL(nomDump);
		return new File(cheminDump+"/"+nomFinalDump);
	}
	
	@WebMethod(operationName="restaureDBDump")
	public void restaureDB(String fichierDump) throws IOException {
		restaureDB(fichierDump,null);
	}

	@WebMethod(operationName="restaureDBDumpSchema")
	public void restaureDB(String fichierDump, String schema) throws IOException {

		File file = null;
		String dbUserName = null; 
		String dbPassword = null; 
		String cheminDump = "/tmp";

		String dbName1 = "bdg";

		String propertiesFileName = "bdg.properties";  

		try {

			Properties props = new Properties();  
			String path = null;
			if(path==null) {
				if(System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(BdgProperties.KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(BdgProperties.KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}

			if(new File(path).exists()) { 
				File f = new File(path);
				props.load(new FileInputStream(f));  
				dbUserName = props.getProperty("jdbc.username");
				dbPassword = props.getProperty("jdbc.password");
			}

			//file = new File("BKUP_DB_" +Calendar.getInstance().toString().replace("/", "-") + ".sql");

			String tagDump = "";
			if(fichierDump!=null && !fichierDump.equals("")) {
				tagDump = fichierDump;
				tagDump = tagDump.trim();
				//tagDump = tagDump.replace(oldChar, newChar);
				tagDump +="_";
			}

			String executeCmd1 = null;
			String executeCmd2 = null;
			String executeCmd3 = null;
			if(schema==null) {
				//executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -f "+cheminDump+"/"+tagDump+dbName1+"_alldb_$(date +%Y-%m-%d_%H-%M-%S).backup "+ dbName1;
//				String executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -v -f "+cheminDump+"/"+tagDump+dbName1+"_$(date +%Y-%m-%d_%H-%M-%S).backup "+ dbName1;
				
				/*
				dropdb login_db
				createdb -O bdg -E UTF8 login_db
				pg_restore -i -h localhost -p 5432 -U $PGUSER -d bdg -v $BDG_FILESYSTEM_PATH/bdg/update/backup_bdg.backup
				*/
				executeCmd1 = "dropdb -h localhost -p 5432 -U "+dbUserName+" "+ dbName1;
				executeCmd2 = "createdb -h localhost -p 5432 -U "+dbUserName+" -E UTF8 -O "+dbUserName+" "+ dbName1;
				executeCmd3 = "pg_restore -h localhost -p 5432 -U "+dbUserName+" -d "+ dbName1+" -v "+fichierDump;
			} else {
				//executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -n "+schema+" -f "+cheminDump+"/"+tagDump+dbName1+"_schema_"+schema+"_$(date +%Y-%m-%d_%H-%M-%S).backup "+ dbName1;
				
				/*
				psql -U $PGUSER -h localhost -d bdg -c "DROP SCHEMA IF EXISTS public CASCADE"
				psql -U $PGUSER -h localhost -d bdg -c "create schema public"
				pg_restore -i -h localhost -p 5432 -U $PGUSER -d bdg -n public -v $BDG_FILESYSTEM_PATH/bdg/update/backup_bdg.backup
				*/
				executeCmd1 = "psql -h localhost -p 5432 -U "+dbUserName+" -d "+ dbName1 +" -c \"DROP SCHEMA IF EXISTS "+schema+" CASCADE\"";
				executeCmd2 = "psql -h localhost -p 5432 -U "+dbUserName+" -d "+ dbName1 +" -c \"CREATE SCHEMA "+schema+"\"";
				executeCmd3 = "pg_restore -h localhost -p 5432 -U "+dbUserName+" -d "+ dbName1+" -n "+schema+" -v "+fichierDump;
			}
			
			Process runtimeProcess1;
			Process runtimeProcess2;
			Process runtimeProcess3;

			//base 1
			//			String[] arg1 = new String[] {
			//					"/bin/bash","-c",executeCmd1
			//			};
			//			String[] envVar1 = new String[] {
			//					"PGUSER="+dbUserName
			//					,"PGPASSWORD="+dbPassword
			//			};
			//			runtimeProcess1 = Runtime.getRuntime().exec(arg1,envVar1);

			ProcessBuilder pb1 = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			ProcessBuilder pb2 = new ProcessBuilder("/bin/bash","-c",executeCmd2);
			ProcessBuilder pb3 = new ProcessBuilder("/bin/bash","-c",executeCmd3);
			
			pb1.redirectOutput(Redirect.INHERIT);
			pb1.redirectError(Redirect.INHERIT);
			pb1.redirectInput(Redirect.INHERIT);
			
			pb2.redirectOutput(Redirect.INHERIT);
			pb2.redirectError(Redirect.INHERIT);
			pb2.redirectInput(Redirect.INHERIT);
			
			pb3.redirectOutput(Redirect.INHERIT);
			pb3.redirectError(Redirect.INHERIT);
			pb3.redirectInput(Redirect.INHERIT);
			
			Map<String, String> env1 = pb1.environment();
			env1.put("PGUSER", dbUserName);
			env1.put("PGPASSWORD", dbPassword);
			
			Map<String, String> env2 = pb2.environment();
			env2.put("PGUSER", dbUserName);
			env2.put("PGPASSWORD", dbPassword);
			
			Map<String, String> env3 = pb3.environment();
			env3.put("PGUSER", dbUserName);
			env3.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			//			pb.redirectOutput(Redirect.INHERIT);
			//			pb.redirectError(Redirect.INHERIT);
			//			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb1.start();
			int processComplete1 = runtimeProcess1.waitFor();
			
			runtimeProcess2 = pb2.start();
			int processComplete2 = runtimeProcess2.waitFor();
			
			runtimeProcess3 = pb3.start();
			int processComplete3 = runtimeProcess3.waitFor();
 
			//nettoyage
			if (processComplete1 == 0 && processComplete2 == 0 && processComplete3 == 0) {
				System.out.println("Dump restauré avec succés");
				runtimeProcess1.destroy();
				runtimeProcess2.destroy();
				runtimeProcess3.destroy();
//				return file;
			} else {
				System.out.println("Impossible de restauré le dump");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
	
	public void renameSchema(String dbName, String nomSchema, String nouveauNomSchema) throws IOException {

		File file = null;
		String dbUserName = null; 
		String dbPassword = null; 
		String nomFinalDump = null;
		
		if(dbName==null) {
			dbName = "bdg";
		}

		BdgProperties p = new BdgProperties();
		try {

			dbUserName = p.getProperty(BdgProperties.KEY_JDBC_USER_NAME);
			dbPassword = p.getProperty(BdgProperties.KEY_JDBC_PASSWORD);
				
			
			
			String executeCmd1 = null;

			executeCmd1 = "psql -h localhost -p 5432 -U "+dbUserName+" -d "+dbName+" -c 'alter schema "+nomSchema+" rename to "+nouveauNomSchema+"'";
			
			Process runtimeProcess1;

			ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			Map<String, String> env = pb.environment();
			env.put("PGUSER", dbUserName);
			env.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb.start();

			int processComplete1 = runtimeProcess1.waitFor();

			//nettoyage
			if (processComplete1 == 0) {
				System.out.println("Schema renommé avec succés");
				runtimeProcess1.destroy();
			} else {
				System.out.println("Impossible de renommer le schéma");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}
	
	public File majBdd(String fichierSQL) throws IOException {

		File fileLogMaj = null;
	
		String dbUserName = null; 
		String dbPassword = null; 

		BdgProperties p = new BdgProperties();
		try {

			dbUserName = p.getProperty(BdgProperties.KEY_JDBC_USER_NAME);
			dbPassword = p.getProperty(BdgProperties.KEY_JDBC_PASSWORD);

			String executeCmd1 = null;
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String dateSysteme = formatter.format(new Date());
			
			String fichierSQLArchive = "/var/lgr/bdg/update_db/sql/maj_"+dateSysteme+".sql";
			String fichierLog = "/var/lgr/bdg/update_db/log/"+dateSysteme+"_log.txt";
			
			new File("/var/lgr/bdg/update_db/sql").mkdirs();
			new File("/var/lgr/bdg/update_db/log").mkdirs();
			fileLogMaj = new File(fichierLog);
			Files.copy(Paths.get(fichierSQL), Paths.get(fichierSQLArchive));
			
			// /var/lgr/bdg/bin/maj_db_all_dossier.sh bdg ###_PASSWORD_PG_BDG_### "+fichierSQL+" > date+log.txt 2>&1 
			executeCmd1 = "/var/lgr/bdg/bin/maj_db_all_dossier.sh "+dbUserName+" "+dbPassword+" "+fichierSQLArchive;
			
			Process runtimeProcess1;

			ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			Map<String, String> env = pb.environment();
//			env.put("PGUSER", dbUserName);
//			env.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			pb.redirectOutput(fileLogMaj);
			pb.redirectError(fileLogMaj);
			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb.start();

			int processComplete1 = runtimeProcess1.waitFor();

			//nettoyage
			if (processComplete1 == 0) {
				System.out.println("Schema renommé avec succés");
				runtimeProcess1.destroy();
				return fileLogMaj;
			} else {
				System.out.println("Impossible de renommer le schéma");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return fileLogMaj;

	}
	
	@Resource
	private WebServiceContext ctx;


	
	public List<String> listeBdd() {
	     MessageContext mc = ctx.getMessageContext();
	     HttpSession session =    ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
	     if (session == null) {
	         throw new WebServiceException("No HTTP Session found");
	     } else {
	    	 TenantInfo t = new TenantInfo();
	    	 t.setTenantId("public");
	    	 session.putValue("tenantInfo", t);
	     }
		return dao.listeBdd();
//		try {
//			List<String> l = new ArrayList<String>();
//			Query query = entityManager.createNativeQuery("SELECT datname FROM pg_database WHERE datistemplate = false");
//			List<Object> r = query.getResultList();
//			for (Object object : r) {
//				l.add(object.toString());
//				//				System.out.println(object);
//			}
//			return l;
//		} catch (RuntimeException re) {
//			throw re;
//		}
	}

	public List<Object[]> listeSchemaTailleConnection() {
		return dao.listeSchemaTailleConnection();
//		try {
//			Query query = entityManager.createNativeQuery(
//					"SELECT table_schema,"
//							+ " pg_size_pretty(SUM(pg_relation_size(table_schema || '.' || table_name))) As Taille_donnees,"
//							+ " pg_size_pretty(SUM(pg_total_relation_size(table_schema || '.' || table_name))) As Taille_totale"
//							+ " FROM information_schema.tables"
//							+ " where table_schema<>'public' and table_schema<>'information_schema' and table_schema not like 'pg_%'"
//							+ " GROUP BY table_schema"
//							+ " ORDER BY Taille_totale DESC");
//			
//			List<Object[]> r = query.getResultList();
//			return r;
//		} catch (RuntimeException re) {
//			throw re;
//		}
	}
	
	@WebMethod(operationName="ajoutDossierSimple")
	public void ajoutDossier(String nomDossier) {
		ajoutDossier(nomDossier, null, null);
	}

	@WebMethod(operationName="ajoutDossier")
	public void ajoutDossier(String nomDossier, String login, String password) {
		String dbUserName = null; 
		String dbPassword = null; 
		String cheminDump = "/tmp";

		String dbName1 = "demo";
		
		nomDossier = nomDossier.toLowerCase();

		///////////////////
		String propertiesFileName = "bdg.properties";  

		try {

			Properties props = new Properties();  
			String path = null;
			if(path==null) {
				if(System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(BdgProperties.KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(BdgProperties.KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}

			String prefixeSousDomaine = null;
			String nomDomaine = null;
			String jbossHome = null;
			String ip = null;
			String loginlgr = null;
			String pwdlgr = null;
			if(new File(path).exists()) { 
				File f = new File(path);
				props.load(new FileInputStream(f));  
				prefixeSousDomaine = props.getProperty("prefixe_sous_domaine");
				nomDomaine = props.getProperty("nom_domaine");
				jbossHome = System.getProperty("jboss.home.dir");
				dbUserName = props.getProperty("jdbc.username");
				dbPassword = props.getProperty("jdbc.password");
				ip = props.getProperty("ip");
				loginlgr = props.getProperty(BdgProperties.KEY_LOGIN_LGR);
				pwdlgr = props.getProperty(BdgProperties.KEY_PASSWORD_LGR);
			}
			if(prefixeSousDomaine==null) { prefixeSousDomaine = "";}
			///////////////

			File f = new File("/tmp/tmp_ajout_dossier.sh");
			Files.copy(this.getClass().getResourceAsStream("ajout_dossier.sh"), f.toPath(),StandardCopyOption.REPLACE_EXISTING);
			f.setExecutable(true);

			File f2 = new File("/tmp/une_ligne_jboss.rb");
			Files.copy(this.getClass().getResourceAsStream("une_ligne_jboss.rb"), f2.toPath(),StandardCopyOption.REPLACE_EXISTING);
			f2.setExecutable(true);
			
			String nomDossierPosgres = nomDossier.replaceAll("-", "_"); 

			//String executeCmd1 = "/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.bdg.ear/divers/ajout_dossier.sh "+nomDossier+" "+jbossHome+" "+nomDomaine+" "+prefixeSousDomaine;
			//String executeCmd1 = "/tmp/tmp_ajout_dossier.sh "+nomDossier+" "+nomDossierPosgres+" "+jbossHome+" "+nomDomaine+" '"+prefixeSousDomaine+"' "+dbUserName+" "+dbPassword+"' "+loginlgr+" "+pwdlgr;
			String executeCmd1 = "/tmp/tmp_ajout_dossier.sh "+nomDossier+" "+nomDossierPosgres+" "+jbossHome+" "+nomDomaine+" '"+prefixeSousDomaine+"' "+dbUserName+" "+dbPassword+" "+loginlgr+" "+pwdlgr;
			//		String executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -v -f "+cheminDump+"/"+tagDump+dbName1+"_$(date +%Y-%m-%d_%H-%M-%S).backup "+ dbName1;
			
			if(login!=null && password!=null) {
				executeCmd1 += " "+login+" "+password;
			} 

			Process runtimeProcess1;

			System.out.println("nomDossierPosgres : "+nomDossierPosgres);
			System.out.println("Execution de la commande : /bin/bash -c "+executeCmd1);
			
			ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			Map<String, String> env = pb.environment();
			env.put("PGUSER", dbUserName);
			env.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb.start();

			int processComplete1 = runtimeProcess1.waitFor();

			//nettoyage
			if (processComplete1 == 0) {
				System.out.println("Dossier created successfully");
				runtimeProcess1.destroy();
			} else {
				System.out.println("Could not create the Dossier");
			}
			
			//Appel du webservice utilisant les API de Gandi pour créer le nouveau sous domaine dans les DNS du domaine
//			if(ip!=null && !ip.equals("") && !ip.equals("127.0.0.1")) { //on est à prioris bien sur internet et pas sur un poste de développement
////				GwiWebServiceClientCXF ws = new GwiWebServiceClientCXF();
////				System.out.println("Création du sous domaine dans les DNS ...");
////				if(ws.creerSousDomaine(nomDomaine, nomDossier, ip)) {
////					System.out.println("Le sous domaine a bien été créé dans les DNS");
////				} else {
////					System.out.println("Erreur pendant l'enregistrement du sous domaine dans les DNS");
////				}
//				
//				GandiAPIWebServiceClientCXF ws = new GandiAPIWebServiceClientCXF();
//				System.out.println("Création du sous domaine dans les DNS ...");
//				if(ws.registerDNSSubDomain(nomDomaine, nomDossier, ip)) {
//					System.out.println("Le sous domaine a bien été créé dans les DNS");
//				} else {
//					System.out.println("Erreur pendant l'enregistrement du sous domaine dans les DNS");
//				}
//			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

	public void supprimerDossier(String nomDossier) {
		String dbUserName = null; 
		String dbPassword = null; 

		String propertiesFileName = "bdg.properties";  

		try {

			Properties props = new Properties();  
			String path = null;
			if(path==null) {
				if(System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(BdgProperties.KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(BdgProperties.KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}

			String prefixeSousDomaine = null;
			String nomDomaine = null;
			String jbossHome = null;
			String ip = null;
			if(new File(path).exists()) { 
				File f = new File(path);
				props.load(new FileInputStream(f));  
				prefixeSousDomaine = props.getProperty("prefixe_sous_domaine");
				nomDomaine = props.getProperty("nom_domaine");
				jbossHome = System.getProperty("jboss.home.dir");
				dbUserName = props.getProperty("jdbc.username");
				dbPassword = props.getProperty("jdbc.password");
				ip = props.getProperty("ip");
			}
			if(prefixeSousDomaine==null) { prefixeSousDomaine = "";}
			///////////////

			File f = new File("/tmp/tmp_supprimer_dossier.sh");
			Files.copy(this.getClass().getResourceAsStream("supprimer_dossier.sh"), f.toPath(),StandardCopyOption.REPLACE_EXISTING);
			f.setExecutable(true);
			
			File f2 = new File("/tmp/une_ligne_jboss.rb");
			Files.copy(this.getClass().getResourceAsStream("une_ligne_jboss.rb"), f2.toPath(),StandardCopyOption.REPLACE_EXISTING);
			f2.setExecutable(true);
			
			String nomDossierPosgres = nomDossier.replaceAll("-", "_"); 

			//String executeCmd1 = "/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.bdg.ear/divers/supprimer_dossier.sh "+nomDossier+" "+jbossHome+" "+nomDomaine+" "+prefixeSousDomaine+" "+dbUserName+" "+dbPassword;;
			String executeCmd1 = "/tmp/tmp_supprimer_dossier.sh "+nomDossier+" "+nomDossierPosgres+" "+jbossHome+" "+nomDomaine+" '"+prefixeSousDomaine+"' "+dbUserName+" "+dbPassword;;
			//			String executeCmd1 = "pg_dump -h localhost -p 5432 -U "+dbUserName+" -F c -b -v -f "+cheminDump+"/"+tagDump+dbName1+"_$(date +%Y-%m-%d_%H-%M-%S).backup "+ dbName1;

			Process runtimeProcess1;

			ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			Map<String, String> env = pb.environment();
			env.put("PGUSER", dbUserName);
			env.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb.start();

			int processComplete1 = runtimeProcess1.waitFor();

			//nettoyage
			if (processComplete1 == 0) {
				System.out.println("Dossier suppr successfully");
				runtimeProcess1.destroy();
			} else {
				System.out.println("Could not suppr the Dossier");
			}
			
			//Appel du webservice utilisant les API de Gandi pour supprimer le nouveau sous domaine dans les DNS du domaine
//			if(ip!=null && !ip.equals("") && !ip.equals("127.0.0.1")) { //on est à prioris bien sur internet et pas sur un poste de développement
////				GwiWebServiceClientCXF ws = new GwiWebServiceClientCXF();
////				System.out.println("Création du sous domaine dans les DNS ...");
////				if(ws.supprimerSousDomaine(nomDomaine, nomDossier)) {
////					System.out.println("Le sous domaine a bien été créé dans les DNS");
////				} else {
////					System.out.println("Erreur pendant l'enregistrement du sous domaine dans les DNS");
////				}
//				
//				GandiAPIWebServiceClientCXF ws = new GandiAPIWebServiceClientCXF();
//				System.out.println("Création du sous domaine dans les DNS ...");
//				if(ws.deleteDNSSubdomain(nomDomaine, nomDossier)) {
//					System.out.println("Le sous domaine a bien été créé dans les DNS");
//				} else {
//					System.out.println("Erreur pendant l'enregistrement du sous domaine dans les DNS");
//				}
//			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}
	
	public void rebootServeur() {


		String propertiesFileName = "bdg.properties";  

		try {

			Properties props = new Properties();  
			String path = null;
			if(path==null) {
				if(System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(BdgProperties.KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(BdgProperties.KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}


			String jbossHome = null;
			String ip = null;
			if(new File(path).exists()) { 
				File f = new File(path);
				props.load(new FileInputStream(f));  
				jbossHome = System.getProperty("jboss.home.dir");
				ip = props.getProperty("ip");
			}

//			File f = new File("/var/lgr/bdg/bin/reboot_jboss.sh");
//			Files.copy(this.getClass().getResourceAsStream("ajout_dossier.sh"), f.toPath(),StandardCopyOption.REPLACE_EXISTING);
//			f.setExecutable(true);

			String executeCmd1 = p.getProperty(BdgProperties.KEY_BDG_FILESYSTEM_PATH)+"/bdg/bin/reboot_jboss.sh";


			Process runtimeProcess1;

			System.out.println("Execution de la commande : /bin/bash -c "+executeCmd1);
			
			ProcessBuilder pb = new ProcessBuilder("/bin/bash","-c",executeCmd1);
			Map<String, String> env = pb.environment();
//			env.put("PGUSER", dbUserName);
//			env.put("PGPASSWORD", dbPassword);

			//Si "-v" dans les options du dump (verbose), il faut rediriger les sorties du processus, sinon le pocessus dépasse le tampon alloué pour les messages en sortie et se met en attente
			//http://www.developpez.net/forums/d1451841/java/general-java/sauvegarde-d-base-donnees-postgres-java/
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.redirectInput(Redirect.INHERIT);

			runtimeProcess1 = pb.start();

			int processComplete1 = runtimeProcess1.waitFor();

			//nettoyage
			if (processComplete1 == 0) {
				System.out.println("Dossier created successfully");
				runtimeProcess1.destroy();
			} else {
				System.out.println("Could not create the Dossier");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}
	
	public List<String> listeFichierDump(String nomDossier, String nomBase) {
		
		
		
		final String FILE_DIR = p.getProperty(BdgProperties.KEY_BDG_FILESYSTEM_PATH)+"/bdg/system/db_dumps";
		final String FILE_TEXT_EXT = ".backup";
		
		List<String> l = new ArrayList<>();
		
		GenericExtFilter filter = new GenericExtFilter(FILE_TEXT_EXT);

		File dir = new File(FILE_DIR);
		
		if(dir.isDirectory()==false){
			System.out.println("Directory does not exists : " + FILE_DIR);
			return l;
		}
		
		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);

		boolean ajouter = false;
		for (String file : list) {
			ajouter = false;
			if(nomDossier==null) {
				if(file.contains("alldb") && !file.contains("schema") && file.startsWith(nomBase)) {
					ajouter = true;
				}
			} else {
				if(file.contains("schema_"+nomDossier+"_")) {
					ajouter = true;
				}
			}
			if(ajouter) {
				String temp = new StringBuffer(FILE_DIR).append(File.separator).append(file).toString();
				l.add(temp);
				System.out.println("file : " + temp);
			}
		}
		return l;
	}
	
	public class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
	
	
	
	
	
	
	

	/*Backup MySQL*/
	public File backupDBMySQL(String nomDump) throws IOException {
		/*
#!/bin/bash
USER=admin
HOST=localhost
PASSWORD=xxxxxxxx
DEST=/root/save_mysql/dump/
for i in $(mysql --user=$USER --password=$PASSWORD --host=$HOST --batch --skip-column-names -e "show databases"| sed  's/ /%/g'); do
       /usr/bin/mysqldump --user=$USER --password=$PASSWORD $i > $DEST$i.sql
done
		 */

		File file = null;
		String dbUserName = "user";
		String dbPassword = "pass";
		String cheminDump = "/var/careco/database_dump";

		String dbName1 = "careco";
		String dbName2 = "careco_dump_ProjetAAA";

		file = new File("BKUP_DB_" +Calendar.getInstance().toString().replace("/", "-") + ".sql");

		String tagDump = "";
		if(nomDump!=null && !nomDump.equals("")) {
			tagDump = nomDump;
			tagDump = tagDump.trim();
			//tagDump = tagDump.replace(oldChar, newChar);
			tagDump +="_";
		}

		//String executeCmd = "mysqldump -u " +dbUserName+ " -p" +dbPassword+" "+"careco" + " > " +file.getPath();
		String executeCmd1 = "mysqldump -u " +dbUserName+ " -p" +dbPassword+" "+dbName1+ " > " +cheminDump+"/"+tagDump+dbName1+"_$(date +%Y-%m-%d_%H-%M-%S).sql";
		String executeCmd2 = "mysqldump -u " +dbUserName+ " -p" +dbPassword+" "+dbName2+ " > " +cheminDump+"/"+tagDump+dbName2+"_$(date +%Y-%m-%d_%H-%M-%S).sql";

		Process runtimeProcess1;
		Process runtimeProcess2;
		try {
			//base 1
			String[] arg1 = new String[] {
					"/bin/bash","-c",executeCmd1
			};
			runtimeProcess1 = Runtime.getRuntime().exec(arg1);
			int processComplete1 = runtimeProcess1.waitFor();

			//base 2
			String[] arg2 = new String[] {
					"/bin/bash","-c",executeCmd2
			};
			runtimeProcess2 = Runtime.getRuntime().exec(arg2);
			int processComplete2 = runtimeProcess2.waitFor();

			//nettoyage
			if (processComplete1 == 0 && processComplete2 == 0) {
				System.out.println("Backup created successfully");
				runtimeProcess1.destroy();
				runtimeProcess2.destroy();
				return file;
			} else {

				System.out.println("Could not create the backup");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}

		return file;
	}
}

	
//	public Boolean majDatabase(final String fichierPath,final String dbPath) {
//		Boolean retour=false;
////		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()!=null) {
////			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
////		}
//		
//		majDatabaseForce(fichierPath,dbPath,false);
//		TaVersion v = daoVersion.findInstance();
////		try {
////			IB_APPLICATION.NettoyageBase();
////		} catch (Exception e) {			
////		}
////		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()!=null) {
////			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
////		}
//		
//		return retour;
//	}	
//	
//
//	
//	/**
//	 * MAJ de la base de données
//	 * @param dbPath - emplacement ou sera créé la bdd - contient les scripts de création (.bat ou .sh) et le fichier ant
//	 * @throws InterruptedException 
//	 */
//	public Boolean majDatabaseForce(final String fichierPath,final String dbPath,boolean force) {
//		Boolean retour=false;
//		String directoryPath = "/var/bdg/sys";
//		String C_FICHIER_ANT_MAJ_DB = "build.xml";
//
//		try {
////			new JPABdLgr().fermetureConnection();
////			IB_APPLICATION.fermetureConnectionBdd();
//			TaVersion v =null;
//			//des modifications    /// !LgrMess.isDOSSIER_EN_RESEAU()&& 
////			if (! IB_APPLICATION.verifMultiConnection())
////			{
//				//if(!(LgrMess.isDEVELOPPEMENT()&& !LgrMess.isTOUJOURS_MAJ())){					
//				v=daoVersion.findInstance();//cette ligne permet de remonter le fichier de properties
////				FireBirdManager fireBirdManager = new FireBirdManager();
////				AntRunner antRunner = new AntRunner();
//				String numVersion;
//				String oldVersion;
//				boolean SauvOk=true;
//				//VERSION v = VERSION.getVERSION("1",null);
//				oldVersion = v.getOldVersion();
//				numVersion = v.getNumVersion();
//				logger.info("OldVersion = "+oldVersion+" - NumVersion = "+numVersion);
////				if(LgrMess.isTOUJOURS_MAJ()==true){
////					InputDialog input = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
////							,"Numéro de version","Indiquer le numéro de version à partir duquel la mise à jour doit être faite.",oldVersion,
////							new IInputValidator(){
////								String[] listeVersionsOk =new String[]{"2.0.6","2.0.8","2.0.9","2.0.10","2.0.11","2.0.12"};
////								String[] listeVersionsMaintenance =new String[]{};
////								public String isValid(String newText) {
////									for (int i = 0; i < listeVersionsOk.length; i++) {
////										if(listeVersionsOk[i].equalsIgnoreCase(newText))
////											return null;
////									}
////									if(LgrMess.isDEVELOPPEMENT()){
////										for (int i = 0; i < listeVersionsMaintenance.length; i++) {
////											if(listeVersionsMaintenance[i].equalsIgnoreCase(newText))
////												return null;
////										}}
////									if(!newText.equals(""))
////										return "Ce numéro n'est pas valide.";
////									else return null;
////								}
////		
////							});
////					input.open();
////					oldVersion=input.getValue();
////					if(oldVersion==null)
////						throw new Exception("abandon de la mise à jour");
////					else initDossier(null,findOpenProject().getName(),true);
////				}		
//				String fileBackup;
//
//				if(oldVersion == null)oldVersion="0";
//				if(numVersion == null)numVersion="0";
//				if (oldVersion.equals(numVersion))
//					fileBackup=C_FICHIER_DB_BACKUP+".FBK";
//				else fileBackup=C_FICHIER_DB_BACKUP+"_"+oldVersion+".FBK";
//				try {
//					logger.info("Mise à jour de la base de données");
//					if (!oldVersion.equals(numVersion)) MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//							"Mise à jour de la base vers version n° "+numVersion,"Une mise à jour de la base doit être effectuée, elle peut-être longue.");
////					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));		
//					if(logger.isDebugEnabled())
//						logger.info(directoryPath+"/"+C_FICHIER_ANT_MAJ_DB);		
//
////					if(Platform.getOS().equals(Platform.OS_WIN32)) {
//						antRunner.setBuildFileLocation(directoryPath+"/"+C_FICHIER_ANT_MAJ_DB);
////					} else if(Platform.getOS().equals(Platform.OS_LINUX)
////							|| Platform.getOS().equals(Platform.OS_MACOSX)) {
////						antRunner.setBuildFileLocation(directoryPath+"/"+C_FICHIER_ANT_MAJ_DB);
////					}
//					String serveur=Const.C_SERVEUR_BDD+"/3050:";
//					String cheminBase=Const.C_FICHIER_BDD.replace(serveur, "");
//					if(!serveur.toLowerCase().contains("localhost"))
//						antRunner.setArguments(new String[]{"-Ddb="+cheminBase,"-DuserLoc="+Const.C_USER,
//								"-DpassLoc="+Const.C_PASS,"-DoldVersion="+oldVersion,"-Dpath="+dbPath,"-DdebutURLLoc="+serveur});
//					else
//						antRunner.setArguments(new String[]{"-Ddb="+dbPath+"/"+C_FICHIER_DB,"-DuserLoc="+Const.C_USER,
//								"-DpassLoc="+Const.C_PASS,"-DoldVersion="+oldVersion,"-Dpath="+dbPath,"-DdebutURLLoc="+serveur});
//					try {
////						if(Platform.getOS().equals(Platform.OS_LINUX)) {
////							if(pass==null) {
////								Process p1 = Runtime.getRuntime().exec(
////										new String[] {
////												"gksu",
////												"--message","Mise à jour de la base de données : "+dbPath,
////												"chmod","u+w,g+w,o+w",dbPath,
////												dbPath
////										}
////										, null
////										, new File(dbPath)
////										);
////								p1.waitFor();
////							} else {
////								String[] cmd = {"/bin/bash","-c","echo "+pass+"| sudo -S chmod u+w,g+w,o+w "+dbPath+" "+dbPath};
////								Process p1 = Runtime.getRuntime().exec(
////										cmd
////										, null
////										, new File(dbPath)
////										);
////								p1.waitFor();
////							}
////						}
//						fireBirdManager.sauvegardeDB(dbPath,C_FICHIER_DB_UPPER,fileBackup,Const.C_USER,Const.C_PASS);
////						if(Platform.getOS().equals(Platform.OS_LINUX)) {
////							if(pass==null) {
////								Process p1 = Runtime.getRuntime().exec(
////										new String[] {
////												"gksu",
////												"chmod","u+w,g+w,o+w",dbPath+"/"+fileBackup,
////												dbPath
////										}
////										, null
////										, new File(dbPath)
////										);
////								p1.waitFor();
////							} else {
////								String[] cmd = {"/bin/bash","-c","echo "+pass+"| sudo -S chmod u+w,g+w,o+w "+dbPath+"/"+fileBackup+" "+dbPath};
////								Process p1 = Runtime.getRuntime().exec(
////										cmd
////										, null
////										, new File(dbPath)
////										);
////								p1.waitFor();
////							}
////						}
////						if(Platform.getOS().equals(Platform.OS_LINUX)) {
////							if(pass==null) {
////								Process p1 = Runtime.getRuntime().exec(
////										new String[] {
////												"gksu",
////												"chmod","u+r,g+r,o+r",dbPath+"/"+fileBackup,
////												dbPath
////										}
////										, null
////										, new File(dbPath)
////										);
////								p1.waitFor();
////							} else {
////								String[] cmd = {"/bin/bash","-c","echo "+pass+"| sudo -S chmod u+r,g+r,o+r "+dbPath+"/"+fileBackup+" "+dbPath};
////								Process p1 = Runtime.getRuntime().exec(
////										cmd
////										, null
////										, new File(dbPath)
////										);
////								p1.waitFor();
////							}
////						}
//					}catch (Exception e1) {
//						logger.info("Problème lors de la sauvegarde");
//						SauvOk=false;
//					}
//					if (!oldVersion.equals(numVersion)){
//						antRunner.setExecutionTargets(new String[]{"MAJ_"+oldVersion});//+oldVersion
//						antRunner.run();			
//					}
//					logger.info("Mise à jour de la base de données terminée");
//					if (!oldVersion.equals(numVersion)) MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//							"Mise à jour de la base","Mise à jour de la base de données terminée");
//
//					retour=true;
//				} catch (Exception e) {
//					try{
//						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
//								,"Mise à jour base de données","Erreur durant la Mise à jour de la base de données."
//										+LgrMess.C_SAUT_DE_LIGNE
//										+"De la version : "+oldVersion+"  à la version : "+numVersion
//										+LgrMess.C_CONTACTER_SERVICE_MAINTENANCE);
//						logger.error("",e);
//						if(SauvOk) {
//							//									IB_APPLICATION.SimpleDeConnectionBdd();
//							IB_APPLICATION.fermetureConnectionBdd();
//							fireBirdManager.restaurationDB(dbPath,C_FICHIER_DB_UPPER,fileBackup,Const.C_USER,Const.C_PASS);
//							logger.info("Suite à un problème, restauration de la base de données terminée");
////							if(Platform.getOS().equals(Platform.OS_LINUX)) {
////								logger.debug("Plateforme Linux => La restauration ne fonctionne pas correctement sur ce système. Pas de restauration.");
////							}
//						}
//					}catch (Exception e2){
//						logger.error("",e2);
//					}
//				}
//				//					}else logger.info("Pas de MAJ de la base");
////			}
//
//		} catch (Exception e) {
//			String mess;
//			if(e.getMessage()!=null)mess=e.getMessage();
//			else mess="Problème lors de la recherche de multi-connection";
//			logger.debug(mess,e);
//			retour= false;
//		}
//		TaVersion v = daoVersion.findInstance();
//		v.getNumVersion();//cette ligne permet de remonter les fichiers de properties
////		try {
////			IB_APPLICATION.NettoyageBase();
////		} catch (Exception e) {			
////		}
////		if(PlatformUI.getWorkbench().getActiveWorkbenchWindow()!=null) {
////			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_ARROW));
////		}
//		
//		return retour;
//	}	
//	
//	public void antRun(String file, String[] targets, String options) {
//		//		<dependency>
//		//			<groupId>org.apache.ant</groupId>
//		//			<artifactId>ant</artifactId>
//		//			<version>1.9.5</version>
//		//		</dependency>
//		//		<dependency>
//		//			<groupId>org.apache.ant</groupId>
//		//			<artifactId>ant-launcher</artifactId>
//		//			<version>1.9.5</version>
//		//		</dependency>
//
//		File buildFile = null;
//		if(file!=null) {
//			buildFile = new File(file);
//		} else {
//			buildFile = new File("db.build_MAJ.xml"/*"build.xml"*/);
//		}
//		Project p = new Project();
//		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
//		p.init();
//		ProjectHelper helper = ProjectHelper.getProjectHelper();
//		p.addReference("ant.projectHelper", helper);
//		helper.parse(p, buildFile);
//		
//		if(options!=null) {
//			//p.setProperty(name, value);
//		}
//		if(targets!=null) {
//			p.executeTarget(targets[0]);
//		} else {
//			p.executeTarget(p.getDefaultTarget());
//		}
//		
//
//		DefaultLogger consoleLogger = new DefaultLogger();
//		consoleLogger.setErrorPrintStream(System.err);
//		consoleLogger.setOutputPrintStream(System.out);
//		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
//		p.addBuildListener(consoleLogger);
//	}
