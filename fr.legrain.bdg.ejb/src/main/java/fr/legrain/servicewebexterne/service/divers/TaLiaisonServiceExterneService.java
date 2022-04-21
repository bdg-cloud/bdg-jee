package fr.legrain.servicewebexterne.service.divers;

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

import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.paiement.service.remote.ITaLogPaiementInternetServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneShippingboServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;
import fr.legrain.servicewebexterne.dao.ITaLiaisonServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dao.ITaLigneShippingboDAO;
import fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO;
import fr.legrain.servicewebexterne.mapper.TaLiaisonServiceExterneMapper;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLiaisonServiceExterneService extends AbstractApplicationDAOServer<TaLiaisonServiceExterne, TaLiaisonServiceExterneDTO> implements ITaLiaisonServiceExterneServiceRemote {

	static Logger logger = Logger.getLogger(TaLiaisonServiceExterneService.class);

	@Inject private ITaLiaisonServiceWebExterneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLiaisonServiceExterneService() {
		super(TaLiaisonServiceExterne.class,TaLiaisonServiceExterneDTO.class);
	}
	
	public void persist(TaLiaisonServiceExterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLiaisonServiceExterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLiaisonServiceExterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLiaisonServiceExterne()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLiaisonServiceExterne merge(TaLiaisonServiceExterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLiaisonServiceExterne merge(TaLiaisonServiceExterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLiaisonServiceExterne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLiaisonServiceExterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLiaisonServiceExterne> selectAll() {
		return dao.selectAll();
	}
	
	public TaLiaisonServiceExterne findByIdEntityByTypeEntiteByIdServiceWebExterne(Integer idEntite, String typeEntite, Integer idServiceWebExterne) {
		return dao.findByIdEntityByTypeEntiteByIdServiceWebExterne( idEntite,  typeEntite,  idServiceWebExterne);
	}
	public List<TaLiaisonServiceExterne> findAllByIdTiers(Integer idEntite){
		return dao.findAllByIdTiers(idEntite);
	}
	public List<TaLiaisonServiceExterne> findAllByIdArticle(Integer idEntite){
		return dao.findAllByIdArticle(idEntite);
	}
	public TaLiaisonServiceExterne findByIdTiersAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne) {
		return dao.findByIdTiersAndByIdServiceWebExterne(idEntite, idServiceWebExterne);
	}
	public TaLiaisonServiceExterne findByIdTaTPaiementAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne) {
		return dao.findByIdTaTPaiementAndByIdServiceWebExterne(idEntite, idServiceWebExterne);
	}
	public TaLiaisonServiceExterne findByIdArticleAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne) {
		return dao.findByIdArticleAndByIdServiceWebExterne(idEntite, idServiceWebExterne);
	}
	public TaLiaisonServiceExterne findByRefArticleAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne) {
		return dao.findByRefArticleAndByIdServiceWebExterne(refEntite, idServiceWebExterne);
	}
	public TaLiaisonServiceExterne findByRefTaTPaiementAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne) {
		return dao.findByRefTaTPaiementAndByIdServiceWebExterne(refEntite, idServiceWebExterne);
	}
	public TaLiaisonServiceExterne findByRefTiersAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne) {
		return dao.findByRefTiersAndByIdServiceWebExterne(refEntite, idServiceWebExterne);
	} 
	public List<TaLiaisonServiceExterne> findAllByTypeEntiteAndByIdServiceWebExterne(String typeEntite, Integer idServiceWebExterne){
		return dao.findAllByTypeEntiteAndByIdServiceWebExterne( typeEntite,  idServiceWebExterne);
	}
	////////////////////////////DTO/////////////////////////////////////////////////////////////
	public List<TaLiaisonServiceExterneDTO> findAllByTypeEntiteAndByIdServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne){
		return dao.findAllByTypeEntiteAndByIdServiceWebExterneDTO(typeEntite, idServiceWebExterne);
	}
	public List<TaLiaisonServiceExterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLiaisonServiceExterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLiaisonServiceExterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLiaisonServiceExterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLiaisonServiceExterneDTO entityToDTO(TaLiaisonServiceExterne entity) {
//		TaLiaisonServiceExterneDTO dto = new TaLiaisonServiceExterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLiaisonServiceExterneDTO> listEntityToListDTO(List<TaLiaisonServiceExterne> entity) {
		List<TaLiaisonServiceExterneDTO> l = new ArrayList<TaLiaisonServiceExterneDTO>();

		for (TaLiaisonServiceExterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLiaisonServiceExterneDTO> selectAllDTO() {
		System.out.println("List of TaLiaisonServiceExterneDTO EJB :");
		ArrayList<TaLiaisonServiceExterneDTO> liste = new ArrayList<TaLiaisonServiceExterneDTO>();

		List<TaLiaisonServiceExterne> projects = selectAll();
		for(TaLiaisonServiceExterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLiaisonServiceExterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLiaisonServiceExterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLiaisonServiceExterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLiaisonServiceExterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLiaisonServiceExterneDTO dto, String validationContext) throws EJBException {
		try {
			TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
			TaLiaisonServiceExterne entity = null;
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
	
	public void persistDTO(TaLiaisonServiceExterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLiaisonServiceExterneDTO dto, String validationContext) throws CreateException {
		try {
			TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
			TaLiaisonServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLiaisonServiceExterneDTO dto) throws RemoveException {
		try {
			TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
			TaLiaisonServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLiaisonServiceExterne refresh(TaLiaisonServiceExterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLiaisonServiceExterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLiaisonServiceExterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLiaisonServiceExterneDTO dto, String validationContext) {
		try {
			TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
			TaLiaisonServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLiaisonServiceExterneDTO> validator = new BeanValidator<TaLiaisonServiceExterneDTO>(TaLiaisonServiceExterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLiaisonServiceExterneDTO dto, String propertyName, String validationContext) {
		try {
			TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
			TaLiaisonServiceExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLiaisonServiceExterneDTO> validator = new BeanValidator<TaLiaisonServiceExterneDTO>(TaLiaisonServiceExterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLiaisonServiceExterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLiaisonServiceExterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLiaisonServiceExterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLiaisonServiceExterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	
	
	
	
}
