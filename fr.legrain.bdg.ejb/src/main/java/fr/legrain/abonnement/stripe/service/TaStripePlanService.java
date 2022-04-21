package fr.legrain.abonnement.stripe.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.abonnement.stripe.dao.IStripePlanDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripePlanMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripePlanBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripePlanService extends AbstractApplicationDAOServer<TaStripePlan, TaStripePlanDTO> implements ITaStripePlanServiceRemote {

	static Logger logger = Logger.getLogger(TaStripePlanService.class);

	@Inject private IStripePlanDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripePlanService() {
		super(TaStripePlan.class,TaStripePlanDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripePlan a";
	
	public List<TaStripePlanDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripePlanDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaStripePlan> findByIdProduct(int idStripePtoduct) {
		return dao.findByIdProduct(idStripePtoduct);
	}
	public List<TaStripePlanDTO> findByIdProductDTO(int idStripePtoduct) {
		return dao.findByIdProductDTO(idStripePtoduct);
	}
	public List<TaStripePlanDTO> findByIdArticleDTO(int idArticle){
		return dao.findByIdArticleDTO(idArticle);
	}
	public List<TaStripePlanDTO> findByIdProductDTOAndByIdFrequence(Integer idStripePtoduct, Integer idFrequence){
		return dao.findByIdProductDTOAndByIdFrequence( idStripePtoduct,  idFrequence);
	}
	public List<TaStripePlanDTO> findByIdArticleDTOAndByIdFrequence(Integer idArticle, Integer idFrequence){
		return dao.findByIdArticleDTOAndByIdFrequence(idArticle, idFrequence);
	}
	public List<TaStripePlanDTO> findByIdArticleDTOAndByNbMois(Integer idArticle, Integer nbMois) {
		return dao.findByIdArticleDTOAndByNbMois(idArticle, nbMois);
	}
	
//	public TaStripePlan findByStripeSubscriptionItem(TaStripeSubscriptionItem taStripeSubscriptionItem) {
//		return dao.findByStripeSubscriptionItem(taStripeSubscriptionItem);
//	}
	
	public TaStripePlan findByLigneAbo(TaLAbonnement ligneAbo) {
		return dao.findByLigneAbo(ligneAbo);
	}
	
	public TaStripePlan findByIdLigneAbo(Integer idLigne) {
		return dao.findByIdLigneAbo(idLigne);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripePlan transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripePlan transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripePlan persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripePlan()));
	}
	
	public TaStripePlan merge(TaStripePlan detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripePlan merge(TaStripePlan detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripePlan findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripePlan findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripePlan> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripePlanDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripePlanDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripePlan> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripePlanDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripePlanDTO entityToDTO(TaStripePlan entity) {
//		TaStripePlanDTO dto = new TaStripePlanDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripePlanMapper mapper = new TaStripePlanMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripePlanDTO> listEntityToListDTO(List<TaStripePlan> entity) {
		List<TaStripePlanDTO> l = new ArrayList<TaStripePlanDTO>();

		for (TaStripePlan taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripePlanDTO> selectAllDTO() {
		System.out.println("List of TaStripePlanDTO EJB :");
		ArrayList<TaStripePlanDTO> liste = new ArrayList<TaStripePlanDTO>();

		List<TaStripePlan> projects = selectAll();
		for(TaStripePlan project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripePlanDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripePlanDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripePlanDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripePlanDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripePlanDTO dto, String validationContext) throws EJBException {
		try {
			TaStripePlanMapper mapper = new TaStripePlanMapper();
			TaStripePlan entity = null;
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
	
	public void persistDTO(TaStripePlanDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripePlanDTO dto, String validationContext) throws CreateException {
		try {
			TaStripePlanMapper mapper = new TaStripePlanMapper();
			TaStripePlan entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripePlanDTO dto) throws RemoveException {
		try {
			TaStripePlanMapper mapper = new TaStripePlanMapper();
			TaStripePlan entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripePlan refresh(TaStripePlan persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripePlan value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripePlan value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripePlanDTO dto, String validationContext) {
		try {
			TaStripePlanMapper mapper = new TaStripePlanMapper();
			TaStripePlan entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripePlanDTO> validator = new BeanValidator<TaStripePlanDTO>(TaStripePlanDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripePlanDTO dto, String propertyName, String validationContext) {
		try {
			TaStripePlanMapper mapper = new TaStripePlanMapper();
			TaStripePlan entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripePlanDTO> validator = new BeanValidator<TaStripePlanDTO>(TaStripePlanDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripePlanDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripePlanDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripePlan value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripePlan value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaStripePlan findByNickname(String nickname) {
		// TODO Auto-generated method stub
		return dao.findByNickname(nickname);
	}

}
