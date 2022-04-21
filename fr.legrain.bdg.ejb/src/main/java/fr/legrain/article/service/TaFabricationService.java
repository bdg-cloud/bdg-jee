package fr.legrain.article.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
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

import fr.legrain.article.dao.IFabricationDAO;
import fr.legrain.article.dao.ILotDAO;
import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.model.mapping.mapper.TaFabricationMapper;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.model.TaMouvementStock;


/**
 * Session Bean implementation class TaFabricationBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFabricationService extends AbstractApplicationDAOServer<TaFabrication, TaFabricationDTO> implements ITaFabricationServiceRemote {

	static Logger logger = Logger.getLogger(TaFabricationService.class);

	@Inject private IFabricationDAO dao;
	@Inject private ILotDAO daoLot;
	@Inject private	SessionInfo sessionInfo;
	
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaMouvementStockServiceRemote taMouvementStockService;
	@EJB private ITaGenCodeExServiceRemote gencode;
//	private List<TaLFabrication> listeLFabASupprimer = new LinkedList<TaLFabrication>();
	
	/**
	 * Default constructor. 
	 */
	public TaFabricationService() {
		super(TaFabrication.class,TaFabricationDTO.class);
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_FABRICATION_FICHIER;
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaFabrication.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaFabrication.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaFabrication.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//-ajouté par Dima - Début
	public List<TaFabrication> findWithCodeArticle(String codeArticle, Date dateDebut, Date dateFin) {
		return dao.findWithCodeArticle(codeArticle, dateDebut, dateFin);
	}
	public List<TaLFabricationMP> findWithMatPremNumLot(String numLot, Date dateDebut, Date dateFin) {
		return dao.findWithMatPremNumLot(numLot, dateDebut, dateFin);
	}
	public List<TaLFabricationMP> findWithMatPremLibelle(String nomProdtuit, Date dateDebut, Date dateFin) {
		return dao.findWithMatPremLibelle(nomProdtuit, dateDebut, dateFin);
	}
	public List<TaLFabricationPF> findWithProdFinNumLot(String numLot, Date dateDebut, Date dateFin) {
		return dao.findWithProdFinNumLot(numLot, dateDebut, dateFin);
	}
	public List<TaLFabricationPF> findWithProdFinLibelle(String nomProdtuit, Date dateDebut, Date dateFin) {
		/*List<TaLFabrication> lf = dao.findWithProdFinLibelle(nomProdtuit, dateDebut, dateFin);
		List<TaLFabrication> res = new ArrayList<TaLFabrication>();
		for (TaLFabrication taLFabrication : lf) {
			taLFabrication.getTaDocument().getCodeDocument();
			res.add(taLFabrication);			
		}
		return res;*/
		
		return dao.findWithProdFinLibelle(nomProdtuit, dateDebut, dateFin);
	}
	public List<TaFabrication> findWithNumFab(String codeFabrication, Date dateDebut, Date dateFin) {
		return dao.findWithNumFab(codeFabrication, dateDebut, dateFin);
	}
	
	public List<TaFabrication> findWithCodeArticleNoDate(String codeArticle) {
		return dao.findWithCodeArticleNoDate(codeArticle);
	}
	public List<TaFabrication> findWithMatPremNumLotNoDate(String numLot) {
		return dao.findWithMatPremNumLotNoDate(numLot);
	}
	public List<TaFabrication> findWithMatPremLibelleNoDate(String nomProdtuit) {
		return dao.findWithMatPremLibelleNoDate(nomProdtuit);
	}
	public List<TaFabrication> findWithProdFinNumLotNoDate(String numLot) {
		return dao.findWithProdFinNumLotNoDate(numLot);
	}
	public List<TaFabrication> findWithProdFinLibelleNoDate(String nomProdtuit) {
		return dao.findWithProdFinLibelleNoDate(nomProdtuit);
	}
	public List<TaFabrication> findWithNumFabNoDate(String codeFabrication) {
		return dao.findWithNumFabNoDate(codeFabrication);
	}
	//-ajouté par Dima - Fin
	
	
	public List<TaFabricationDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaFabricationDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaFabricationDTO> findByDateLight(Date debut, Date fin) {
		return dao.findByDateLight(debut,fin);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFabrication transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFabrication transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFabrication persistentInstance) throws RemoveException {
		TaFabrication objInitial = persistentInstance;
		 
		Map<Integer, TaLot> listeLotSuppr = new HashMap<Integer, TaLot>();
//		try {
//			if(persistentInstance.getIdDocument()!=0)
//				objInitial=findByIDFetch(persistentInstance.getIdDocument());
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//		}
////			
//			for (TaLFabricationPF lp : objInitial.getLignesProduits()) {
//				if(lp.getTaLot()!=null && (!lp.getTaLot().getVirtuel() || lp.getTaLot().getVirtuelUnique())) {
//					listeLotSuppr.put(lp.getTaLot().getIdLot(), lp.getTaLot());
//				}
//			}
			
			//dao.remove(findById(persistentInstance.getIdDocument()));
			dao.remove(persistentInstance);
//			for (TaLFabricationPF l : persistentInstance.getLignesProduits()) {
//				if(l.getTaLot()!=null && (!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique())) {
//					if(listeLotSuppr.containsKey(l.getTaLot().getIdLot()))
//						listeLotSuppr.remove(l.getTaLot().getIdLot());
//					taLotService.remove(l.getTaLot());	
//				}
//				
//			}
//			for (TaLot l : listeLotSuppr.values()) {
//				taLotService.remove(l);
//			}

//			taLotService.suppression_lot_non_utilise();
//		} catch(Exception e) {
//			throw e;
//		}
	}
	
	public TaFabrication mergeAndFindById(TaFabrication detachedInstance, String validationContext) {
		TaFabrication obj = merge(detachedInstance,validationContext);
		try {
			obj = findById(obj.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public TaFabrication merge(TaFabrication detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFabrication merge(TaFabrication detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		
		
//		TaFabrication objInitial = detachedInstance;
////		Map<Integer, TaLot> listeLotSuppr = new HashMap<Integer, TaLot>();
//		List<Integer> listeLotSuppr = new LinkedList<>();
//		List<Integer> listeMouvSupp = new LinkedList<>();
//		int index=-1;
//		try {
//			if(detachedInstance.getIdDocument()!=0) {
//				objInitial=findByIDFetch(detachedInstance.getIdDocument());
//				//pour chaque ligne trouvé dans ancien, je regarde si existe dans modifié
//				for (TaLFabricationPF l : objInitial.getLignesProduits()) {
//					if(l.getTaLot()!=null) {//s'il y avait un lot
//						index=detachedInstance.getLignesProduits().indexOf(l);
//						if(index>=0) {//si ligne existe toujours
//							//alors on recherche si le lot est le même
//							TaLFabricationPF encours=detachedInstance.getLignesProduits().get(index);
//							if(encours.getTaLot()!=null && !encours.getTaLot().equals(l.getTaLot()) && (!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique())) {
//								detachedInstance.getListeLotSuppr().add(l.getTaLot().getIdLot());
////								l.setTaLot(null);
////								if(l.getTaMouvementStock()!=null)listeMouvSupp.add(l.getTaMouvementStock().getIdMouvementStock());
//							}
//						}else {//si ligne n'existe plus, il faut enlever le lot si
////							if(!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique()) {
////								for (Integer i : detachedInstance.getListeLotSuppr()) {
////									listeLotSuppr.add(i);
////								}
////							}
////								listeLotSuppr.add(l.getTaLot().getIdLot());
////								if(l.getTaMouvementStock()!=null)listeMouvSupp.add(l.getTaMouvementStock().getIdMouvementStock());								
////							}
//						}
//					}
//				}
//
//			}
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//		}//		
		
//		for (TaLFabricationPF l : detachedInstance.getListeLFabPFSuppr()) {
//			try {
//				if(!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique()) 
//					taLotService.remove(l.getTaLot());
//			} catch (RemoveException  e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		for (Integer l : detachedInstance.getListeLotSuppr()) {
//			try {
//				taLotService.remove(taLotService.findById(l));
//			} catch (RemoveException | FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		for (ITaLFabrication l : detachedInstance.getLignes()) {
			//si lot non virtuel ou virtuel mais unique, alors on enregistre le lot manuellemnt puisqu'il n'y a plus de cascade
			if(l.getTaLot()!=null && (!l.getTaLot().getVirtuel() || l.getTaLot().getVirtuelUnique()||l.getTaLot().getIdLot()==0)) {
				try {
				TaLot lot=taLotService.merge(l.getTaLot());
				lot=taLotService.findById(lot.getIdLot());

					l.setTaLot(lot);
					if(l.getTaMouvementStock()!=null)l.getTaMouvementStock().setTaLot(lot);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		detachedInstance=dao.merge(detachedInstance);

		
//		for (Integer l : listeMouvSupp) {
//			try {
//				TaMouvementStock m =taMouvementStockService.findById(l);
//				m.setTaLot(null);
//				taMouvementStockService.remove(m);
//			} catch (FinderException | RemoveException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		



			
		this.annuleCode(detachedInstance.getCodeDocument());
		for (ITaLFabrication ligne : detachedInstance.getLignes()) {
			if(ligne.getTaLot()!=null)
				taLotService.annuleCode(ligne.getTaLot().getNumLot());
		}
		
//		taLotService.suppression_lot_non_utilise();
		return detachedInstance;
	}

	public TaFabrication findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFabrication findByIdWithList(int id) throws FinderException {
		TaFabrication fab =  dao.findById(id);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}

	public TaFabrication findByCodeWithList(String code)   {
		TaFabrication fab =  dao.findByCode(code);
//		fab.getListMatierePremieres();
//		fab.getListProduits();
		return fab;
	}
	public TaFabrication findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaFabrication> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFabricationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFabricationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFabrication> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFabricationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFabricationDTO entityToDTO(TaFabrication entity)  {
//		TaFabricationDTO dto = new TaFabricationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;

		TaFabricationMapper mapper = new TaFabricationMapper();
		TaFabricationDTO dto = new TaFabricationDTO();
		dto = mapper.mapEntityToDto(entity, null);
		
		
		
		return dto;
	}

	public List<TaFabricationDTO> listEntityToListDTO(List<TaFabrication> entity) {
		List<TaFabricationDTO> l = new ArrayList<TaFabricationDTO>();

		for (TaFabrication taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFabricationDTO> selectAllDTO() {
		System.out.println("List of TaFabricationDTO EJB :");
		ArrayList<TaFabricationDTO> liste = new ArrayList<TaFabricationDTO>();

		List<TaFabrication> projects = selectAll();
		for(TaFabrication project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFabricationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFabricationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFabricationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFabricationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFabricationDTO dto, String validationContext) throws EJBException {
		try {
			TaFabricationMapper mapper = new TaFabricationMapper();
			TaFabrication entity = null;
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
	
	public void persistDTO(TaFabricationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFabricationDTO dto, String validationContext) throws CreateException {
		try {
			TaFabricationMapper mapper = new TaFabricationMapper();
			TaFabrication entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFabricationDTO dto) throws RemoveException {
		try {
			TaFabricationMapper mapper = new TaFabricationMapper();
			TaFabrication entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFabrication refresh(TaFabrication persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFabrication value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaFabrication value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFabricationDTO dto, String validationContext) {
		try {
			TaFabricationMapper mapper = new TaFabricationMapper();
			TaFabrication entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFabricationDTO> validator = new BeanValidator<TaFabricationDTO>(TaFabricationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFabricationDTO dto, String propertyName, String validationContext) {
		try {
			TaFabricationMapper mapper = new TaFabricationMapper();
			TaFabrication entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFabricationDTO> validator = new BeanValidator<TaFabricationDTO>(TaFabricationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFabricationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFabricationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFabrication value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFabrication value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		return generePDF(idDoc,editionDefaut, mapParametreEdition,null,null);
	}

	@Override
	public String generePDF(int idDoc, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		try {
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("fabrication.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaFabrication doc = findById(idDoc);
			doc.calculeTvaEtTotaux();
			

			mapEdition.put("fabrication",doc );

			mapEdition.put("taInfoEntreprise", entrepriseService.findInstance());

			mapEdition.put("lignes", doc.getLignes());

			
			if(mapParametreEdition == null) {
				mapParametreEdition = new HashMap<String,Object>();
			}
			List<TaPreferences> liste= taPreferencesService.findByGroupe("Fabrication");
			if(!mapParametreEdition.containsKey("preferences")) mapParametreEdition.put("preferences", liste);
			if(!mapParametreEdition.containsKey("gestionLot")) mapParametreEdition.put("gestionLot", false);
			if(!mapParametreEdition.containsKey("mode")) mapParametreEdition.put("mode", "");
//			if(!mapParametreEdition.containsKey("Theme")) mapParametreEdition.put("Theme", "defaultTheme");
//			if(!mapParametreEdition.containsKey("Librairie")) mapParametreEdition.put("Librairie", "bdgFactTheme1");
			mapEdition.put("param", mapParametreEdition);

			//sessionMap.put("edition", mapEdition);
			
			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF
			
			HashMap<String,Object> hm = new HashMap<>();

			hm.put("edition", mapEdition);
			
			BirtUtil.setAppContextEdition(hm);
			BirtUtil.startReportEngine();
			
			BirtUtil.renderReportToFile(
					//bdgProperties.urlDemoHttps()+modeleEdition, //"https://dev.demo.promethee.biz:8443/reports/documents/facture/FicheFacture.rptdesign",
					modeleEdition,
					localPath, 
					new HashMap<>(), 
					BirtUtil.PDF_FORMAT,
					listResourcesPath,
					theme);
			 
			 return localPath;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public TaFabrication findAvecResultatConformites(int idBr) {
		// TODO Auto-generated method stub
		return dao.findAvecResultatConformites(idBr);
	}

	@Override
	public String generePDF(int idDoc, Map<String, Object> mapParametreEdition, List<String> listResourcesPath,
			String theme, TaActionEdition action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaFabrication findByCodeFetch(String code) throws FinderException {
		// TODO Auto-generated method stub
		return findByCode(code);
	}

	@Override
	public TaFabrication findByIDFetch(int id) throws FinderException {
		// TODO Auto-generated method stub
		return findById(id);
	}

	@Override
	public void remove(TaFabrication persistentInstance, boolean recharger) throws RemoveException {
		remove(persistentInstance);		
	}

	

}
