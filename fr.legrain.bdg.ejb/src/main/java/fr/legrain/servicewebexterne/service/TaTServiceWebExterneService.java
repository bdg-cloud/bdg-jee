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

import fr.legrain.bdg.model.mapping.mapper.TaTServiceWebExterneMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaTServiceWebExterneServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.dao.ITAuthentificationDAO;
import fr.legrain.servicewebexterne.dao.ITServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.tache.dao.IAgendaDAO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTServiceWebExterneService extends AbstractApplicationDAOServer<TaTServiceWebExterne, TaTServiceWebExterneDTO> implements ITaTServiceWebExterneServiceRemote {

	static Logger logger = Logger.getLogger(TaTServiceWebExterneService.class);

	@Inject private ITServiceWebExterneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTServiceWebExterneService() {
		super(TaTServiceWebExterne.class,TaTServiceWebExterneDTO.class);
	}
	
	public List<TaTServiceWebExterneDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
//	public List<TaTServiceWebExterne> findAgendaUtilisateur(TaUtilisateur utilisateur) {
//		return dao.findAgendaUtilisateur(utilisateur);
//	}
	
	//	private String defaultJPQLQuery = "select a from TaTServiceWebExterne a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTServiceWebExterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTServiceWebExterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTServiceWebExterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTServiceWebExterne()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTServiceWebExterne merge(TaTServiceWebExterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTServiceWebExterne merge(TaTServiceWebExterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTServiceWebExterne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTServiceWebExterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTServiceWebExterne> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTServiceWebExterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTServiceWebExterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTServiceWebExterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTServiceWebExterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTServiceWebExterneDTO entityToDTO(TaTServiceWebExterne entity) {
//		TaTServiceWebExterneDTO dto = new TaTServiceWebExterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTServiceWebExterneMapper mapper = new TaTServiceWebExterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTServiceWebExterneDTO> listEntityToListDTO(List<TaTServiceWebExterne> entity) {
		List<TaTServiceWebExterneDTO> l = new ArrayList<TaTServiceWebExterneDTO>();

		for (TaTServiceWebExterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTServiceWebExterneDTO> selectAllDTO() {
		System.out.println("List of TaTServiceWebExterneDTO EJB :");
		ArrayList<TaTServiceWebExterneDTO> liste = new ArrayList<TaTServiceWebExterneDTO>();

		List<TaTServiceWebExterne> projects = selectAll();
		for(TaTServiceWebExterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTServiceWebExterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTServiceWebExterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTServiceWebExterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTServiceWebExterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTServiceWebExterneDTO dto, String validationContext) throws EJBException {
		try {
			TaTServiceWebExterneMapper mapper = new TaTServiceWebExterneMapper();
			TaTServiceWebExterne entity = null;
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
	
	public void persistDTO(TaTServiceWebExterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTServiceWebExterneDTO dto, String validationContext) throws CreateException {
		try {
			TaTServiceWebExterneMapper mapper = new TaTServiceWebExterneMapper();
			TaTServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTServiceWebExterneDTO dto) throws RemoveException {
		try {
			TaTServiceWebExterneMapper mapper = new TaTServiceWebExterneMapper();
			TaTServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTServiceWebExterne refresh(TaTServiceWebExterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTServiceWebExterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTServiceWebExterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTServiceWebExterneDTO dto, String validationContext) {
		try {
			TaTServiceWebExterneMapper mapper = new TaTServiceWebExterneMapper();
			TaTServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTServiceWebExterneDTO> validator = new BeanValidator<TaTServiceWebExterneDTO>(TaTServiceWebExterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTServiceWebExterneDTO dto, String propertyName, String validationContext) {
		try {
			TaTServiceWebExterneMapper mapper = new TaTServiceWebExterneMapper();
			TaTServiceWebExterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTServiceWebExterneDTO> validator = new BeanValidator<TaTServiceWebExterneDTO>(TaTServiceWebExterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTServiceWebExterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTServiceWebExterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTServiceWebExterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTServiceWebExterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}
