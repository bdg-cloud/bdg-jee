package fr.legrain.data;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
//import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author Le Grain SA
 * @version 1.0
 */

//@Entity
//@Table(name="TA_MODIF")
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
//		
//		@NamedNativeQuery(name = GestionModif.QNN.RENTRE_EN_MODIFICATION, /*resultClass = Person.class, */
//					query = "{call " + "RENTRE_EN_MODIFICATION" +"(?,?,?)}",    
//					hints = {    
//						@QueryHint(name = "org.hibernate.callable", value = "true")  
//					}
//		)
//		
//})
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
//@PermitAll
@Interceptors(ServerTenantInterceptor.class)
public class GestionModif extends JPABdLgrServer /*implements IGestionModif*/ {

//	public static class QNN {
//		public static final String AUTORISE_MODIF_GENERE_VERIF = "GestionModif.AUTORISE_MODIF_GENERE_VERIF";
//		public static final String AUTORISE_MODIF_GENERE = "GestionModif.AUTORISE_MODIF_GENERE";
//		public static final String ANNULE_MODIFICATION = "GestionModif.ANNULE_MODIFICATION";
//		public static final String RENTRE_EN_MODIFICATION = "GestionModif.RENTRE_EN_MODIFICATION";
//	}
//	
//	//@Resource
//   // private UserTransaction transaction ;
//	
//	@Resource(mappedName="java:/BDGPostgresDS")
//    private DataSource dataSource ;
//
//	static private PropertiesConfiguration listeGestionModif = null;
//	static Logger logger = Logger.getLogger(GestionModif.class.getName());
//	protected void ecritFileName(String value) {};
//	static protected String ipConnection = null;
//	
////	@PersistenceContext(unitName = "bdg")
//    private EntityManager entityManager;
//
//	protected String urlComplet =null;
//	protected String user =null;
//	protected String pass =null;
//	protected String driver =null;
//	//TODO supprimer l'objet base et tout ce qui s'y rapporte dès que possible (surement a la fin de la migration vers jpa)
//	//private static Database base ;
//	//private Connection cnx = null;
//
//	public GestionModif(){
//		
//	}
//	
//	@PostConstruct
//	public void init() {
//		entityManager = getEntityManager();
//		cnx = getJDBCConnectionFromEntityManagerAndHibernate();
//	}
//
//	public GestionModif(Connection cnx){
//		//this.cnx = cnx;
//		cnx = getJDBCConnectionFromEntityManagerAndHibernate();
////		this.cnx = dataSource.getConnection();
//
//	}
//	
//	public Connection connexionDefaut(){
//		return JPABdLgrServer.getCnx();
//	}
//	
//	/**
//	 * === Adapter du client lourd : IB_APPLICATION.NettoyageBase();
//	 * 
//	 * Supprime les verouillage et les enregistrements dans les tables temporaires ayant un certains nombre de jour d'anciennete.
//	 * @param base - la base sur laquelle on a une connection
//	 * @param delais - anciennete en nombre de jours des enregistrements a supprimer
//	 * @throws Exception
//	 */
//	public void nettoyageBase(int delais) throws Exception {
//		/*passage postgresSql*/
////		try {
////			if (cnx!=null){
////				CallableStatement cs;
////				//cs = base.getJdbcConnection().prepareCall("{call " + "NETTOYAGE() }");
////				cs = cnx.prepareCall("{call " + "NETTOYAGE(?) }");
////				cs.setInt(1, delais);
////				cs.execute();
////				if(!cnx.getAutoCommit())
////					cnx.commit();
////				logger.info("NettoyageBase");
////			}
////		} catch (Exception e1) {
////			logger.error("Erreur : NettoyageBase", e1);	
////		}
//	}
//
//	public GestionModif(String url,String user,String pass,String driver) {
//		this.urlComplet=url;
//		this.user=user;
//		this.pass=pass;
//		this.driver=driver;
//		if(cnx==null) {
//			connection(url,user,pass,driver);
//		}
//		try {
//			cnx.setAutoCommit(false);
//		} catch (SQLException e) {
//			logger.error("",e);
//		}
//		logger.info("connection gestion modif");
//	}
//
//	@Override
//	public boolean connection(String url,String user,String pass,String driver) {
//		try{
////			if (base==null) 
////				base = new Database();
////			base.setConnection(new com.borland.dx.sql.dataset.ConnectionDescriptor(
////					url,user,pass , false, driver));
////			base.setAutoCommit(false);
////			cnx=base.getJdbcConnection();
//			if(cnx==null)
//				//cnx = ConnectionJDBC.getConnection(url, user, pass, driver);
//				cnx = getJDBCConnectionFromEntityManagerAndHibernate(); 
//			
//			
//			return true;
//		}catch(Exception e){
//			logger.error("Erreur : getDatabase", e);
//			return false;
//		}
//	}
//	
//	private Connection getJDBCConnectionFromEntityManagerAndHibernate(){
//		Session hibernateSession = entityManager.unwrap(Session.class); // unwraps the Connection class.
//		SessionFactoryImplementor sfi = (SessionFactoryImplementor) hibernateSession.getSessionFactory();
//		ConnectionProvider cp = sfi.getConnectionProvider();
//		try {
//			//return cp.getConnection();
//			if(cnx==null)
//				return dataSource.getConnection();
//			else
//				return cnx;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public static boolean deConnection() {
//		try{
//			if(cnx!=null)
//				cnx.close();
//			//base=null;
//			return true;
//		}catch(Exception e){
//			logger.error("Erreur : getDatabase", e);
//			return false;
//		}
//	}
//
//	@Override
//	public void initIp(String ip){
//		ipConnection=ip;
//	}
//
//	@Override
//	public boolean initQuery(){
////		if(fIBQuery.getQuery()==null){
////			fIBQuery.setQuery(new QueryDescriptor(fIBBase,"select * from ta_modif", true));
////			fIBQuery.open();
////		}
//		return true;
//	}
//
//	@Override
//	public void setListeGestionModif(String fileName){
////		try {
////			if (!new File(fileName).exists()) {
//////				MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "ATTENTION",
//////						"Le fichier .properties "+ fileName + " est inexistant");
////				throw new Exception("Le fichier .properties "+ fileName + " est inexistant");
////			} else {
////				FileInputStream file = new FileInputStream(fileName);
////				listeGestionModif=new PropertiesConfiguration();
////				listeGestionModif.load(file);
////				file.close();
////			}
////		}
////		catch (Exception e) {
////			logger.error("Erreur : setListeGestionModif", e);
////		}
//	}
//
//	@Override
//	public String recupChampGenere(String nomTable)throws Exception{
////		org.apache.commons.configuration.SubsetConfiguration propertie = null;
////		propertie = new org.apache.commons.configuration.SubsetConfiguration(listeGestionModif,nomTable,".");
////		String res ="";
////		if (!propertie.isEmpty()){
////			List<Object> liste = listeGestionModif.getList(nomTable+".GENERE");
////			if (liste!=null && !liste.isEmpty()){
////				for (Object f : liste) {
////					if (((String)f).startsWith("CODE_"))
////						res=(String)f;
////				}
////			}
////		}		
////		return res;
//		return "";
//	}
//
//	@Override
//	public String recupChampVerifModification(String nomTable)throws Exception{
////		org.apache.commons.configuration.SubsetConfiguration propertie = null;
////		propertie = new org.apache.commons.configuration.SubsetConfiguration(listeGestionModif,nomTable,".");
////		String res =null;
////		if (!propertie.isEmpty()){
////			List<Object> liste = listeGestionModif.getList(nomTable);
////			if (liste!=null && !liste.isEmpty()){
////				for (Object f : liste) {
////					if (((String)f).contains("ID_"))
////						res=(String)f;
////				}
////			}
////		}		
////		return res;
//		return null;
//	}
//
//	@Override
//	public boolean autoriseModif(String nomTable,String nomChamp,String valeurChamp) throws Exception{
//
//		boolean res = true;
//		CallableStatement cs;
//		PreparedStatement ps;
//		if (valeurChamp==null && nomChamp!=null)return res;
//		
//		ps = cnx.prepareStatement("SELECT RETOUR FROM AUTORISE_MODIF (?,?,?)");
//		//ps.registerOutParameter(1,Types.INTEGER);
//		ps.setString(1,nomTable);
//		ps.setString(2,nomChamp);
//		ps.setString(3,valeurChamp);
//		ResultSet rs = ps.executeQuery();
//
//		if(rs.next()) {
//			if(rs.getInt(1)>0)
//				res=false;
//		}
//		
//		return res;
//	}
//
//	@Override
//	public boolean autoriseModifCodeGenere(String nomTable,String nomChamp,String valeurChamp,Boolean verif_Connection) throws Exception{
//		
//		boolean res = true;
//		/*passage postgresSql*/
////		CallableStatement cs;
////		if (valeurChamp==null && nomChamp!=null)return res;
////		//cs=baseModif.createCallableStatement("{? = call " + "AUTORISE_MODIF_GENERE" +"(?,?,?)}");
////		cs = cnx.prepareCall("{? = call " + "AUTORISE_MODIF_GENERE" +"(?,?,?,?)}");
////		cs.registerOutParameter(1,Types.INTEGER);
////		cs.setString(2,nomTable);
////		cs.setString(3,nomChamp);
////		cs.setString(4,valeurChamp);	
////		cs.setInt(5,LibConversion.booleanToInt(verif_Connection));
////		cs.execute();
////		if (cs.getInt(1)>0)
////			res=false;
////		if (res){
////			//vérifier s'il n'existe pas déjà
////		}
//		return res;
//	}
//
//	@Override
//	public void annuleModif(String nomTable,String nomChamp,String valeurChamp,boolean commited) throws Exception{
//		/*passage postgresSql*/
////		try{
////			CallableStatement cs;
////			PreparedStatement ps;
////			if (valeurChamp!=null && nomChamp!=null){
////				
////				//ps = cnx.prepareStatement("EXECUTE procedure ANNULE_MODIFICATION" +"?,?,?");
////				cs = cnx.prepareCall("{call " + "ANNULE_MODIFICATION(?,?,?) }");
////				cs.setString(1,nomTable);
////				cs.setString(2,nomChamp);
////				cs.setString(3,valeurChamp);
////				cs.execute();
////    
////				logger.info("commit - annuleModif => "+nomTable+" / "+nomChamp+" / "+valeurChamp);
////			}
////		}catch(Exception e){
////			logger.debug("",e);
////			throw new Exception(e.getMessage());
////		}
//	}
//
//	@Override
//	public void annuleModif(String nomTable,String nomChamp,Integer valeurChamp,boolean commited) throws Exception{
//		if(valeurChamp!=null)
//			annuleModif(nomTable, nomChamp, valeurChamp.toString(), commited);
//	}
//
//	@Override
//	public void rentreEnModif(String nomTable,String nomChamp,String valeurChamp) throws Exception{
//		/*passage postgresSql*/
////		try{
////			CallableStatement cs;
////			PreparedStatement ps;
////			if (valeurChamp!=null && nomChamp!=null){
////				
////				Statement s = cnx.createStatement();
////				
////				ps = cnx.prepareStatement("EXECUTE procedure RENTRE_EN_MODIFICATION " +"?,?,?");
////				ps.setString(1,nomTable);
////				ps.setString(2,nomChamp);
////				ps.setString(3,valeurChamp);
////				ps.execute();
////
////				if(!cnx.getAutoCommit())
////					cnx.commit();
////   
////				logger.info("commit - rentreEnModif => "+nomTable+" / "+nomChamp+" / "+valeurChamp);
////			}
////		}catch(Exception e){
////			logger.debug("",e);
////			throw new Exception(e.getMessage());
////		}
//	}
//
//	@Override
//	public void rentreEnModif(String nomTable,String nomChamp,Integer valeurChamp) throws Exception{
//		if(valeurChamp!=null)
//			rentreEnModif(nomTable, nomChamp, valeurChamp.toString());
//	}
//
//	public static PropertiesConfiguration getListeGestionModif() {
//		return listeGestionModif;
//	}
//
////	@Override
////	public void setCnx(Connection cnx) {
////		JPABdLgrServer.cnx = cnx;
////	}
//
//	public static String getIpConnection() {
//		return ipConnection;
//	}

}
