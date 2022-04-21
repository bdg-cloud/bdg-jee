package fr.legrain.recherche.multicritere.service;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRTitreTransportServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaArticleMapper;
import fr.legrain.bdg.recherche.multicritere.service.remote.IRechercheMultiCritereServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.recherche.multicritere.dao.IRechercheMultiCritereDAO;
import fr.legrain.recherche.multicritere.model.GroupeLigne;
import fr.legrain.tiers.dto.TaTiersDTO;


/**
 * Session Bean implementation class TaArticleBean
 */
@Stateless
//@Stateful
//@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class RechercheMultiCritereService /*extends AbstractApplicationDAOServer<TaArticle, TaArticleDTO>*/ implements IRechercheMultiCritereServiceRemote {

	static Logger logger = Logger.getLogger(RechercheMultiCritereService.class);

//	@Inject private IArticleDAO dao;
	@Inject private IRechercheMultiCritereDAO dao;
//	@Inject private IFactureDAO daoFacture;
	@Inject private	SessionInfo sessionInfo;
//	@EJB private ITaRTitreTransportServiceRemote daoRTitreTransportService;
//	@EJB private ITaGenCodeExServiceRemote gencode;
	
	public ArrayList<Object> getResultat(String resultat,List<GroupeLigne> groupe) {
		return dao.getResultat(resultat, groupe);
	}
	
//	/**
//	 * Default constructor. 
//	 */
//	public RechercheMultiCritereService() {
//		super(TaArticle.class,TaArticleDTO.class);
//	}
//
//	//	private String defaultJPQLQuery = "select a from TaArticle a";
//
//	public List<TaArticle> findByCodeTva(String codeTva) {
//		return dao.findByCodeTva(codeTva);
//	}
//
//	public List<Object[]> findByActifLight(boolean actif) {
//		return dao.findByActifLight(actif);
//	}
//
//	public List<Object[]> findByEcranLight() {
//		return dao.findByEcranLight();
//	}
//	
//	public List<TaArticleDTO> findAllLight() {
//		return dao.findAllLight();
//	}
//	
//	public List<TaArticleDTO> findByCodeLight(String code) {
//		return dao.findByCodeLight(code);
//	}
//	
//	public List<TaArticleDTO> findByCodeLightMP(String code) {
//		return dao.findByCodeLightMP(code);
//	}
//	
//	public List<TaArticleDTO> findByCodeLightPF(String code) {
//		return dao.findByCodeLightPF(code);
//	}
//
//	public int countAllArticleActif() {
//		return dao.countAllArticleActif();
//	}
//	
//	public List<TaArticleDTO> findArticleCaisseLight(String codeFamille) {
//		return dao.findArticleCaisseLight(codeFamille);
//	}
//	
//	public TaArticle findByCodeBarre(String codeBarre) {
//		return dao.findByCodeBarre(codeBarre);
//	}
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	// 										ENTITY
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	public void persist(TaArticle transientInstance) throws CreateException {
//		persist(transientInstance, null);
//	}
//
//	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
//	@WebMethod(operationName = "persistValidationContext")
//	public void persist(TaArticle transientInstance, String validationContext) throws CreateException {
//
//		validateEntity(transientInstance, validationContext);
//
//		dao.persist(transientInstance);
//	}
//
//	public void remove(TaArticle persistentInstance) throws RemoveException {
//		try {
//			dao.remove(findById(persistentInstance.getIdArticle()));
//		} catch (FinderException e) {
//			logger.error("", e);
//		}
//	}
//
//	public TaArticle merge(TaArticle detachedInstance) {
//		return merge(detachedInstance, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "mergeValidationContext")
//	public TaArticle merge(TaArticle detachedInstance, String validationContext) {
//		validateEntity(detachedInstance, validationContext);
//		detachedInstance.setCodeArticle(detachedInstance.getCodeArticle().toUpperCase());
//		 detachedInstance=dao.merge(detachedInstance);
//		 this.annuleCode(detachedInstance.getCodeArticle());
//		 return detachedInstance;
//	}
//
//	public TaArticle findById(int id) throws FinderException {
//		return dao.findById(id);
//	}
//	public Map<Integer,String> findIdAndCode() throws FinderException {
//		return dao.findIdEtCode();
//	}
//	public Map<String,String> findCodeAndLibelle() throws FinderException {
//		return dao.findCodeEtLibelle();
//	}
//	public TaArticle findByCode(String code) throws FinderException {
//		return dao.findByCode(code);
//	}
//
//	//@RolesAllowed("admin")
//	public List<TaArticle> selectAll() {
//		return dao.selectAll();
//	}
//
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	// 										DTO
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	public List<TaArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
//		return null;
//	}
//
//	@Override
//	public List<TaArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
//		List<TaArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
//		List<TaArticleDTO> l = null;
//		if(entityList!=null) {
//			l = listEntityToListDTO(entityList);
//		}
//		return l;
//	}
//
//	public TaArticleDTO entityToDTO(TaArticle entity) {
//		//		TaArticleDTO dto = new TaArticleDTO();
//		//		dto.setId(entity.getIdTCivilite());
//		//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		//		return dto;
//		TaArticleMapper mapper = new TaArticleMapper();
//		return mapper.mapEntityToDto(entity, null);
//	}
//
//	public List<TaArticleDTO> listEntityToListDTO(List<TaArticle> entity) {
//		List<TaArticleDTO> l = new ArrayList<TaArticleDTO>();
//
//		for (TaArticle taTCivilite : entity) {
//			l.add(entityToDTO(taTCivilite));
//		}
//
//		return l;
//	}
//
////	@RolesAllowed("admin")
//	public List<TaArticleDTO> selectAllDTO() {
//		System.out.println("List of TaArticleDTO EJB :");
//		ArrayList<TaArticleDTO> liste = new ArrayList<TaArticleDTO>();
//
//		List<TaArticle> projects = selectAll();
//		for(TaArticle project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}
//
//	public TaArticleDTO findByIdDTO(int id) throws FinderException {
//		return entityToDTO(findById(id));
//	}
//
//	public TaArticleDTO findByCodeDTO(String code) throws FinderException {
//		return entityToDTO(findByCode(code));
//	}
//
//	@Override
//	public void error(TaArticleDTO dto) {
//		throw new EJBException("Test erreur EJB");
//	}
//
//	@Override
//	public int selectCount() {
//		return dao.selectAll().size();
//		//return 0;
//	}
//
//	public void mergeDTO(TaArticleDTO dto) throws EJBException {
//		mergeDTO(dto, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "mergeDTOValidationContext")
//	public void mergeDTO(TaArticleDTO dto, String validationContext) throws EJBException {
//		try {
//			TaArticleMapper mapper = new TaArticleMapper();
//			TaArticle entity = null;
//			if(dto.getId()!=null) {
//				entity = dao.findById(dto.getId());
//				if(dto.getVersionObj()!=entity.getVersionObj()) {
//					throw new OptimisticLockException(entity,
//							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
//				} else {
//					entity = mapper.mapDtoToEntity(dto,entity);
//				}
//			}
//
//			//dao.merge(entity);
//			dao.detach(entity); //pour passer les controles
//			enregistrerMerge(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			//throw new CreateException(e.getMessage());
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	public void persistDTO(TaArticleDTO dto) throws CreateException {
//		persistDTO(dto, null);
//	}
//
//	@Override
//	@WebMethod(operationName = "persistDTOValidationContext")
//	public void persistDTO(TaArticleDTO dto, String validationContext) throws CreateException {
//		try {
//			TaArticleMapper mapper = new TaArticleMapper();
//			TaArticle entity = mapper.mapDtoToEntity(dto,null);
//			//dao.persist(entity);
//			enregistrerPersist(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CreateException(e.getMessage());
//		}
//	}
//
//	@Override
//	public void removeDTO(TaArticleDTO dto) throws RemoveException {
//		try {
//			TaArticleMapper mapper = new TaArticleMapper();
//			TaArticle entity = mapper.mapDtoToEntity(dto,null);
//			//dao.remove(entity);
//			supprimer(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoveException(e.getMessage());
//		}
//	}
//
//	@Override
//	protected TaArticle refresh(TaArticle persistentInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityValidationContext")
//	public void validateEntity(TaArticle value, String validationContext) /*throws ExceptLgr*/ {
//		try {
//			if(validationContext==null) validationContext="";
//			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
//			//dao.validate(value); //validation automatique via la JSR bean validation
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityPropertyValidationContext")
//	public void validateEntityProperty(TaArticle value, String propertyName, String validationContext) {
//		try {
//			if(validationContext==null) validationContext="";
//			validate(value, propertyName, validationContext);
//			//dao.validateField(value,propertyName);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOValidationContext")
//	public void validateDTO(TaArticleDTO dto, String validationContext) {
//		try {
//			TaArticleMapper mapper = new TaArticleMapper();
//			TaArticle entity = mapper.mapDtoToEntity(dto,null);
//			validateEntity(entity,validationContext);
//
//			//validation automatique via la JSR bean validation
//			//			BeanValidator<TaArticleDTO> validator = new BeanValidator<TaArticleDTO>(TaArticleDTO.class);
//			//			validator.validate(dto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOPropertyValidationContext")
//	public void validateDTOProperty(TaArticleDTO dto, String propertyName, String validationContext) throws BusinessValidationException {
//		try {
//			//			TaArticleMapper mapper = new TaArticleMapper();
//			//			TaArticle entity = mapper.mapDtoToEntity(dto,null);
//			//			validateEntityProperty(entity,propertyName,validationContext);
//
//			if(validationContext==null) validationContext="";
//			//			validateDTO(dto, propertyName, modeObjetEcranServeur, validationContext);
//			validateDTO(dto, propertyName, validationContext);
//
//			//validation automatique via la JSR bean validation
//			//			BeanValidator<TaArticleDTO> validator = new BeanValidator<TaArticleDTO>(TaArticleDTO.class);
//			//			validator.validateField(dto,propertyName);
//		} catch (Exception e) {
//			e.printStackTrace();
//			//throw new EJBException(e.getMessage());
//			throw new BusinessValidationException(e.getMessage());
//		}
//
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTO")
//	public void validateDTO(TaArticleDTO dto) {
//		validateDTO(dto,null);
//
//	}
//
//	@Override
//	@WebMethod(operationName = "validateDTOProperty")
//	public void validateDTOProperty(TaArticleDTO dto, String propertyName) throws BusinessValidationException {
//		validateDTOProperty(dto,propertyName,null);
//
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntity")
//	public void validateEntity(TaArticle value) {
//		validateEntity(value,null);
//	}
//
//	@Override
//	@WebMethod(operationName = "validateEntityProperty")
//	public void validateEntityProperty(TaArticle value, String propertyName) {
//		validateEntityProperty(value,propertyName,null);
//
//	}
//
//
//	public String genereCode( Map<String, String> params) {
//		//return dao.genereCode();
//		try {
//			return gencode.genereCodeJPA(TaArticle.class.getSimpleName(),params);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "NOUVEAU CODE";
//	}
//	public TaRapportUnite getRapport( String CodeArticle ) {
//		return dao.getRapport(CodeArticle);
//	}
//	
//	public void annuleCode(String code) {
//		try {
//			
//			gencode.annulerCodeGenere(gencode.findByCode(TaArticle.class.getSimpleName()),code);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void verrouilleCode(String code) {
//		try {
//			gencode.rentreCodeGenere(gencode.findByCode(TaArticle.class.getSimpleName()),code, sessionInfo.getSessionID());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//	/**
//	 * Creation d'un objet "TaRTaTitreTransport" pour l'objet "TaArticle" gerer par cet ecran
//	 * dans le cas ou la propriete TaRTaTitreTransport de ce dernier est nulle.
//	 */
//	public TaArticle initTaRTaTitreTransport(TaArticle taArticle) {
//		if(taArticle.getTaRTaTitreTransport()==null ) {
//			//initialisation
//			TaRTaTitreTransport r = null;
//			if(taArticle.getIdArticle()!=0) {
//				try {
//					r = daoRTitreTransportService.findByIdArticle(taArticle.getIdArticle());
//				}  catch (FinderException e) {
//					logger.error("",e);
//				}
//
//			}
//			if (r==null) 
//				r = new TaRTaTitreTransport();
//			r.setTaArticle(taArticle);
//			taArticle.setTaRTaTitreTransport(r);
//
//			if(taArticle.getTaUnite1()!=null) {
//				taArticle.getTaRTaTitreTransport().setTaUnite(taArticle.getTaUnite1());
//			}
//		}
//		return taArticle;
//	}
//
//
//	///**
//	// * Creation d'un objet "TaPrix" pour l'objet "TaArticle" gerer par cet ecran
//	// * dans le cas ou la propriete taPrix de ce dernier est nulle.
//	// */
//	//public void initPrixArticle(TaArticle taArticle,TaArticleDTO dto) {
//	//	if(taArticle.getTaPrix()==null) {
//	//		//initialisation du prix
//	//		TaPrix p = new TaPrix();
//	//		if(uniteEstRempli(dto)){
//	//			p.setPrixPrix(new BigDecimal(0));
//	//			p.setPrixttcPrix(new BigDecimal(0));				
//	//		}
//	//		p.setTaArticle(taArticle);
//	//		taArticle.setTaPrix(p);
//	//		taArticle.getTaPrixes().add(p);
//	//	}
//	//}
//
//
//	private boolean uniteEstRempli(TaArticleDTO dto) {
//		boolean retour=false;
//		if(dto.getCodeUnite()!=null && !dto.getCodeUnite().equals(""))return true;
//		if(dto.getPrixPrix()!=null && dto.getPrixPrix().signum()!=0)return true;
//		if(dto.getPrixttcPrix()!=null && dto.getPrixttcPrix().signum()!=0)return true;
//		//		if(!vue.getTfCODE_UNITE().getText().equals(""))return true;
//		//		if(!vue.getTfPRIX_PRIX().getText().equals(""))return true;
//		//		if(!vue.getTfPRIXTTC_PRIX().getText().equals(""))return true;		
//		return retour;
//	}
//
//
//
//	public boolean initEtatRapportUnite(boolean enModif, TaArticle taArticle, TaArticleDTO dto) {
//		boolean enabled = false;
//		if(taArticle.getTaPrix()!=null) {
//			if(taArticle.getTaUnite1()!=null){
//				enabled=true;
//			}
//		}
//		if(!enabled && enModif){
//			if(taArticle.getTaRapportUnite()!=null){
//				taArticle.removeRapportUnite(taArticle.getTaRapportUnite());
//				taArticle.setTaRapportUnite(null);
//			}			
//			dto.setCodeUnite2("");
//			dto.setRapport(null);
//			dto.setNbDecimal(null);
//		}
//		return enabled;
//	}
//
//
//	public boolean rapportUniteEstRempli(TaArticleDTO dto) {
//		boolean retour=false;
//		if(dto.getRapport()!=null && dto.getRapport().signum()!=0)return true;
//		if(dto.getNbDecimal()!=null && dto.getNbDecimal()!=0)return true;		
//		//		if(!vue.getTfRAPPORT().getText().equals(""))return true;
//		//		if(!vue.getTfDECIMAL().getText().equals(""))return true;		
//		return retour;
//	}
//
//
//
//	public boolean crdEstRempli(TaArticleDTO dto) {
//		boolean retour=false;
//		if(dto.getCodeTitreTransport()!=null && !dto.getCodeTitreTransport().equals(""))return true;
//		//	if(vue.getTfTitreTransportU1()!=null && !vue.getTfTitreTransportU1().getText().equals(""))return true;
//		return retour;
//	}
//
//	
//	
//	public List<TaArticle>  findByMPremiere(Boolean mPremiere) {
//		return dao.findByMPremiere(mPremiere);
//	}
//	
//	public List<TaArticle>  findByPFini(Boolean pFini) {
//		return dao.findByPFini(pFini);
//	}
//
//	/**
//	 * Creation d'un objet "TaPrix" pour l'objet "TaArticle" gerer par cet ecran
//	 * dans le cas ou la propriete taPrix de ce dernier est nulle.
//	 */
//	public TaArticle initPrixArticle(TaArticle taArticle,TaArticleDTO dto) {
//		if(taArticle.getTaPrix()==null) {
//			//initialisation du prix
//			TaPrix p = new TaPrix();
//			if(uniteEstRempli(dto)){
//				p.setPrixPrix(new BigDecimal(0));
//				p.setPrixttcPrix(new BigDecimal(0));				
//			}
//			p.setTaArticle(taArticle);
//			taArticle.setTaPrix(p);
//			taArticle.getTaPrixes().add(p);
//		}
//		return taArticle;
//	}
//
//
//
//	public File exportToCSV(List<TaArticleDTO> l) {
//
//		SchemaResolver sr = new SchemaResolver();
//		String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("articles.csv");
//		try {
//			File f = new File(localPath);
//			FileWriter writer = new FileWriter(f);
//
//			CSVWriter csvWriter = new CSVWriter(writer, ';', '\'');
//
////			List<String[]> data = toStringArray(l);
////			csvWriter.writeAll(data);
////			csvWriter.close();
//
//			BeanToCsv bc = new BeanToCsv();
//		    ColumnPositionMappingStrategy<TaArticleDTO> mappingStrategy = new ColumnPositionMappingStrategy<TaArticleDTO>();
//		    mappingStrategy.setType(TaArticleDTO.class);
//		    String[] columns = new String[]{
//				"id",
//			    "codeArticle",
//				"libellecArticle",
//				"codeFamille",
//				"codeUniteReference",
//				"codeUnite",
//				"codeUnite2",
//				"matierePremiere",
//				"produitFini",
//				"codeTva",
//				"tauxTva",
//				"actif",				
//				"gestionLot",
//				"codeBarre"
//			};
//
//		    mappingStrategy.setColumnMapping(columns);
//		    StatefulBeanToCsvBuilder<TaArticleDTO> builder = new StatefulBeanToCsvBuilder<TaArticleDTO>(writer);
//		    StatefulBeanToCsv<TaArticleDTO> beanWriter = builder.withMappingStrategy(mappingStrategy).build();
//		    
//		    beanWriter.write(l);
//		    writer.close();
//		    
////		    bc.write(mappingStrategy,csvWriter,l);
////		    csvWriter.close();
//		    
//			return f;
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private List<String[]> toStringArray(List<TaArticleDTO> l) {
//		List<String[]> records = new ArrayList<String[]>();
//
//		// entete
//		//records.add(new String[] { "Id", "Nom" });
//
//		Iterator<TaArticleDTO> it = l.iterator();
//		while (it.hasNext()) {
//			TaArticleDTO t = it.next();
//			records.add(new String[] { 
//					LibConversion.integerToString(t.getId()),
//					t.getCodeArticle() 
//			});
//		}
//		return records;
//	}
//	
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle){
//	return daoFacture.findChiffreAffaireTotalParCodeArticleDTO(dateDebut, dateFin, codeArticle);
//	}
//
//	/*
//	 ************************************************************************************************************************************************
//	 */
//	
	



}
