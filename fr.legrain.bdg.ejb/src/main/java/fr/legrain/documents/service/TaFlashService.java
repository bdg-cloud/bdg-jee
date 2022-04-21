package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaFlashMapper;

import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFlashDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.documents.dao.IFlashDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;


/**
 * Session Bean implementation class TaFlashBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaFlashService extends AbstractApplicationDAOServer<TaFlash, TaFlashDTO> implements ITaFlashServiceRemote {

	static Logger logger = Logger.getLogger(TaFlashService.class);

	@Inject private IFlashDAO dao;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@Inject private	SessionInfo sessionInfo;
	/**
	 * Default constructor. 
	 */
	public TaFlashService() {
		super(TaFlash.class,TaFlashDTO.class);
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFlash transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFlash transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaFlash persistentInstance) throws RemoveException {				
		try {
		persistentInstance=findById(persistentInstance.getIdFlash());
//		List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigneFlash(persistentInstance);
		dao.remove(persistentInstance);
//		mergeTaFlashLieParLigneALigneFlash(listeLien);

//			dao.remove(findById(persistentInstance.getIdFlash()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaFlash merge(TaFlash detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFlash merge(TaFlash detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		
//		à mettre si gestion des états !!!!
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		if(etat!=null)detachedInstance.addREtat(etat);
		
		
//		List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigne(detachedInstance);
		detachedInstance=dao.merge(detachedInstance);		
//		mergeTaFlashLieParLigneALigne(listeLien);
		
		annuleCode(detachedInstance.getCodeFlash());
		return detachedInstance;
	}

	public TaFlash findById(int id) throws FinderException {
		TaFlash o= dao.findById(id);
		o=recupSetREtat(o);
		o= recupSetHistREtat(o);
		o=recupSetLigneALigne(o);
		o=recupSetREtatLigneDocuments(o);
		o=recupSetHistREtatLigneDocuments(o);
		return o;
	}

	public TaFlash findByCode(String code) throws FinderException {
		TaFlash o= dao.findByCode(code);
		o=recupSetREtat(o);
		o= recupSetHistREtat(o);
		o=recupSetLigneALigne(o);
		o=recupSetREtatLigneDocuments(o);
		o=recupSetHistREtatLigneDocuments(o);
		return o;
	}

//	@RolesAllowed("admin")
	public List<TaFlash> selectAll() {
		List<TaFlash> l=dao.selectAll();
		for (TaFlash o : l) {
			o=recupSetREtat(o);
			o= recupSetHistREtat(o);
			o=recupSetLigneALigne(o);
			o=recupSetREtatLigneDocuments(o);
			o=recupSetHistREtatLigneDocuments(o);
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFlashDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFlashDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFlash> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFlashDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFlashDTO entityToDTO(TaFlash entity) {
//		TaFlashDTO dto = new TaFlashDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaFlashMapper mapper = new TaFlashMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFlashDTO> listEntityToListDTO(List<TaFlash> entity) {
		List<TaFlashDTO> l = new ArrayList<TaFlashDTO>();

		for (TaFlash taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFlashDTO> selectAllDTO() {
		System.out.println("List of TaFlashDTO EJB :");
		ArrayList<TaFlashDTO> liste = new ArrayList<TaFlashDTO>();

		List<TaFlash> projects = selectAll();
		for(TaFlash project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFlashDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFlashDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFlashDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFlashDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFlashDTO dto, String validationContext) throws EJBException {
		try {
			TaFlashMapper mapper = new TaFlashMapper();
			TaFlash entity = null;
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
	
	public void persistDTO(TaFlashDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFlashDTO dto, String validationContext) throws CreateException {
		try {
			TaFlashMapper mapper = new TaFlashMapper();
			TaFlash entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFlashDTO dto) throws RemoveException {
		try {
			TaFlashMapper mapper = new TaFlashMapper();
			TaFlash entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFlash refresh(TaFlash persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFlash value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaFlash value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFlashDTO dto, String validationContext) {
		try {
			TaFlashMapper mapper = new TaFlashMapper();
			TaFlash entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFlashDTO> validator = new BeanValidator<TaFlashDTO>(TaFlashDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFlashDTO dto, String propertyName, String validationContext) {
		try {
			TaFlashMapper mapper = new TaFlashMapper();
			TaFlash entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFlashDTO> validator = new BeanValidator<TaFlashDTO>(TaFlashDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFlashDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFlashDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFlash value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFlash value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	
	@Override
	//	@RolesAllowed("admin")
	public String genereCode( Map<String, String> params) {
		try {
			return gencode.genereCodeJPA(TaFlash.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}

	public void annuleCode(String code) {
		try {

			gencode.annulerCodeGenere(gencode.findByCode(TaFlash.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaFlash.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//modifier l'état des lignes et du document en fonction des liens ligne à ligne
    protected void modifEtatLigne(TaFlash detachedInstance) {
////    	//récupération du set des LignesALignes qui est en lazy
//		detachedInstance= recupSetLigneALigne(dao.merge(detachedInstance));
		for (TaLFlash ligne : detachedInstance.getLignes()) {
					// traiter l'état de la ligne
								BigDecimal totalTransformee=BigDecimal.ZERO;
								String ligneBefore="";
								TaEtat etatLigneOrg = null;
								TaEtat etatLigneOrgBefore = null;

								for (TaLigneALigne l : ligne.getTaLigneALignes()) {
									if(l.getIdLigneSrc().equals(ligne.getIdLFlash())) {
										totalTransformee=totalTransformee.add(l.getQteRecue());
										}
								}



								if(ligne.getTaREtatLigneDocument()!=null)etatLigneOrgBefore=ligne.getTaREtatLigneDocument().getTaEtat();
								if(etatLigneOrgBefore!=null) {
									if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
										etatLigneOrg=taEtatService.documentAbandonne(null);
									}
									if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)) {
										etatLigneOrg=taEtatService.documentEncours(null);
									}
								}
								
								if(totalTransformee.compareTo(ligne.getQteLFlash())>=0)
									etatLigneOrg=taEtatService.documentTermine(null);
								else if(totalTransformee.signum()!=0) etatLigneOrg=taEtatService.documentPartiellementTransforme(null);
								
								if(etatLigneOrg==null) {//plus rien de transforme
									if(etatLigneOrgBefore!=null) {

										if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
											etatLigneOrg=taEtatService.documentAbandonne(null);
										}
										if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)) {
											etatLigneOrg=taEtatService.documentEncours(null);
										}		
									}
								}else //si tout transforme
									if(etatLigneOrg.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)){
									if(etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE) || etatLigneOrgBefore.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
										etatLigneOrg=taEtatService.documentTotalementTransforme(null);
									}
								}
								if(etatLigneOrg!=null) {
									ligne.addREtatLigneDoc(etatLigneOrg);
								}
							}
		}


    
    protected TaEtat changeEtatDocument(TaFlash detachedInstance) {
		boolean trouveNonTermine=false;
		boolean trouveAbandonne=false;
		boolean trouveTransformation=false;
//    	//récupération du set des LignesALignes qui est en lazy
//		detachedInstance= recupSetLigneALigne(detachedInstance);
		List<TaEtat> liste=new LinkedList<TaEtat>();
		TaEtat etatDocPossible = null;
		int rang=-1;
		int position=-1;
		
		for (TaLFlash l : detachedInstance.getLignes()) {
			if(!trouveNonTermine && (l.getTaREtatLigneDocument()==null ||(l.getTaREtatLigneDocument()!=null && l.getTaREtatLigneDocument().getTaEtat()!=null && l.getTaREtatLigneDocument().getTaEtat().getTaTEtat()!=null && l.getTaREtatLigneDocument().getTaEtat().getTaTEtat().getCodeTEtat().equalsIgnoreCase(TaEtat.ENCOURS))))
				trouveNonTermine=true;
			if(!trouveAbandonne && (l.getTaREtatLigneDocument()!=null && l.getTaREtatLigneDocument().getTaEtat()!=null && l.getTaREtatLigneDocument().getTaEtat().getIdentifiant()!=null && l.getTaREtatLigneDocument().getTaEtat().getIdentifiant().toLowerCase().contains(TaEtat.ABANDONNE.toLowerCase())))
				trouveAbandonne=true;
			if(l.getTaREtatLigneDocument()!=null && l.getTaREtatLigneDocument().getTaEtat()!=null)liste.add(l.getTaREtatLigneDocument().getTaEtat());

			for (TaLigneALigne ll : l.getTaLigneALignes()) {
				if(!trouveTransformation &&ll.getIdLigneSrc().equals(l.getIdLFlash()))
					trouveTransformation=true;
			}
		}
		for (TaEtat taEtat : liste) {
			if(taEtat.getPosition().compareTo(position)>0) {
				position=taEtat.getPosition();
				rang++;
			}
		}
		if(liste!=null && !liste.isEmpty())etatDocPossible=liste.get(rang);
		if(trouveNonTermine) {
			if(trouveAbandonne)etatDocPossible=taEtatService.documentPartiellementAbandonne(null);
			else if(trouveTransformation)etatDocPossible=taEtatService.documentPartiellementTransforme(null);
		}
		else if(trouveAbandonne)etatDocPossible=taEtatService.documentTerminePartiellementAbandonne(null);
//		lEtatDocPossible=taEtatService.findByIdentifiantAndTypeEtat(TaEtat.ETAT_DOCUMENT,null);
			return etatDocPossible;

//		if(trouveNonTermine&&trouveAbandonne)

	}

    public TaEtat etatLigneInsertion(TaFlash detachedInstance) {
    	TaEtat etat = rechercheEtatInitialDocument();
		if(detachedInstance.getTaREtat()!=null && detachedInstance.getTaREtat().getTaEtat()!=null && 
				detachedInstance.getTaREtat().getTaEtat().getPosition().compareTo(taEtatService.documentBrouillon(null).getPosition())>0) {
			etat=taEtatService.documentEncours(null);
		}

    	return etat;
    }

    
	public TaEtat rechercheEtatInitialDocument() {
		try {
			return taEtatService.documentEncours(null);
		} catch (Exception e) {
			return null;
		}

	}


	@Override
	public TaFlash findDocByLDoc(TaLFlash lDoc) {
		// TODO Auto-generated method stub
		TaFlash o= dao.findDocByLDoc(lDoc);
		o=recupSetREtat(o);
		o= recupSetHistREtat(o);
		o=recupSetLigneALigne(o);
		o=recupSetREtatLigneDocuments(o);
		o=recupSetHistREtatLigneDocuments(o);
		return o;
	}


	@Override
	public TaFlash mergeEtat(TaFlash detachedInstance) {
//		à mettre si gestion des états !!!!
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		if(etat!=null)detachedInstance.addREtat(etat);
		
		
		detachedInstance=dao.merge(detachedInstance);
		return detachedInstance;
	}
	
	public TaFlash findByIdFetch(int id) throws FinderException {
		TaFlash o= dao.findById(id);
		if(o!=null) {
			o=recupSetREtat(o);
			o= recupSetHistREtat(o);
		o=recupSetLigneALigne(o);
		o=recupSetREtatLigneDocuments(o);
		o=recupSetHistREtatLigneDocuments(o);
		}
		return o;
	}

	public TaFlash findByCodeFetch(String code) throws FinderException {
		TaFlash o= dao.findByCode(code);
		if(o!=null) {
			o=recupSetREtat(o);
			o= recupSetHistREtat(o);
		o=recupSetLigneALigne(o);
		o=recupSetREtatLigneDocuments(o);
		o=recupSetHistREtatLigneDocuments(o);
		}
		return o;
	}
}
