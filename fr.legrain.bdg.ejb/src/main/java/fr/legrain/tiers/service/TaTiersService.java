package fr.legrain.tiers.service;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaPanier;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTiers;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;

//@Stateful
@Stateless
//@DeclareRoles("admin")
//@RolesAllowed("admin")
//@StatefulTimeout(-1)
@WebService
@Interceptors(ServerTenantInterceptor.class)
//@SecurityDomain(value = "BDGRealm")
public class TaTiersService extends AbstractApplicationDAOServer<TaTiers, TaTiersDTO> implements ITaTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaTiersService.class);

	@Inject private ITiersDAO dao;
	@Inject private IFactureDAO daoFacture;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaPanierServiceRemote taPanierService;

	@EJB private ITaGenCodeExServiceRemote gencode;
	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel;
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI;

	/**
	 * Default constructor. 
	 */
	public TaTiersService() {
		super(TaTiers.class,TaTiersDTO.class);
		editionDefaut = "/reports/tiers/FicheTiers.rptdesign";
	}

	@PostConstruct
	public void init() {
		mapperUIToModel  = new LgrDozerMapper<TaTiersDTO,TaTiers>();
		mapperModelToUI  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	}

	public String generePDF(int idTiers) {
		try {
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("tiers.pdf");

			HashMap<String,Object> hm = new HashMap<>();
			hm.put( "tiers", findById(idTiers));
			BirtUtil.setAppContextEdition(hm);

			BirtUtil.startReportEngine();

			BirtUtil.renderReportToFile(
					//"https://dev.demo.promethee.biz:8443/reports/tiers/FicheTiers.rptdesign", 
					bdgProperties.urlDemoHttps()+editionDefaut,
					localPath, 
					new HashMap<>(), 
					BirtUtil.PDF_FORMAT,
					null,null);

			//			BirtUtil.renderReportToFile(
			//					"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.solstyce.webapp/src/main/webapp/reports/tiers/FicheTiers.rptdesign", 
			//					"/tmp/tiers.pdf", 
			//					new HashMap<>(), 
			//					BirtUtil.PDF_FORMAT);

			////			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTiers.rptdesign");
			//			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("fr.legrain.solstyce.webapp.war/reports/tiers/FicheTiers.rptdesign");
			//			 BirtUtil.renderReportToFile(
			//					inputStream, 
			//					"/tmp/tiers_hhh.pdf", 
			//					new HashMap<>(), 
			//					BirtUtil.PDF_FORMAT);

			return localPath;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public File exportToCSV(List<TaTiersDTO> l) {

		SchemaResolver sr = new SchemaResolver();
		String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("tiers.csv");
		try {
			File f = new File(localPath);
			FileWriter writer = new FileWriter(f);

			CSVWriter csvWriter = new CSVWriter(writer, ';', '\'');

//			List<String[]> data = toStringArray(l);
//			csvWriter.writeAll(data);
//			csvWriter.close();

			BeanToCsv bc = new BeanToCsv();
		    ColumnPositionMappingStrategy<TaTiersDTO> mappingStrategy = new ColumnPositionMappingStrategy<TaTiersDTO>();
		    mappingStrategy.setType(TaTiersDTO.class);
		    String[] columns = new String[]{
			    "nomTiers",
			    "id",
				"codeTiers",
				"nomTiers",
				"prenomTiers",
				"actifTiers",
				"idFamilleTiers",
				"codeFamilleTiers",
				"libelleFamilleTiers",
				"codepostalAdresse",
				"villeAdresse",
				"paysAdresse"
			};
		    
		    mappingStrategy.setColumnMapping(columns);
		    StatefulBeanToCsvBuilder<TaTiersDTO> builder = new StatefulBeanToCsvBuilder<TaTiersDTO>(writer);
		    StatefulBeanToCsv<TaTiersDTO> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
		    beanWriter.write(l);
		    writer.close();
		    
//		    bc.write(mappingStrategy,csvWriter,l);
//		    csvWriter.close();
		    
			return f;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<String[]> toStringArray(List<TaTiersDTO> l) {
		List<String[]> records = new ArrayList<String[]>();

		// entete
		//records.add(new String[] { "Id", "Nom" });

		Iterator<TaTiersDTO> it = l.iterator();
		while (it.hasNext()) {
			TaTiersDTO t = it.next();
			records.add(new String[] { 
					LibConversion.integerToString(t.getId()),
					t.getNomTiers() 
			});
		}
		return records;
	}

	public String getDefaultJPQLQueryIdentiteEntrepise() {
		return dao.getDefaultJPQLQueryIdentiteEntrepise();
	}

	public List<TaTiers> rechercheParType(String codeType) {
		return dao.rechercheParType(codeType);
	}

	public String getTiersActif() {
		return dao.getTiersActif();
	}

	@Override
	//	@RolesAllowed("admin")
	public String genereCode( Map<String, String> params) {
		try {
			return gencode.genereCodeJPA(TaTiers.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}

	public void annuleCode(String code) {
		try {

			gencode.annulerCodeGenere(gencode.findByCode(TaTiers.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaTiers.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPreferences(String preferences) {
		dao.setPreferences(preferences);
	}
	public List<Object[]> selectTiersSurTypeTiersLight() {
		return dao.selectTiersSurTypeTiersLight();
	}

	public List<TaTiersDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}

	public List<TaTiersDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaTiersDTO> findAllLightAdresseComplete() {
		return dao.findAllLightAdresseComplete();
	}

	public int countAllTiersActifTaTiersDTO() {
		return dao.countAllTiersActif();
	}
	
	public List<TaTiers> findByEmail(String adresseEmail) {
		return dao.findByEmail(adresseEmail);
	}
	public List<TaTiers> findByEmailParDefaut(String adresseEmail) {
		return dao.findByEmailParDefaut(adresseEmail);
	}
	
	public TaTiers findByEmailAndCodeTiers(String adresseEmail, String codeTiers) {
		return dao.findByEmailAndCodeTiers(adresseEmail,codeTiers);
	}
	public TaTiers findByEmailParDefautAndCodeTiers(String adresseEmail, String codeTiers) {
		return dao.findByEmailParDefautAndCodeTiers(adresseEmail,codeTiers);
	}
	
	public List<TaTiers> rechercheTiersPourCreationEspaceClientEnSerie() {
		return dao.rechercheTiersPourCreationEspaceClientEnSerie();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaTiers transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTiers(transientInstance.getCodeTiers().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTiers persistentInstance) throws EJBException {
		try {
			
			if(persistentInstance.getTaTTiers()!=null && persistentInstance.getTaTTiers().getCodeTTiers().equals(TaTTiers.VISITEUR_BOUTIQUE)) {
				//pour les visiteurs (pas encore de commande) il faut supprimer le panier actif et le compte espace client
				TaEspaceClient taEspaceClient = taEspaceClientService.findByCodeTiers(persistentInstance.getCodeTiers());
				if(taEspaceClient!=null) {
					taEspaceClientService.remove(taEspaceClient);
				}
				TaPanier taPanier = taPanierService.findByActif(persistentInstance.getCodeTiers());
				if(taPanier!=null) {
					taPanierService.remove(taPanier);
				}
			}
			
			persistentInstance = dao.findById(persistentInstance.getIdTiers());
			dao.remove(persistentInstance);
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		} catch (RemoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public TaTiers merge(TaTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTiers merge(TaTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTiers(detachedInstance.getCodeTiers().toUpperCase());
		detachedInstance=dao.merge(detachedInstance);
		annuleCode(detachedInstance.getCodeTiers());
		return detachedInstance;
	}

	public TaTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

	//	@RolesAllowed("admin")
	public List<TaTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTiersDTO entityToDTO(TaTiers entity) {
		//		TaTiersMapper mapper = new TaTiersMapper();
		//		return mapper.mapEntityToDto(entity, null);
		return mapperModelToUI.map(entity,TaTiersDTO.class);
	}

	public List<TaTiersDTO> listEntityToListDTO(List<TaTiers> entity) {
		List<TaTiersDTO> l = new ArrayList<TaTiersDTO>();

		for (TaTiers TaTiers : entity) {
			l.add(entityToDTO(TaTiers));
		}

		return l;
	}

	//	@RolesAllowed("admin")
	public List<TaTiersDTO> selectAllDTO() {
		System.out.println("List of TaTiersDTO EJB :");
		ArrayList <TaTiersDTO> liste = new ArrayList<TaTiersDTO>();

		List<TaTiers> projects = selectAll();
		for(TaTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}

	public void mergeDTO(TaTiersDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaTiersMapper mapper = new TaTiersMapper();
			TaTiers entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					entity = mapper.mapDtoToEntity(dto,entity);
				}
			}

			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}

	public void persistDTO(TaTiersDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaTiersMapper mapper = new TaTiersMapper();
			TaTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTiersDTO dto) throws RemoveException {
		try {
			TaTiersMapper mapper = new TaTiersMapper();
			TaTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTiers refresh(TaTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTiers value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaTiers value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaTiersDTO dto, String validationContext) {
		try {
			TaTiersMapper mapper = new TaTiersMapper();
			TaTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);

			//validation automatique via la JSR bean validation
			//			BeanValidator<TaTiersDTO> validator = new BeanValidator<TaTiersDTO>(TaTiersDTO.class);
			//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTiersDTO dto, String propertyName, /*ModeObjetEcranServeur modeObjetEcranServeur,*/ String validationContext) throws BusinessValidationException {
		try {
			//			TaTiersMapper mapper = new TaTiersMapper();
			//			TaTiers entity = mapper.mapDtoToEntity(dto,null);
			//			TaTiers entity = mapperUIToModel.map(dto, TaTiers.class);
			//			validateEntityProperty(entity,propertyName,validationContext);

			if(validationContext==null) validationContext="";
			//			validateDTO(dto, propertyName, modeObjetEcranServeur, validationContext);
			validateDTO(dto, propertyName, validationContext);

			//validation automatique via la JSR bean validation
			//			BeanValidator<TaTiersDTO> validator = new BeanValidator<TaTiersDTO>(TaTiersDTO.class);
			//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			//			throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}

	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTiersDTO dto) {
		validateDTO(dto,null);

	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTiersDTO dto, String propertyName) throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);

	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);

	}

	@Override
	@WebMethod(exclude = true)
	public List<TaTiers> selectTiersTypeDoc(String partieRequeteTiers,
			IDocumentTiers doc, String typeOrigine, String typeDest,
			Date debut, Date fin) {
		return dao.selectTiersTypeDoc(partieRequeteTiers, doc, typeOrigine, typeDest, debut, fin);
	}

	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers){
		return daoFacture.findChiffreAffaireTotalParCodeTiersDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<TaTiersDTO> findLightTTarif() {
		// TODO Auto-generated method stub
		return dao.findLightTTarif();
	}

	@Override
	public List<TaTiersDTO> findLightTTarifFamille(String codeTTarif, String codeFamille) {
		// TODO Auto-generated method stub
		 return dao.findLightTTarifFamille(codeTTarif, codeFamille);
	}

	
	@Override
	public List<TaTiersDTO> findListeTiersBoutique(String codeTTiers){
		return dao.findListeTiersBoutique(codeTTiers);
	}

}
