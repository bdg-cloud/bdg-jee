package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLAbonnementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.ILigneDocumentDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.documents.dao.ILAbonnementDAO;
import fr.legrain.documents.dao.ILDevisDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLAbonnementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLAbonnementService extends AbstractApplicationDAOServer<TaLAbonnement, TaLAbonnementDTO> implements ITaLAbonnementServiceRemote {

	static Logger logger = Logger.getLogger(TaLAbonnementService.class);

	@Inject private ILAbonnementDAO dao;
	
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;

	/**
	 * Default constructor. 
	 */
	public TaLAbonnementService() {
		super(TaLAbonnement.class,TaLAbonnementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLAbonnement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLAbonnement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLAbonnement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLAbonnement persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLAbonnement merge(TaLAbonnement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLAbonnement merge(TaLAbonnement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLAbonnement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLAbonnement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLAbonnement> selectAll() {
		return dao.selectAll();
	}
	
	public List<TaLAbonnementDTO> selectAllDTOAvecEtat(){
		return dao.selectAllDTOAvecEtat();
	}
	public List<TaLAbonnement> findAllByIdAbonnement(Integer id){
		return dao.findAllByIdAbonnement(id);
	}
	public TaLAbonnement findByIdLEcheance(Integer id) {
		return dao.findByIdLEcheance(id);
	}
	
	public TaLAbonnement findByIDFetch(Integer id) {
		TaLAbonnement l = dao.findById(id);
		l.getTaREtatLigneDocument().getTaEtat();
		l.getTaPlan().getTaArticle();
		return l;
	}
	
	public List<TaLAbonnement> findAllAnnule(){
		return dao.findAllAnnule();
	}
	
	public TaLAbonnement donneEtat(TaLAbonnement detachedInstance, TaEtat etat) {
		try {
			detachedInstance= findById(detachedInstance.getIdLDocument());
			detachedInstance.addREtatLigneDoc(etat);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return merge(detachedInstance, null);
	}
	
	
	public TaLAbonnement donneEtatSuspendu(TaLAbonnement detachedInstance) {
		try {
			TaEtat etat = taEtatService.findByCode("doc_suspendu");
			detachedInstance= findById(detachedInstance.getIdLDocument());
			detachedInstance.addREtatLigneDoc(etat);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return merge(detachedInstance, null);
	}
	public List<TaLAbonnement> findAllSansEtat() {
		return dao.findAllSansEtat();
		}
	public TaLAbonnement donneEtatAnnule(TaLAbonnement detachedInstance) {
		try {
			TaEtat etat = taEtatService.findByCode("doc_abandonne");
			detachedInstance= findById(detachedInstance.getIdLDocument());
			detachedInstance.addREtatLigneDoc(etat);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return merge(detachedInstance, null);
	}
	
	/**
	 * Annule une ligne d'abonnement (etat doc_abandonne) et supprime toutes ses lignes d'échéances en cours et suspendu
	 * @author yann
	 * @param TaLAbonnement ligne d'abonnement
	 */
	public void annuleLigne(TaLAbonnement ligneAbo) {
		if(ligneAbo != null ) {
			
			System.out.println("j'annule la ligne d'abonnement : ");
			System.out.println("  ID : "+ligneAbo.getIdLDocument()+" avec le libellé suivant : "+ligneAbo.getLibLDocument());
			
			try {
				ligneAbo = findById(ligneAbo.getIdLDocument());
				if(ligneAbo != null) {
					TaEtat etat = taEtatService.findByCode("doc_abandonne");
					donneEtat(ligneAbo, etat);
					
					//on cherche toutes les échéances en cours ou suspendu à supprimer pour cette ligne d'abonnement
					List<TaLEcheance> listeEchASupprimer = taLEcheanceService.rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement(ligneAbo.getIdLDocument());
					
					
					for (TaLEcheance taLEcheance : listeEchASupprimer) {
						try {
							taLEcheanceService.remove(taLEcheance);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				}
			} catch (FinderException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			

			
		}
	}
	
	public BigDecimal sumTtcLigneAboEnCoursEtSuspendu() {
		return dao.sumTtcLigneAboEnCoursEtSuspendu();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLAbonnementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLAbonnementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLAbonnement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLAbonnementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLAbonnementDTO entityToDTO(TaLAbonnement entity) {
//		TaLAbonnementDTO dto = new TaLAbonnementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLAbonnementMapper mapper = new TaLAbonnementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLAbonnementDTO> listEntityToListDTO(List<TaLAbonnement> entity) {
		List<TaLAbonnementDTO> l = new ArrayList<TaLAbonnementDTO>();

		for (TaLAbonnement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLAbonnementDTO> selectAllDTO() {
		System.out.println("List of TaLAbonnementDTO EJB :");
		ArrayList<TaLAbonnementDTO> liste = new ArrayList<TaLAbonnementDTO>();

		List<TaLAbonnement> projects = selectAll();
		for(TaLAbonnement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLAbonnementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLAbonnementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLAbonnementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLAbonnementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLAbonnementDTO dto, String validationContext) throws EJBException {
		try {
			TaLAbonnementMapper mapper = new TaLAbonnementMapper();
			TaLAbonnement entity = null;
			if(dto.getIdLDocument()!=null) {
				entity = dao.findById(dto.getIdLDocument());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLDocument()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLAbonnementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLAbonnementDTO dto, String validationContext) throws CreateException {
		try {
			TaLAbonnementMapper mapper = new TaLAbonnementMapper();
			TaLAbonnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLAbonnementDTO dto) throws RemoveException {
		try {
			TaLAbonnementMapper mapper = new TaLAbonnementMapper();
			TaLAbonnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLAbonnement refresh(TaLAbonnement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLAbonnement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLAbonnement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLAbonnementDTO dto, String validationContext) {
		try {
			TaLAbonnementMapper mapper = new TaLAbonnementMapper();
			TaLAbonnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLAbonnementDTO> validator = new BeanValidator<TaLAbonnementDTO>(TaLAbonnementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLAbonnementDTO dto, String propertyName, String validationContext) {
		try {
			TaLAbonnementMapper mapper = new TaLAbonnementMapper();
			TaLAbonnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLAbonnementDTO> validator = new BeanValidator<TaLAbonnementDTO>(TaLAbonnementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLAbonnementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLAbonnementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLAbonnement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLAbonnement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
