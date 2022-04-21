package fr.legrain.servicewebexterne.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaServiceWebExterneMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.dao.IServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaServiceWebExterneService extends AbstractApplicationDAOServer<TaServiceWebExterne, TaServiceWebExterneDTO> implements ITaServiceWebExterneServiceRemote {

	static Logger logger = Logger.getLogger(TaServiceWebExterneService.class);

	@Inject private IServiceWebExterneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaServiceWebExterneService() {
		super(TaServiceWebExterne.class,TaServiceWebExterneDTO.class);
	}
	
	public List<TaServiceWebExterneDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaServiceWebExterneDTO> findAllLightActif() {
		return dao.findAllLightActif();
	}
	
//	public List<TaServiceWebExterne> findAgendaUtilisateur(TaUtilisateur utilisateur) {
//		return dao.findAgendaUtilisateur(utilisateur);
//	}
	
	//	private String defaultJPQLQuery = "select a from TaServiceWebExterne a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaServiceWebExterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaServiceWebExterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaServiceWebExterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdServiceWebExterne()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaServiceWebExterne merge(TaServiceWebExterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaServiceWebExterne merge(TaServiceWebExterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaServiceWebExterne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaServiceWebExterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaServiceWebExterne> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaServiceWebExterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaServiceWebExterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaServiceWebExterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaServiceWebExterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaServiceWebExterneDTO entityToDTO(TaServiceWebExterne entity) {
//		TaServiceWebExterneDTO dto = new TaServiceWebExterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaServiceWebExterneMapper mapper = new TaServiceWebExterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaServiceWebExterneDTO> listEntityToListDTO(List<TaServiceWebExterne> entity) {
		List<TaServiceWebExterneDTO> l = new ArrayList<TaServiceWebExterneDTO>();

		for (TaServiceWebExterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaServiceWebExterneDTO> selectAllDTO() {
		System.out.println("List of TaServiceWebExterneDTO EJB :");
		ArrayList<TaServiceWebExterneDTO> liste = new ArrayList<TaServiceWebExterneDTO>();

		List<TaServiceWebExterne> projects = selectAll();
		for(TaServiceWebExterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaServiceWebExterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaServiceWebExterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaServiceWebExterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaServiceWebExterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaServiceWebExterneDTO dto, String validationContext) throws EJBException {
		try {
			TaServiceWebExterneMapper mapper = new TaServiceWebExterneMapper();
			TaServiceWebExterne entity = null;
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
	
	public void persistDTO(TaServiceWebExterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaServiceWebExterneDTO dto, String validationContext) throws CreateException {
		try {
			TaServiceWebExterneMapper mapper = new TaServiceWebExterneMapper();
			TaServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaServiceWebExterneDTO dto) throws RemoveException {
		try {
			TaServiceWebExterneMapper mapper = new TaServiceWebExterneMapper();
			TaServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaServiceWebExterne refresh(TaServiceWebExterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaServiceWebExterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaServiceWebExterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaServiceWebExterneDTO dto, String validationContext) {
		try {
			TaServiceWebExterneMapper mapper = new TaServiceWebExterneMapper();
			TaServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaServiceWebExterneDTO> validator = new BeanValidator<TaServiceWebExterneDTO>(TaServiceWebExterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaServiceWebExterneDTO dto, String propertyName, String validationContext) {
		try {
			TaServiceWebExterneMapper mapper = new TaServiceWebExterneMapper();
			TaServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaServiceWebExterneDTO> validator = new BeanValidator<TaServiceWebExterneDTO>(TaServiceWebExterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaServiceWebExterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaServiceWebExterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaServiceWebExterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaServiceWebExterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
