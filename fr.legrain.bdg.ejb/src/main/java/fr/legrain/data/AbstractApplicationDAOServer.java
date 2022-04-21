package fr.legrain.data;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Table;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.email.service.remote.IEnvoiEmailViaServiceExterneDossierService;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaDocumentTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.birt.BirtUtil;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaHistREtat;
import fr.legrain.document.model.TaHistREtatLigneDocument;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.email.service.EmailLgr;
import fr.legrain.email.service.MyQualifierEmailLiteral;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.MessCtrlLgr;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.MyQualifierLiteral;
import fr.legrain.paiement.service.PaiementGeneralDossierService;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaDocumentTiers;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.ICtrlLgr;
import fr.legrain.validator.LgrHibernateValidated;

public abstract class AbstractApplicationDAOServer<Entity,DTO> extends AbstractLgrDAOServer<Entity,DTO> {

	static Logger logger = Logger.getLogger(AbstractApplicationDAOServer.class);

	//private static final String PERSISTENCE_UNIT_NAME = "sample";

	//protected JPACtrl_Application ctrlGeneraux = null;
	@Inject protected ICtrlLgr ctrlGeneraux = null;
	@Inject protected IGenCode code = null;
	
	@Inject protected	TenantInfo tenantInfo;
	@Inject protected ITaInfoEntrepriseServiceLocal entrepriseService;
//	@Inject private ITaGenCodeExServiceRemote genCodeEx ;
	@EJB private PaiementGeneralDossierService paiementGeneralDossierService;
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementStripeDossierService;
	@EJB	protected ITaPreferencesServiceRemote taPreferencesService;
	@EJB	protected ITaEtatServiceRemote taEtatService;
	protected  @EJB ITaRDocumentServiceRemote taRDocumentService;
	protected  @EJB ITaLigneALigneServiceRemote taLigneALigneService;
    private @EJB ITaFlashServiceRemote taFlashServiceRemote;
    private @EJB ITaPreparationServiceRemote taPreparationServiceRemote;
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;
	
	
	private Class<Entity> entityClass;
	private Class<DTO> dtoClass;
	protected BdgProperties bdgProperties = null;
	protected String editionDefaut = null;
	
    private static Map<String,Field[]> classFields = new HashMap<String,Field[]>();
	private static Map<String,LgrHibernateValidated> fieldValidation = new HashMap<String,LgrHibernateValidated>();
	private static Map<String,PropertyDescriptor> fieldPropertyDescriptor = new HashMap<String,PropertyDescriptor>();
	
	protected boolean appelClientRiche() {
		try {
			TransactionSynchronizationRegistry reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
			if(reg!=null && reg.getResource("tenant")!=null ) {
				System.out.println("==> client lourd, remote EJB");
				return true;
			} else {
				System.out.println("==> client leger, JSF");
				return false;
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
		return false;
	}

	/**
	 * Créer un fichier xml dans le tmp/bdg/demo 
	 * Elle devrait être accessible par tout les services des documents, ou même les autres services
	 * @author yann
	 * @param taEdition l'edition auquel on veut extraire le blob et créer le fichier xml pour éventuellement generation de PDF par la suite
	 * @return un string représentant le chemin vers le fichier BIRT xml
	 */
	public String writingFileEdition(TaEdition taEdition) {
		String localPath;
		BdgProperties bdgProperties = new BdgProperties();
		localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());
		try {
			Files.write(Paths.get(localPath), taEdition.getFichierBlob());
			AnalyseFileReport afr = new AnalyseFileReport();
			afr.initializeBuildDesignReportConfig(localPath);
			afr.ajouteLogo();
			afr.closeDesignReportConfig();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return localPath;
		
	}

	public AbstractApplicationDAOServer() {
		super();

	}

	public AbstractApplicationDAOServer(Class<Entity> entityClass,Class<DTO> dtoClass) {
		super();
		bdgProperties = new BdgProperties();
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
	
	public Class getClassEntityGeneric() {
		Class entityClass = (Class<Entity>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return entityClass;
	}
	
	public Class getClassDTOGeneric() {
		Class dtoClass = (Class<DTO>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		return dtoClass;
	}
	
	@Deprecated
	public RetourPaiementCarteBancaire payerTicketDeCaisseCB(PaiementCarteBancaire cb, IDocumentPayableCB ticket, String libelle) throws EJBException {
		
		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
		if(service!=null) {
			return service.payer(compte, cb, ticket, libelle);
		}
//		
//
//		if(compte!=null) {
//
//			String implementation = "";
//
//			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
//			case UtilServiceWebExterne.CODE_SWE_STRIPE:
//				implementation = PaiementLgr.TYPE_STRIPE;
//				break;
//			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
//				implementation = PaiementLgr.TYPE_PAYPAL;
//				break;
//			default:
//				break;
//			}
//			
//			IPaiementEnLigneDossierService service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
//			
////			try {
//				return service.payer(compte, cb, ticket, libelle);
////			} catch(EJBException e) {
////				throw new EJBException(e);
////			}
//		} else {
//			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
//			paiementStripeDossierService.payer(compte, cb, ticket, libelle);
//		}

		return null;
	}
	
	@Deprecated
	public RetourPaiementCarteBancaire payerCB(PaiementCarteBancaire cb, TaTiers taTiers, String libelle) throws EJBException {
		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
		if(service!=null) {
			return service.payer(compte, cb, taTiers, libelle);
		}
//		if(compte!=null) {
//
//			String implementation = "";
//
//			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
//			case UtilServiceWebExterne.CODE_SWE_STRIPE:
//				implementation = PaiementLgr.TYPE_STRIPE;
//				break;
//			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
//				implementation = PaiementLgr.TYPE_PAYPAL;
//				break;
//			default:
//				break;
//			}
//			
//			IPaiementEnLigneDossierService service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
//			
////			try {
//				return service.payer(compte, cb, taTiers, libelle);
////			} catch(EJBException e) {
////				throw new EJBException(e);
////			}
//		} else {
//			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
//			paiementStripeDossierService.payer(compte, cb, taTiers, libelle);
//		}

		return null;
	}
	
	public String creerPaymentIntent(PaiementCarteBancaire cb, TaTiers taTiers, String libelle) throws EJBException {
		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
		if(service!=null) {
			return service.creerPaymentIntent(compte, cb, taTiers, libelle);
		}
//		if(compte!=null) {
//
//			String implementation = "";
//
//			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
//			case UtilServiceWebExterne.CODE_SWE_STRIPE:
//				implementation = PaiementLgr.TYPE_STRIPE;
//				break;
//			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
//				implementation = PaiementLgr.TYPE_PAYPAL;
//				break;
//			default:
//				break;
//			}
//			
//			IPaiementEnLigneDossierService service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
//			
////			try {
//				return service.creerPaymentIntent(compte, cb, taTiers, libelle);
////			} catch(EJBException e) {
////				throw new EJBException(e);
////			}
//		} else {
//			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
//			paiementStripeDossierService.creerPaymentIntent(compte, cb, taTiers, libelle);
//		}

		return null;
	}
	
	public String creerPaymentIntent(PaiementCarteBancaire cb, IDocumentPayableCB ticket, String libelle) throws EJBException {
		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
		if(service!=null) {
			return service.creerPaymentIntent(compte, cb, ticket, libelle);
		}
//		if(compte!=null) {
//
//			String implementation = "";
//
//			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
//			case UtilServiceWebExterne.CODE_SWE_STRIPE:
//				implementation = PaiementLgr.TYPE_STRIPE;
//				break;
//			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
//				implementation = PaiementLgr.TYPE_PAYPAL;
//				break;
//			default:
//				break;
//			}
//			
//			IPaiementEnLigneDossierService service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
//			
////			try {
//				return service.creerPaymentIntent(compte, cb, ticket, libelle);
////			} catch(EJBException e) {
////				throw new EJBException(e);
////			}
//		} else {
//			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
//			paiementStripeDossierService.creerPaymentIntent(compte, cb, ticket, libelle);
//		}

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
			System.err.println("** Service (EJB) pour : "+entityClass.getCanonicalName()+" *************************");

			System.err.println("**A CHANGER RAPIDEMENT : Modif.properties *************************");
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

	public boolean validateDTOAll(DTO dto, String validationContext) throws Exception{
		//for (Field field : Entity.getClass().getDeclaredFields()) {

		Class a = getClassDTOGeneric();
		
		String messageComplet =  null;
		//validation automatique via la JSR bean validation
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Set<ConstraintViolation<DTO>> violations = factory.getValidator().validate(dto);
		

		
		if (violations.size() > 0) {
			messageComplet = "Erreur de validation : ";
			for (ConstraintViolation<DTO> cv : violations) {
				messageComplet+=" "+cv.getMessage()+"\n";
			}
			throw new Exception(messageComplet);
		} else {
			//aucune erreur de validation "automatique", on déclanche les validations du controller
			return true;
		}

//		boolean res = true;
//		PropertyDescriptor propertyDescriptor = null;
//		//for (Field field : a.getClass().getDeclaredFields()) {
//		for (Field field : a.getDeclaredFields()) {
//			LgrHibernateValidated col = null;
//			// check if field has annotation
//			System.out.println(a.getName()+" *** "+field.getName());
//
//			//if ((col = field.getAnnotation(LgrHibernateValidated.class)) != null) {
//			propertyDescriptor = PropertyUtils.getPropertyDescriptor(dto, field.getName());
//			if (propertyDescriptor!=null && PropertyUtils.getReadMethod(propertyDescriptor)!=null)
//				col = PropertyUtils.getReadMethod(propertyDescriptor).getAnnotation(LgrHibernateValidated.class);
//
//			if (propertyDescriptor!=null && col != null) {
//				boolean isPrimitiveOrWrapped = field.getType().isPrimitive() || ClassUtils.wrapperToPrimitive(field.getType()) != null || field.getType().equals(String.class) || field.getType().equals(Date.class);
//				//if(!field.getName().startsWith("ta")) {
//				if(isPrimitiveOrWrapped) {
//					res = validateDTO(dto, field.getName(), validationContext);
//					if(!res) {
//						return false;
//					}
//				} else {
//					System.out.println("A CORRIGER : Probleme de validation d'objet composé dans le DTO !!!!");
//				}
//			}
//		}
//		return true;

	}
	
		
	
	@SuppressWarnings("unchecked")
	public boolean validateAll(Entity entity, String validationContext, boolean verrouilleCode) throws Exception{
		//for (Field field : Entity.getClass().getDeclaredFields()) {
		
		Date d3 = new Date();
		@SuppressWarnings("rawtypes")
		Date d = new Date();
		Class a = getClassEntityGeneric();
		Date d2 = new Date();
		
		String className = a.getCanonicalName();
		
		
		
		String messageComplet =  null;
		//validation automatique via la JSR bean validation
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Set<ConstraintViolation<Entity>> violations = null;
		if(factory!=null && factory.getValidator()!=null) {
			violations = factory.getValidator().validate(entity);
		}
		
		if(verrouilleCode) {
			//rentreCodeGenere(PropertyUtils.getProperty(entity, field),mess.getNomChampDB());
		}
		
		if (violations!=null && violations.size() > 0) {
			messageComplet = "Erreur de validation : ";
			for (ConstraintViolation<Entity> cv : violations) {
				messageComplet+=" "+cv.getMessage()+"\n";
			}
			throw new Exception(messageComplet);
		} else {
			//aucune erreur de validation "automatique", on déclanche les validations du controller
			return true;
		}

//		boolean res = true;
//		PropertyDescriptor propertyDescriptor = null;
//		
//		if(!classFields.containsKey(className))
//			classFields.put(className, a.getDeclaredFields());
//		
////		for (Field field : a.getDeclaredFields()) {
//		for (Field field : classFields.get(className)) {
//			LgrHibernateValidated col = null;
//			// check if field has annotation
//			System.out.println(a.getName()+" *** "+field.getName());
//
//			String propertyDescriptorKey = className+"."+field.getName();
//			if(!fieldPropertyDescriptor.containsKey(propertyDescriptorKey)) {
//				fieldPropertyDescriptor.put(propertyDescriptorKey, PropertyUtils.getPropertyDescriptor(entity, field.getName()));
//			}
//			propertyDescriptor = fieldPropertyDescriptor.get(propertyDescriptorKey);
//			
//			String annotationKey = className+"."+field.getName();
//			if(!fieldValidation.containsKey(annotationKey)) {
//				if (propertyDescriptor!=null && PropertyUtils.getReadMethod(propertyDescriptor)!=null) {
//					fieldValidation.put(annotationKey, PropertyUtils.getReadMethod(propertyDescriptor).getAnnotation(LgrHibernateValidated.class));
//				}
//			}
//			col = fieldValidation.get(annotationKey);
//		
//
//			if (propertyDescriptor!=null && col != null) {
//				boolean isPrimitiveOrWrapped = field.getType().isPrimitive() || ClassUtils.wrapperToPrimitive(field.getType()) != null || field.getType().equals(String.class) || field.getType().equals(Date.class);
//				//if(!field.getName().startsWith("ta")) {
//				if(isPrimitiveOrWrapped) {
//					res = validate(entity, field.getName(), validationContext,annotationKey);
//					if(!res) {
//						return false;
//					}
//				} else {
//					System.out.println("A CORRIGER : Probleme de validation d'objet composé");
//				}
//			}
//		}
//		Date d4 = new Date();
//		System.out.println(d+" ****** "+d2);
//		System.out.println("Validate ==================== "+d3+" ****** "+d4);
//		return true;
	}


	public boolean validate(Entity entity, String field, String validationContext,String cleCacheAnnotation) throws Exception{
		return validate(entity, field, validationContext, null, false, cleCacheAnnotation);
	}
	
	public boolean validate(Entity entity, String field, String validationContext) throws Exception{
		return validate(entity, field, validationContext, null, false, null);
	}

	public boolean validateDTO(DTO dto, String field, String validationContext) throws Exception{
		return validateDTO(dto, field, validationContext, null, false);
	}
	
	public boolean validate(Entity entity, String field, ModeObjetEcranServeur modeEcran, String validationContext, String cleCacheAnnotation) throws Exception{
		return validate(entity, field, validationContext, modeEcran, false,cleCacheAnnotation);
	}
	
	public boolean validate(Entity entity, String field, ModeObjetEcranServeur modeEcran, String validationContext) throws Exception{
		return validate(entity, field, validationContext, modeEcran, false, null);
	}

	public boolean validateDTO(DTO dto, String field, ModeObjetEcranServeur modeEcran, String validationContext) throws Exception{
		return validateDTO(dto, field, validationContext, modeEcran, false);
	}
	
	static private Map<String,String> nomTableSQL = new HashMap<String,String>();

	/**
	 * 
	 * @param entity - Entite contenant la valeur
	 * @param field - Nom du champ de l'entité à valider
	 * @param validationContext - contexte de validation
	 * @param verrouilleCode - Pour les champs/codes dont on veut empecher la modif s'ils sont deja en cours de modif 
	 * ou pour empecher de générer 2 fois un même code
	 * @return
	 */
	public boolean validate(Entity entity, String field, String validationContext, ModeObjetEcranServeur modeEcran, boolean verrouilleCode, String cleCacheAnnotation) throws Exception{
		//public IStatus validate(Object entity, String field, String contexte){
		Class a = getClassEntityGeneric();
		//**
		boolean s = false;
		try {
			//				ClassValidator<TaUnite> uniteValidator = new ClassValidator<TaUnite>( TaUnite.class );
			//				InvalidValue[] iv = uniteValidator.getInvalidValues(entity,field);
			
			if(!appelClientRiche()) {
				//Class<Entity> cEntity = entity.getClass();
				String nomChamp =  field;
				String messageComplet =  null;
				//validation automatique via la JSR bean validation
				ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
				Set<ConstraintViolation<Entity>> violations = factory.getValidator().validateValue(a,nomChamp,PropertyUtils.getProperty(entity, field));
				
				if(verrouilleCode) {
					//rentreCodeGenere(PropertyUtils.getProperty(entity, field),mess.getNomChampDB());
				}
				
				if (violations.size() > 0) {
					messageComplet = "Erreur de validation : ";
					for (ConstraintViolation<Entity> cv : violations) {
						messageComplet+=" "+cv.getMessage()+"\n";
					}
					throw new Exception(messageComplet);
				} else {
					//aucune erreur de validation "automatique", on déclanche les validations du controller
					
				}
	
//				System.out.println("Serveur (validate appellé manuellement, pas automatiquement à partir des Beans Validation) => xxxDAO.validate() : "+field);
//	
//				MessCtrlLgr mess = new MessCtrlLgr();
//				LgrHibernateValidated a = null;
//				if(cleCacheAnnotation!=null) {
//					a = fieldValidation.get(cleCacheAnnotation);
//				} else {
//					a = PropertyUtils.getReadMethod(PropertyUtils.getPropertyDescriptor(entity, field)).getAnnotation(LgrHibernateValidated.class);
//				}
//				mess.setContexte(validationContext);
//	
//				if(a!=null){//isa le 19-03-2009 car existant dans taPrixDao par exemple
//					mess.setNomChampBd(a.champBd());
//					mess.setNomChampEntite(field);
//					mess.setModeObjet(modeEcran);
//					if(!nomTableSQL.containsKey(entity.getClass().getCanonicalName()))
//						nomTableSQL.put(entity.getClass().getCanonicalName(), entity.getClass().getAnnotation(Table.class).name());
//					mess.setNomTable(nomTableSQL.get(entity.getClass().getCanonicalName()));
//					mess.setEntityClass(a.clazz());
//					//					mess.setModeObjet(getModeObjet());
//					Object p = PropertyUtils.getProperty(entity, field);
//					if(p!=null) {
//						if(p.getClass() == java.util.Date.class)
//							mess.setValeur(LibDate.dateToString((java.util.Date)p));
//						else
//							mess.setValeur(p.toString());
//	
//						//annulerCodeGenere(mess.getValeur(),mess.getNomChampDB());
//						if(verrouilleCode) {
//							rentreCodeGenere(mess.getValeur(),mess.getNomChampDB());
//						}
//					} else {
//						mess.setValeur(null);
//					}
//					mess.setNomChampId(initChampId(entity));
//					initValeurIdTable(entity);
//					mess.setID_Valeur(valeurIdTable.toString());
//					
//					ctrlGeneraux.setModeServeur(true);
//					ctrlGeneraux.ctrlSaisie(mess); // 1/2 @
//				} else {
//					logger.error("Annotation de controle (LgrHibernateValidated) non presente.");
//				}
//				//s = new Status(Status.OK,gestComBdPlugin.PLUGIN_ID,"OK");
//				s = true;
//				//if(s.getSeverity()!=IStatus.ERROR ){
				if(!s){
					ctrlSaisieSpecifique(entity, field);
				}
			} else {
				System.out.println("Appel client riche, pas de validation entity pour l'instant");
				s = true;
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
			
			if(!appelClientRiche()) {
	
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
					Object p = PropertyUtils.getProperty(dto, field);
					if(p!=null) {
						if(p.getClass() == java.util.Date.class)
							mess.setValeur(LibDate.dateToString((java.util.Date)p));
						else
							mess.setValeur(p.toString());
	
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
			} else {
				System.out.println("Appel client riche, pas de validation DTO pour l'instant");
				s = true;
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
	
//	public List<IDocumentTiers> dejaGenereDocument(IDocumentDTO ds ) {
//		List<IDocumentTiers> dejaGenere = null;
//		String requeteFixeRDocument = null;
//		if(!ds.getTypeDocument().equals(TaBonReception.TYPE_DOC)
//				&& !ds.getTypeDocument().equals(TaAbonnement.TYPE_DOC)) {
//			if(ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAcompte.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvoir.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taAvisEcheance.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taApporteur.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBoncde.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taBonliv.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taDevis.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taFacture.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taPrelevement.idDocument="+ds.getId();
//			if(ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteFixeRDocument=" from TaRDocument x where x.taProforma.idDocument="+ds.getId();
//			String requeteDoc="";
//			if(ds!=null && ds.getId()!=0) {
//				String jpql = "select x "+requeteFixeRDocument;
//				List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
//				if(l!=null && l.size()>0)dejaGenere=new LinkedList<>();
//				for (TaRDocument taRDocument : l) {
//					if(taRDocument.getTaAcompte()!=null && !ds.getTypeDocument().equals(TaAcompte.TYPE_DOC))requeteDoc="select x.taAcompte ";
//					if(taRDocument.getTaAvoir()!=null && !ds.getTypeDocument().equals(TaAvoir.TYPE_DOC))requeteDoc="select x.taAvoir ";
//					if(taRDocument.getTaAvisEcheance()!=null && !ds.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC))requeteDoc="select x.taAvisEcheance ";;
//					if(taRDocument.getTaApporteur()!=null && !ds.getTypeDocument().equals(TaApporteur.TYPE_DOC))requeteDoc="select x.taApporteur ";
//					if(taRDocument.getTaBoncde()!=null && !ds.getTypeDocument().equals(TaBoncde.TYPE_DOC))requeteDoc="select x.taBoncde ";
//					if(taRDocument.getTaBonliv()!=null && !ds.getTypeDocument().equals(TaBonliv.TYPE_DOC))requeteDoc="select x.taBonliv ";
//					if(taRDocument.getTaDevis()!=null && !ds.getTypeDocument().equals(TaDevis.TYPE_DOC))requeteDoc="select x.taDevis ";
//					if(taRDocument.getTaFacture()!=null && !ds.getTypeDocument().equals(TaFacture.TYPE_DOC))requeteDoc="select x.taFacture ";
//					if(taRDocument.getTaPrelevement()!=null && !ds.getTypeDocument().equals(TaPrelevement.TYPE_DOC))requeteDoc="select x.taPrelevement ";
//					if(taRDocument.getTaProforma()!=null && !ds.getTypeDocument().equals(TaProforma.TYPE_DOC))requeteDoc="select x.taProforma ";
//					
//					List<IDocumentTiers> l2 =taRDocumentService.dejaGenereDocument(requeteDoc+requeteFixeRDocument);
//					for (IDocumentTiers iDocumentTiers : l2) {
//						dejaGenere.add(iDocumentTiers);
//					}
//					System.err.println(l.size());
//				}
//			} else {
//				dejaGenere = new LinkedList<>();
//			}
//		}
//			return dejaGenere;
//	}

	public BdgProperties getBdgProperties() {
		return bdgProperties;
	}


	public void setBdgProperties(BdgProperties bdgProperties) {
		this.bdgProperties = bdgProperties;
	}


	public String getEditionDefaut() {
		return editionDefaut;
	}


	public void setEditionDefaut(String editionDefaut) {
		this.editionDefaut = editionDefaut;
	}

	

	

//	public String genereCodeJPA() throws Exception{
//		return genereCodeJPA(new LinkedList<String>());		
//	}
//
//	public String genereCodeJPA(List<String> listeCodes) throws Exception{
//		try{
//			boolean nonAutorise = false;
//			String res=null;
//			int compteur=-1;
//
//			//code.setListeGestCode(Const.C_FICHIER_GESTCODE);
//			//			TaInfoEntrepriseDAO taInfoEntrepriseDAO = new TaInfoEntrepriseDAO();
//			TaInfoEntreprise taInfoEntreprise = entrepriseService.findInstance();
//
//			while (!nonAutorise) {
//				compteur ++;
////				res=code.genereCodeJPA(nomTableJPA,compteur,taInfoEntreprise.getCodexoInfoEntreprise());
//				res=genCodeEx.genereCodeExJPA(nomTableJPA,taInfoEntreprise.getCodexoInfoEntreprise());
//				nonAutorise=autoriseUtilisationCodeGenere(res,nomTableMere,true);
//				if(listeCodes.size()>0)
//					nonAutorise=!listeCodes.contains(res);
//			};
//			code.reinitialiseListeGestCode();
//			return res;
//		}catch(Exception e){
//			logger.error("",e);
//			return "";
//		}
//	}

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
	public IDocumentTiers recupSetREtat(IDocumentTiers doc) {
		if(doc!=null) {
			if(doc.getTaREtat()!=null)doc.getTaREtat().getIdREtat();
			if(doc.getTaREtats()!=null) {
				for (TaREtat o : doc.getTaREtats()) {
					o.getIdREtat();
				}
			}
		}
		return doc;
	}
	
	public IDocumentTiers recupTiers(IDocumentTiers obj) {
		if(obj!=null) obj.getTaTiers().getIdTiers();
		return obj;
		
	}
	
	public IDocumentTiers recupSetArticle(IDocumentTiers obj) {
		if(obj!=null) {	
		for (ILigneDocumentTiers o : obj.getLignesGeneral()) {
			if(o.getTaArticle()!=null)o.getTaArticle().getIdArticle();
		}
		}
		return obj;
		
	}
	
	public IDocumentTiers recupSetHistREtat(IDocumentTiers doc) {
		if(doc!=null) {
		if(doc.getTaHistREtats()!=null) {
			for (TaHistREtat o : doc.getTaHistREtats()) {
				o.getIdHistREtat();
			}
		}
		}
		return doc;
	}
	
	public TaFlash recupSetREtat(TaFlash doc) {
		if(doc!=null) {
			if(doc.getTaREtat()!=null)doc.getTaREtat().getIdREtat();
			if(doc.getTaREtats()!=null) {
				for (TaREtat o : doc.getTaREtats()) {
					o.getIdREtat();
				}
			}
		}
		return doc;
	}
	
	public TaFlash recupSetHistREtat(TaFlash doc) {
		if(doc!=null) {
		if(doc.getTaHistREtats()!=null) {
			for (TaHistREtat o : doc.getTaHistREtats()) {
				o.getIdHistREtat();
			}
		}
		}
		return doc;
	}
	
	public TaFlash recupSetLigneALigne(TaFlash doc) {
		if(doc!=null) {
		for (TaLFlash l : doc.getLignes()) {
			for (TaLigneALigne ll : l.getTaLigneALignes()) {
				ll.getId();
			}
		}
		}
		return doc;
	}	
	/**
	 * @author yann
	 * @param doc
	 * @return
	 */
	protected IDocumentTiers recupSetLigneALigneEcheance(IDocumentTiers doc) {
		if(doc!=null) {
			for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
				if(l.getTaLigneALignes()!=null) {
					for (TaLigneALigneEcheance ll : l.getTaLigneALignesEcheance()) {
						ll.getId();
						if(ll.getTaLEcheance()!=null)ll.getTaLEcheance().getIdLEcheance();
						if(ll.getTaLAcompte()!=null)ll.getTaLAcompte().getIdLDocument();
						if(ll.getTaLApporteur()!=null)ll.getTaLApporteur().getIdLDocument();
						if(ll.getTaLAvisEcheance()!=null)ll.getTaLAvisEcheance().getIdLDocument();
						if(ll.getTaLAvoir()!=null)ll.getTaLAvoir().getIdLDocument();
						if(ll.getTaLBoncde()!=null)ll.getTaLBoncde().getIdLDocument();
						if(ll.getTaLBoncdeAchat()!=null)ll.getTaLBoncdeAchat().getIdLDocument();
						if(ll.getTaLBonliv()!=null)ll.getTaLBonliv().getIdLDocument();
						if(ll.getTaLBonReception()!=null)ll.getTaLBonReception().getIdLDocument();
						if(ll.getTaLDevis()!=null)ll.getTaLDevis().getIdLDocument();
						if(ll.getTaLFacture()!=null)ll.getTaLFacture().getIdLDocument();
						if(ll.getTaLFlash()!=null)ll.getTaLFlash().getIdLFlash();
						if(ll.getTaLPrelevement()!=null)ll.getTaLPrelevement().getIdLDocument();
						if(ll.getTaLPreparation()!=null)ll.getTaLPreparation().getIdLDocument();
						if(ll.getTaLProforma()!=null)ll.getTaLProforma().getIdLDocument();
						if(ll.getTaLTicketDeCaisse()!=null)ll.getTaLTicketDeCaisse().getIdLDocument();					
					}
				}
			}
			}
			return doc;
	}
	
	protected IDocumentTiers recupSetLigneALigne(IDocumentTiers doc) {
		if(doc!=null) {
		for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
			if(l.getTaLigneALignes()!=null) {
				for (TaLigneALigne ll : l.getTaLigneALignes()) {
					ll.getId();
					if(ll.getTaLAcompte()!=null)ll.getTaLAcompte().getIdLDocument();
					if(ll.getTaLApporteur()!=null)ll.getTaLApporteur().getIdLDocument();
					if(ll.getTaLAvisEcheance()!=null)ll.getTaLAvisEcheance().getIdLDocument();
					if(ll.getTaLAvoir()!=null)ll.getTaLAvoir().getIdLDocument();
					if(ll.getTaLBoncde()!=null)ll.getTaLBoncde().getIdLDocument();
					if(ll.getTaLBoncdeAchat()!=null)ll.getTaLBoncdeAchat().getIdLDocument();
					if(ll.getTaLBonliv()!=null)ll.getTaLBonliv().getIdLDocument();
					if(ll.getTaLBonReception()!=null)ll.getTaLBonReception().getIdLDocument();
					if(ll.getTaLDevis()!=null)ll.getTaLDevis().getIdLDocument();
					if(ll.getTaLFacture()!=null)ll.getTaLFacture().getIdLDocument();
					if(ll.getTaLFlash()!=null)ll.getTaLFlash().getIdLFlash();
					if(ll.getTaLPrelevement()!=null)ll.getTaLPrelevement().getIdLDocument();
					if(ll.getTaLPreparation()!=null)ll.getTaLPreparation().getIdLDocument();
					if(ll.getTaLProforma()!=null)ll.getTaLProforma().getIdLDocument();
					if(ll.getTaLTicketDeCaisse()!=null)ll.getTaLTicketDeCaisse().getIdLDocument();					
				}
			}
		}
		}
		return doc;
	}
	
	protected IDocumentTiers recupSetREtatLigneDocuments(IDocumentTiers doc) {
		if(doc!=null) {
		for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
			l.getTaDocument().getIdDocument();
			if(l.getTaREtatLigneDocument()!=null)l.getTaREtatLigneDocument().getIdREtatLigneDoc();
			if(l.getTaREtatLigneDocuments()!=null) {
				for (TaREtatLigneDocument ll : l.getTaREtatLigneDocuments()) {
					ll.getIdREtatLigneDoc();
				}
			}
		}
		}
		return doc;
	}
	
	
	protected TaFlash recupSetREtatLigneDocuments(TaFlash doc) {
		if(doc!=null) {
		for (TaLFlash l : doc.getLignes()) {
			if(l.getTaREtatLigneDocument()!=null)l.getTaREtatLigneDocument().getIdREtatLigneDoc();
			for (TaREtatLigneDocument ll : l.getTaREtatLigneDocuments()) {
				ll.getIdREtatLigneDoc();
			}
		}
		}
		return doc;
	}
	
	
	protected IDocumentTiers recupSetHistREtatLigneDocuments(IDocumentTiers doc) {
		if(doc!=null) {
		for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
			if(l.getTaHistREtatLigneDocuments()!=null) {
				for (TaHistREtatLigneDocument ll : l.getTaHistREtatLigneDocuments()) {
					ll.getIdHistREtatLigneDoc();
				}
			}
		}
		}
		return doc;
	}
	
	
	protected TaFlash recupSetHistREtatLigneDocuments(TaFlash doc) {
		if(doc!=null) {
		for (TaLFlash l : doc.getLignes()) {
			for (TaHistREtatLigneDocument ll : l.getTaHistREtatLigneDocuments()) {
				ll.getIdHistREtatLigneDoc();
			}
		}
		}
		return doc;
	}
	
	protected IDocumentTiers recupSetRDocument(IDocumentTiers doc) {
		if(doc!=null) {
		if(doc.getTaRDocuments()!=null) {
			for (TaRDocument l : doc.getTaRDocuments()) {
				l.getIdRDocument();
			}
		}
		}
		return doc;
	}	
	
    public List<TaLFlash>  recupListeLienLigneALigneFlash(IDocumentTiers persistentInstance) {
		logger.debug("recupListeLienLigneALigne pour TaFlash");
		try {
			IDocumentTiers objInitial = persistentInstance;
//			try {
//				if(persistentInstance.getIdDocument()!=0)
//					objInitial=(IDocumentTiers) findById(persistentInstance.getIdDocument());
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//			}
			List<TaLFlash>listeLien = new LinkedList<TaLFlash>();
			for (ILigneDocumentTiers l : objInitial.getLignesGeneral()) {
				for (TaLigneALigne ll : l.getTaLigneALignes()) {
					if(ll.getTaLFlash()!=null)listeLien.add(ll.getTaLFlash());	
	
				}
			}
			return listeLien;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("recupListeLienLigneALigne pour TaLFlash failed", re);
			throw re2;
		}
	}
    
    
	public void  mergeTaFlashLieParLigneALigneFlash(List<TaLFlash>listeLien) {
        logger.debug("mergeTaFlashLieParLigneALigne");
        try {
            TaFlash doc;
              for (TaLFlash ldoc : listeLien) {
//                  if(ldoc instanceof TaLFlash) {
                      doc=taFlashServiceRemote.findDocByLDoc(ldoc);
                      taFlashServiceRemote.mergeEtat((TaFlash) doc);
//                }
              }
        } catch (RuntimeException re) {
              RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
              logger.error("mergeTaFlashLieParLigneALigne failed", re);
              throw re2;
        }
  }
	
	
	protected IDocumentTiers recupSetRReglementLiaison(IDocumentTiers doc) {
		if(doc!=null) {
		if(doc.getTaRReglementLiaisons()!=null) {
			for (TaRReglementLiaison l : doc.getTaRReglementLiaisons()) {
				l.getId();
			}
		}
		}
		return doc;
	}
	
	
}