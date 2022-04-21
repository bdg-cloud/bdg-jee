package fr.legrain.moncompte.data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.MessCtrlLgr;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.dossier.model.TaInfoEntreprise;
import fr.legrain.validator.ICtrlLgr;
import fr.legrain.validator.LgrHibernateValidated;

public abstract class AbstractApplicationDAOServer<Entity,DTO> extends AbstractLgrDAOServer<Entity,DTO> {

	static Logger logger = Logger.getLogger(AbstractApplicationDAOServer.class);

	//private static final String PERSISTENCE_UNIT_NAME = "sample";

	//protected JPACtrl_Application ctrlGeneraux = null;
	@Inject protected ICtrlLgr ctrlGeneraux = null;
	@Inject protected IGenCode code = null;

	//@EJB private ITaInfoEntrepriseServiceRemote entrepriseService;
	//	@Inject private ITaInfoEntrepriseServiceRemote entrepriseService;
	@Inject private ITaInfoEntrepriseServiceLocal entrepriseService;

	private Class<Entity> entityClass;
	private Class<DTO> dtoClass;

	public AbstractApplicationDAOServer() {
		super();

	}

	public AbstractApplicationDAOServer(Class<Entity> entityClass,Class<DTO> dtoClass) {
		super();
		init(entityClass,dtoClass);
	}



	/*
	 * Retourne une instance du type générique avec le constructeur par défaut (sans argument)
	 */
	public Entity getInstanceOfEntity(Class<Entity> aClass) {
		try {
			return aClass.newInstance();
		} catch (InstantiationException e) {
			logger.error("",e);
		} catch (IllegalAccessException e) {
			logger.error("",e);
		}
		return null;
	}  
	
	public DTO getInstanceOfDTO(Class<DTO> aClass) {
		try {
			return aClass.newInstance();
		} catch (InstantiationException e) {
			logger.error("",e);
		} catch (IllegalAccessException e) {
			logger.error("",e);
		}
		return null;
	}

	public DatabaseMetaData getDbMetaData() {
//		try {
//			return gestionModif.connexionDefaut().getMetaData();
//		} catch (SQLException e) {
//			logger.error("",e);
//		}
		return null;
	}

	public int getDbMetaDataLongeur(String table, String champ) {
		int taille = 0;
//		try {
//			ResultSet rs;
//
//			rs = getDbMetaData().getColumns(null, null, table, champ);
//			if (rs.next()) {
//				taille= rs.getInt(7);
//			}
//		} catch (SQLException e) {
//			logger.error("",e);
//		}
		return taille;
	}
	
	public int getDbMetaDataPrecision(String table, String champ) {
		int precision = 0;
//		try {
//			ResultSet rs = getDbMetaData().getColumns(null, null, table, champ);
//			if (rs.next()) {
//				precision= rs.getInt(9);
//			}
//		} catch (SQLException e) {
//			logger.error("",e);
//		}
		return precision;
	}

	public void init(Class<Entity> entityClass,Class<DTO> dtoClass) {
		initConnexion();
		//ctrlGeneraux = new JPACtrl_Application();
		//		System.err.println("CHEMIN EN DUR A CHANGER : (Const.C_FICHIER_MODIF) (Modif.properties)");
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}

	@PostConstruct
	public void init() {
		try {
			super.init(getInstanceOfEntity(entityClass));

			System.err.println("***********A CHANGER RAPIDEMENT**************************");
			//			gestionModif.setListeGestionModif("c:/lgrdoss/BureauDeGestion/demo/Bd/Modif.properties");
//			gestionModif.setListeGestionModif("/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE/GestionCommerciale/Bd/Modif.properties");
			//			setCnx(gestionModif.connexionDefaut());
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	@Override
	public void initConnexion() {	
		//		System.err.println("CHEMIN EN DUR A CHANGER : connection 'parallèle' pour obtenir metadata pour les controles");

		//		setCnx(gestionModif.connexionDefaut());

		//		final String C_URL_BDD          = "jdbc:firebirdsql:";
		////		public static final String C_URL_BDD          = "jdbc:firebirdsql://192.168.0.6/";
		//		final String C_USER             = "###_LOGIN_FB_BDG_###";
		//		final String C_PASS             = "###_PASSWORD_FB_BDG_###";
		//		final String C_DRIVER_JDBC      = "org.firebirdsql.jdbc.FBDriver";
		//		final String C_FICHIER_BDD      = "/home/nicolas/public/lgrdoss/BureauDeGestion/demo/Bd/GEST_COM.FDB";
		////		Connection cnx = ConnectionJDBC.getConnection(Const.C_URL_BDD+Const.C_FICHIER_BDD, Const.C_USER, Const.C_PASS, Const.C_DRIVER_JDBC);
		//		Connection cnx = ConnectionJDBC.getConnection(C_URL_BDD+"//localhost/"+C_FICHIER_BDD, C_USER, C_PASS, C_DRIVER_JDBC);
		//		setCnx(cnx);

		////		if (gestionModif.getListeGestionModif()==null)
		//			gestionModif.setListeGestionModif(Const.C_FICHIER_MODIF);
		//		gestionModif.setCnx(cnx);
		//		emf = getEntityManagerFactory();
	}

	public static void deconnectionGestionModif(){
		//GestionModif.deConnection();
	}


	//	public static EntityManagerFactory getEntityManagerFactory() {
	//		try {
	//			
	//		if (emf == null) {
	//			logger.info("Creation de EntityManagerFactory ...");
	//			// Create the EntityManagerFactory
	//			Map<String,String> configOverrides = new HashMap<String,String>();
	//			configOverrides.put("hibernate.connection.driver_class", Const.C_DRIVER_JDBC);
	//			configOverrides.put("hibernate.connection.password", Const.C_PASS);
	//			configOverrides.put("hibernate.connection.url", Const.C_URL_BDD+Const.C_FICHIER_BDD);
	//			configOverrides.put("hibernate.connection.username", Const.C_USER);
	//			//configOverrides.put("hibernate.dialect", "org.hibernate.dialect.FirebirdDialect");
	//			configOverrides.put("hibernate.dialect", "fr.legrain.gestCom.global.LgrHibernateFirebirdDialect");
	//			configOverrides.put("hibernate.validator.autoregister_listeners", "false");
	//			//configOverrides.put("hibernate.show_sql", "true");
	//
	//			/* Enable Hibernate's automatic session context management */
	////			configOverrides.put("current_session_context_class", "thread");
	////			configOverrides.put("hibernate.archive.autodetection", "class, hbm");
	//			
	//			configOverrides.put("hibernate.cache.use_second_level_cache", "true");
	//			configOverrides.put("hibernate.cache.provider_class","org.hibernate.cache.EhCacheProvider");
	//			
	//			//configOverrides.put("hibernate.cache.use_query_cache", "true");
	//
	//			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,configOverrides);
	//			logger.info("EntityManagerFactory créé.");
	//		}
	//		} catch (Exception e) {
	//			logger.error("EntityManagerFactory pas générée correctement",e);
	//			return null;
	//		}
	//		return emf;
	//	}

	//	public static void CloseEntityManagerFactory() {
	//			ConnectionJDBC.closeConnection();
	//			deconnectionGestionModif();
	//			if (emf != null) {
	//				emf.close();
	//				emf=null;
	//			}
	//	}

	public boolean validateAll(Entity entity, String validationContext, boolean verrouilleCode) throws Exception{
		//for (Field field : Entity.getClass().getDeclaredFields()) {

		Class a = (Class<Entity>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		boolean res = true;
		PropertyDescriptor propertyDescriptor = null;
		//for (Field field : a.getClass().getDeclaredFields()) {
		for (Field field : a.getDeclaredFields()) {
			LgrHibernateValidated col = null;
			// check if field has annotation
			System.out.println(a.getName()+" *** "+field.getName());

			//if ((col = field.getAnnotation(LgrHibernateValidated.class)) != null) {
			propertyDescriptor = PropertyUtils.getPropertyDescriptor(entity, field.getName());
			if (propertyDescriptor!=null && PropertyUtils.getReadMethod(propertyDescriptor)!=null)
				col = PropertyUtils.getReadMethod(propertyDescriptor).getAnnotation(LgrHibernateValidated.class);

			if (propertyDescriptor!=null && col != null) {
				boolean isPrimitiveOrWrapped = field.getType().isPrimitive() || ClassUtils.wrapperToPrimitive(field.getType()) != null || field.getType().equals(String.class) || field.getType().equals(Date.class);
				//if(!field.getName().startsWith("ta")) {
				if(isPrimitiveOrWrapped) {
					res = validate(entity, field.getName(), validationContext);
					if(!res) {
						return false;
					}
				} else {
					System.out.println("A CORRIGER : Probleme de validation d'objet composé");
				}
			}
		}
		return true;

	}


	public boolean validate(Entity entity, String field, String validationContext) throws Exception{
		return validate(entity, field, validationContext, null, false);
	}

	public boolean validateDTO(DTO dto, String field, String validationContext) throws Exception{
		return validateDTO(dto, field, validationContext, null, false);
	}
	
	public boolean validate(Entity entity, String field, ModeObjetEcranServeur modeEcran, String validationContext) throws Exception{
		return validate(entity, field, validationContext, modeEcran, false);
	}

	public boolean validateDTO(DTO dto, String field, ModeObjetEcranServeur modeEcran, String validationContext) throws Exception{
		return validateDTO(dto, field, validationContext, modeEcran, false);
	}

	/**
	 * 
	 * @param entity - Entite contenant la valeur
	 * @param field - Nom du champ de l'entité à valider
	 * @param validationContext - contexte de validation
	 * @param verrouilleCode - Pour les champs/codes dont on veut empecher la modif s'ils sont deja en cours de modif 
	 * ou pour empecher de générer 2 fois un même code
	 * @return
	 */
	public boolean validate(Entity entity, String field, String validationContext, ModeObjetEcranServeur modeEcran, boolean verrouilleCode) throws Exception{
		//public IStatus validate(Object entity, String field, String contexte){
		boolean s = false;
		try {
			//				ClassValidator<TaUnite> uniteValidator = new ClassValidator<TaUnite>( TaUnite.class );
			//				InvalidValue[] iv = uniteValidator.getInvalidValues(entity,field);

			System.out.println("Serveur (validate appellé manuellement, pas automatiquement à partir des Beans Validation) => xxxDAO.validate() : "+field);

			MessCtrlLgr mess = new MessCtrlLgr();
			LgrHibernateValidated a = PropertyUtils.getReadMethod(PropertyUtils.getPropertyDescriptor(entity, field)).getAnnotation(LgrHibernateValidated.class);
			mess.setContexte(validationContext);
			if(a!=null){//isa le 19-03-2009 car existant dans taPrixDao par exemple
				mess.setNomChampBd(a.champBd());
				mess.setNomChampEntite(field);
				mess.setModeObjet(modeEcran);
				mess.setNomTable(entity.getClass().getAnnotation(Table.class).name());
				mess.setEntityClass(a.clazz());
				//					mess.setModeObjet(getModeObjet());
				if(PropertyUtils.getProperty(entity, field)!=null) {
					if(PropertyUtils.getProperty(entity, field).getClass() == java.util.Date.class)
						mess.setValeur(LibDate.dateToString((java.util.Date)PropertyUtils.getProperty(entity, field)));
					else
						mess.setValeur(PropertyUtils.getProperty(entity, field).toString());

					//annulerCodeGenere(mess.getValeur(),mess.getNomChampDB());
					if(verrouilleCode) {
						rentreCodeGenere(mess.getValeur(),mess.getNomChampDB());
					}
				} else {
					mess.setValeur(null);
				}
				mess.setNomChampId(initChampId(entity));
				initValeurIdTable(entity);
				mess.setID_Valeur(valeurIdTable.toString());

				ctrlGeneraux.setModeServeur(true);
				ctrlGeneraux.ctrlSaisie(mess);
			} else {
				logger.error("Annotation de controle (LgrHibernateValidated) non presente.");
			}
			//s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
			s = true;
			//if(s.getSeverity()!=IStatus.ERROR ){
			if(!s){
				ctrlSaisieSpecifique(entity, field);
			}
			return s;
		} catch (ExceptLgr e) {
			logger.error("",e);
			//s = new Status(Status.ERROR,gestComBdPlugin.PLUGIN_ID,e.getMessage(),e);
			s = false;
			throw new Exception(e.getMessage());
			//return s;

			//valid = false;
		} catch (IllegalAccessException e) {
			logger.error("",e);
		} catch (InvocationTargetException e) {
			logger.error("",e);
		} catch (NoSuchMethodException e) {
			logger.error("",e);
		} catch (Exception e) {
			logger.error("",e);
		}
		return s;

	}
	
	/**
	 * 
	 * @param dto - dto contenant la valeur
	 * @param field - Nom du champ du dto à valider
	 * @param validationContext - contexte de validation
	 * @param verrouilleCode - Pour les champs/codes dont on veut empecher la modif s'ils sont deja en cours de modif 
	 * ou pour empecher de générer 2 fois un même code
	 * @return
	 */
	public boolean validateDTO(DTO dto, String field, String validationContext, ModeObjetEcranServeur modeEcran, boolean verrouilleCode) throws Exception{
		//public IStatus validate(Object entity, String field, String contexte){
		boolean s = false;
		try {
			//				ClassValidator<TaUnite> uniteValidator = new ClassValidator<TaUnite>( TaUnite.class );
			//				InvalidValue[] iv = uniteValidator.getInvalidValues(entity,field);

			System.out.println("Serveur (validateDTO appellé manuellement, pas automatiquement à partir des Beans Validation) => xxxDAO.validate() : "+field);

			MessCtrlLgr mess = new MessCtrlLgr();
			LgrHibernateValidated a = PropertyUtils.getReadMethod(PropertyUtils.getPropertyDescriptor(dto, field)).getAnnotation(LgrHibernateValidated.class);
			mess.setContexte(validationContext);
			if(a!=null){//isa le 19-03-2009 car existant dans taPrixDao par exemple
				mess.setNomChampBd(a.champBd());
				mess.setNomChampEntite(field);
				mess.setModeObjet(modeEcran);
				
				mess.setNomTable(a.clazz().getAnnotation(Table.class).name());
				mess.setEntityClass(a.clazz());
				//					mess.setModeObjet(getModeObjet());
				if(PropertyUtils.getProperty(dto, field)!=null) {
					if(PropertyUtils.getProperty(dto, field).getClass() == java.util.Date.class)
						mess.setValeur(LibDate.dateToString((java.util.Date)PropertyUtils.getProperty(dto, field)));
					else
						mess.setValeur(PropertyUtils.getProperty(dto, field).toString());

					//annulerCodeGenere(mess.getValeur(),mess.getNomChampDB());
					if(verrouilleCode) {
						rentreCodeGenere(mess.getValeur(),mess.getNomChampDB());
					}
				} else {
					mess.setValeur(null);
				}
				
				//non applicable aux validation de DTO
//				mess.setNomChampId(initChampId(dto));
//				initValeurIdTable(dto);
				
				mess.setID_Valeur(valeurIdTable.toString());

				ctrlGeneraux.setModeServeur(true);
				ctrlGeneraux.ctrlSaisie(mess);
			} else {
				logger.error("Annotation de controle (LgrHibernateValidated) non presente.");
			}
			//s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
			s = true;
			//if(s.getSeverity()!=IStatus.ERROR ){
			if(!s){
				ctrlSaisieSpecifiqueDTO(dto, field);
			}
			return s;
		} catch (ExceptLgr e) {
			logger.error("",e);
			//s = new Status(Status.ERROR,gestComBdPlugin.PLUGIN_ID,e.getMessage(),e);
			s = false;
			throw new Exception(e.getMessage());
			//return s;

			//valid = false;
		} catch (IllegalAccessException e) {
			logger.error("",e);
		} catch (InvocationTargetException e) {
			logger.error("",e);
		} catch (NoSuchMethodException e) {
			logger.error("",e);
		} catch (Exception e) {
			logger.error("",e);
		}
		return s;

	}

	/**
	 * Verification entre les champs, permet par exemple d'initialiser ou de modifier une valeurs dès qu'une autre valeur est saisie
	 * @param entity - l'objet modifier
	 * @param field - le champ modifier dans cet objet
	 * @throws ExceptLgr
	 */
	public void ctrlSaisieSpecifique(Entity entity,String field) throws ExceptLgr {}
	
	public void ctrlSaisieSpecifiqueDTO(DTO dto,String field) throws ExceptLgr {}



	public String genereCodeJPA() throws Exception{
		return genereCodeJPA(new LinkedList<String>());		
	}

	public String genereCodeJPA(List<String> listeCodes) throws Exception{
		try{
			boolean nonAutorise = false;
			String res=null;
			int compteur=-1;

			//code.setListeGestCode(Const.C_FICHIER_GESTCODE);
			//			TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO();
			TaInfoEntreprise taInfoEntreprise = entrepriseService.findInstance();

			while (!nonAutorise) {
				compteur ++;
				res=code.genereCodeJPA(nomTableJPA,compteur,taInfoEntreprise.getCodexoInfoEntreprise());
				nonAutorise=autoriseUtilisationCodeGenere(res,nomTableMere,true);
				if(listeCodes.size()>0)
					nonAutorise=!listeCodes.contains(res);
			};
			code.reinitialiseListeGestCode();
			return res;
		}catch(Exception e){
			logger.error("",e);
			return "";
		}
	}

	/*
	public static GenCode recupCodeDocument(String nomTable) throws Exception{
		try{
			boolean nonAutorise = false;
			String res=null;
			int compteur=-1;
			GenCode code = new GenCode();
			code.setListeGestCode(Const.C_FICHIER_GESTCODE);
			TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO();
			TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseDAO.findInstance();
			while (!nonAutorise) {
				compteur ++;
				res=code.genereCode(nomTable,compteur,taInfoEntreprise.getCodexoInfoEntreprise());
				nonAutorise=autoriseUtilisationCodeGenere(res,nomTable);	
			};
			code.reinitialiseListeGestCode();
			return code;
		}catch(Exception e){
			return null;
		}
	}	


	 */





	/* ejb
	public Boolean retourneEtatLegrain(Map<IDocumentTiers, Boolean> listeEtat,IDocumentTiers doc){
		for (IDocumentTiers docLoc : listeEtat.keySet()) {
			if(doc.getCodeDocument().equals(docLoc.getCodeDocument()))
				return docLoc.isLegrain();
		}
		return false;
	}

	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectDocNonTransformeRequete ");
		try {			
			String requete = "select a from Ta"+typeOrigine+" a join a.taTiers t where " ;
			if(codeTiers!=null ) //&& !codeTiers.equals("")
				requete+=" t.codeTiers like '"+codeTiers+"' and ";
			if(doc!=null)
				requete+=" a.idDocument = "+doc.getIdDocument()+" and ";
			requete+=" a.dateDocument between ? and ? and not exists(select r from TaRDocument r " +
					"join r.ta"+typeOrigine+" org " +
					"join r.ta"+typeDest+" dest " +
					" where org.idDocument" +
					" = a.idDocument and (dest.idDocument is not null or dest.idDocument!=0))";
			requete+=" order by t.codeTiers,a.ttc,a.dateDocument ";
			Query ejbQuery = getEntityManager().createQuery(requete);
			ejbQuery.setParameter(1, debut);
			ejbQuery.setParameter(2, fin);
			List<IDocumentTiers> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	 */

}